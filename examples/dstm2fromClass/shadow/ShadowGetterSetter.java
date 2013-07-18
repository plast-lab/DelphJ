package examples.dstm2fromClass.shadow;

import examples.dstm2fromClass.ContentionManager;
import examples.dstm2fromClass.Transaction;
import examples.dstm2fromClass.Thread;
import examples.dstm2fromClass.ofree.ReadSet;
import examples.dstm2fromClass.exceptions.PanicException;
import examples.dstm2fromClass.exceptions.AbortedException;
import examples.dstm2fromClass.Releasable;
import examples.dstm2fromClass.atomic;
import examples.dstm2fromClass.AtomicBase;
import examples.dstm2fromClass.Adaptor;

import java.util.Set;


public class ShadowGetterSetter<Y> extends Adaptor<Y> implements Releasable {
    Recoverable<Y> recoverable;
    
    ContentionManager manager;
    Transaction writer;
    ReadSet readers;
    private final String FORMAT = "Unexpected transaction state: %s";

    /**
     * A transaction switches to exclusive mode after being aborted this many times.
     */
    public static final int CONFLICT_THRESHOLD = 0;
    
    public ShadowGetterSetter(Recoverable<Y> x) {
	this.recoverable = x;
	manager = Thread.getContentionManager();
	writer = Transaction.COMMITTED;
	readers = new ReadSet();
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

    // get methods. // TODO primitives.

    // TODO: the follownig pattern cannot be resolved because
    // we can't figure out that for every such method get#f1, there is
    // a field f1 in Recoverable.
    //    <F1>[f1] for ( F1 get#f1() : Recoverable<Y>.methods )

    <F4 extends Object>[f4] for ( @atomic F4 f4 : Y.fields ; 
				  no get#f4 () : Object.methods ) {|
    public AtomicBase<F4> get#f4() {
	try {
            Transaction me  = Thread.getTransaction();
	    Transaction other = null;
            while (true) {
		synchronized (this) {
		    other = openRead(me);
		    //other = openWrite(me);
		    if (other == null) {
			return recoverable.notransGet#f4();
			//return recoverable.f1;
		    }
		}
		manager.resolveConflict(me, other);
            }
	} catch (SecurityException e) {
            throw new PanicException(e);
	}
    }

    // set methods.
    //    <F2>[f2] for ( void set#f2(F2) : Recoverable<Y>.methods )
    public void set#f4( AtomicBase<F4> value) {
	Transaction me  = Thread.getTransaction();
	Transaction other = null;
	Set<Transaction> others = null;
	while (true) {
	    synchronized (this) {
		others = readWriteConflict(me);
		if (others == null) {
		    other = openWrite(me);
		    if (other == null) {
			recoverable.notransSet#f4(value);
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
    }
    |}

    [f5] for ( @atomic int f5 : Y.fields ; no get#f5 () : Object.methods ) {|
    public int get#f5() {
	try {
            Transaction me  = Thread.getTransaction();
	    Transaction other = null;
            while (true) {
		synchronized (this) {
		    other = openRead(me);
		    //other = openWrite(me);
		    if (other == null) {
			return recoverable.notransGet#f5();
			//return recoverable.f1;
		    }
		}
		manager.resolveConflict(me, other);
            }
	} catch (SecurityException e) {
            throw new PanicException(e);
	}
    }
    public void set#f5( int value) {
	Transaction me  = Thread.getTransaction();
	Transaction other = null;
	Set<Transaction> others = null;
	while (true) {
	    synchronized (this) {
		others = readWriteConflict(me);
		if (others == null) {
		    other = openWrite(me);
		    if (other == null) {
			recoverable.notransSet#f5(value);
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
    }
    |}

    /**
     * Tries to open object for reading. Returns reference to conflictin transaction, if one exists
     **/
    public Transaction openRead(Transaction me) {
	// don't try read sharing if contention seems high
	if (me == null) {	// restore object if latest writer aborted
	    if (writer.isAborted()) {
		recoverable.recover();
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
	    recoverable.recover();
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
		recoverable.recover();
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
	    recoverable.backup();
	    break;
	case ABORTED:
	    recoverable.recover();
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