package examples.dstm2;

import dstm2.ContentionManager;
import dstm2.Transaction;
import dstm2.exceptions.AbortedException;
import dstm2.exceptions.PanicException;
import dstm2.exceptions.SnapshotException;
import dstm2.Thread;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class ShadowAdapter<class X extends Recoverable<X>> extends X {

    ContentionManager manager;
    Transaction writer;
    ReadSet readers;
    private final String FORMAT = "Unexpected transaction state: %s";
    /**
     * A transaction switches to exclusive mode after being aborted this many times.
     */
    public static final int CONFLICT_THRESHOLD = 0;
    
    public ShadowAdaptor () {
	manager = Thread.getContentionManager();
	writer = Transaction.COMMITTED;
	readers = new ReadSet();
    }

    // get methods.
    <@atomic F1>[f1] for (@atomic !private F1 f1 : X.fields ; 
			  no get#f1() : X.methods ) 
    public F1 get#f1() {
	try {
            Transaction me  = Thread.getTransaction();
            Transaction other = null;
            while (true) {
		synchronized (this) {
		    other = openRead(me);
		    //other = openWrite(me);
		    if (other == null) {
			return f1;
		    }
              }
		manager.resolveConflict(me, other);
            }
	} catch (SecurityException e) {
            throw new PanicException(e);
	} catch (IllegalAccessException e) {
            throw new PanicException(e);
	} catch (InvocationTargetException e) {
            throw new PanicException(e);
	}
    }

    // set methods.
    <@atomic F2>[f2] for (@atomic !private F2 f2 : X.fields ; 
			  no set#f2() : X.methods )
    public void set#f2(F2 value) {
	try {
            Transaction me  = Thread.getTransaction();
            Transaction other = null;
            Set<Transaction> others = null;
            while (true) {
              synchronized (this) {
		  others = readWriteConflict(me);
		  if (others == null) {
		      other = openWrite(me);
		      if (other == null) {
			  f2 = value;
			  return;
		      }
		  }
              }
              if (others != null) {
		  manager.resolveConflict(me, others);
              } else if (other != null) {
		  manager.resolveConflict(me, other);
              }
            }
	} catch (IllegalAccessException e) {
            throw new PanicException(e);
	} catch (InvocationTargetException e) {
            throw new PanicException(e);
	}
    }
	
 
    public void release() {
	Transaction me = Thread.getTransaction();
	if (me != null) {
	    boolean ok = readers.remove(me);
	    if (!ok) {
		throw new PanicException("illegal release attempt");
	    }
	}
    }
    
    /**
     * Tries to open object for reading. Returns reference to conflictin transaction, if one exists
     **/
    public Transaction openRead(Transaction me) {
	// don't try read sharing if contention seems high
	if (me == null) {	// restore object if latest writer aborted
	    if (writer.isAborted()) {
		recover();
		writer = Transaction.COMMITTED;
	    }
	    return null;
	}
	if (me.attempts > CONFLICT_THRESHOLD) {
	    return openWrite(me);
	}
	// Am I still active?
	if (!me.isActive()) {
	    throw new AbortedException();
	}
	// Have I already opened this object?
	if (writer == me) {
	    return null;
	}
	switch (writer.getStatus()) {
	case ACTIVE:
	    return writer;
	case COMMITTED:
	    break;
	case ABORTED:
	    recover();
	    break;
	default:
	    throw new PanicException(FORMAT, writer.getStatus());
	}
	writer = Transaction.COMMITTED;
	readers.add(me);
	manager.openSucceeded();
	return null;
    }
    
    /**
     * Tries to open object for reading.
     * Returns reference to conflicting transaction, if one exists
     **/
    Transaction openWrite(Transaction me) {
	boolean cacheHit = false;  // already open for read?
	// not in a transaction
	if (me == null) {	// restore object if latest writer aborted
	    if (writer.isAborted()) {
		recover();
		writer = Transaction.COMMITTED;
	    }
	    return null;
	}
	if (!me.isActive()) {
	    throw new AbortedException();
	}
	if (me == writer) {
	    return null;
	}
	switch (writer.getStatus()) {
	case ACTIVE:
	    return writer;
	case COMMITTED:
	    backup();
	    break;
	case ABORTED:
	    recover();
	    break;
	default:
	    throw new PanicException(FORMAT, writer.getStatus());
	}
	writer = me;
	if (!cacheHit) {
	    me.memRefs++;
	    manager.openSucceeded();
	}
	return null;
    }
    
    public Set<Transaction> readWriteConflict(Transaction me) {
	for (Transaction reader : readers) {
	    if (reader.isActive() && reader != me) {
		return readers;
	    }
	}
	readers.clear();
	return null;
    }
    
}