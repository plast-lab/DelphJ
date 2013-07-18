package examples.dstm2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import examples.dstm2.ContentionManager;
import examples.dstm2.Transaction;
import examples.dstm2.exceptions.AbortedException;
import examples.dstm2.exceptions.PanicException;
import examples.dstm2.exceptions.SnapshotException;
import examples.dstm2.factory.Factory;
import examples.dstm2.Thread;

public class TwoPhase<X, Y extends Recoverable<X>> extends Y {
    Lock lock;
    boolean firstTime;
    private final String FORMAT = "Unexpected transaction state: %s";

    //    private static Map<Class,Factory> map = new HashMap<Class,Factory>();
    
    /**
     * Creates a new instance of Adapter
     */
    public Adapter(Class<T> _class) {
	lock = new ReentrantLock();
	/*
	Factory<T> factory = map.get(_class);
	if (factory == null) {
	    factory = new RecoverableFactory(_class);
	    map.put(_class, factory);
	}
	version = factory.create();*/

	firstTime = true;
    }

    <F1>[f1] for ( F1 f1() : Y.fields )
    public F1 get#f1() {
	try{
            lock.lock();
            if (firstTime) {
		backup();
		firstTime = false;
            }
	    Thread.onCommitOnce( new Runnable() {
		    public void run() {
			lock.unlock();
		    }
		});
	    Thread.onAbortOnce( new Runnable() {
		    public void run() {
			lock.unlock();
			recover();
		    }
		});
	    return super.get#f1();
	} catch (IllegalArgumentException ex) {
            throw new PanicException(ex);
	} catch (IllegalAccessException ex) {
            throw new PanicException(ex);
	} catch (InvocationTargetException ex) {
            throw new PanicException(ex);
	}
    }

    <F2>[f2] for ( F2 f2 : Y.fields )
    public void set#f1( F2 value ) {
	try{
            lock.lock();
            if (firstTime) {
		backup();
		firstTime = false;
            }
	    Thread.onCommitOnce( new Runnable() {
		    public void run() {
			lock.unlock();
		    }
		});
	    Thread.onAbortOnce( new Runnable() {
		    public void run() {
			lock.unlock();
			recover();
		    }
		});
	    super.set#f2(value);
	} catch (IllegalArgumentException ex) {
            throw new PanicException(ex);
	} catch (IllegalAccessException ex) {
            throw new PanicException(ex);
	} catch (InvocationTargetException ex) {
            throw new PanicException(ex);
	}
    }
}