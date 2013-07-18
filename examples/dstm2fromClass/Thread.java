package examples.dstm2fromClass;

import examples.dstm2fromClass.exceptions.AbortedException;
import examples.dstm2fromClass.exceptions.GracefulException;
import examples.dstm2fromClass.exceptions.PanicException;
import examples.dstm2fromClass.exceptions.SnapshotException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * The basic unit of computation for the transactional memory.  This
 * class extends <code>java.lang.Thread</code> by providing methods to
 * begin, commit and abort transactions.
 *
 * Every <code>Thread</code> has a contention manager, created when
 * the thread is created.  Before creating any <code>Thread</code>s,
 * you must call <code>Thread.setContentionManager</code> to set the
 * class of the contention manager that will be created.  The
 * contention manager of a thread is notified (by invoking its
 * notification methods) of the results of any methods involving the
 * thread.  It is also consulted on whether a transaction should be
 * begun.
 *
 * @see examples.dstm2fromClass.ContentionManager
 */
public class Thread extends java.lang.Thread {
  /**
   * Contention manager class.
   */
  protected static Class contentionManagerClass;
  
  /**
   * Adapter class.
   */
    //  protected static Class<examples.dstm2fromClass.factory.Adapter> adapterClass;
  
  /**
   * Set to true when benchmark runs out of time.
   **/
  public static volatile boolean stop = false;
  /**
   * number of committed transactions for all threads
   */
  public static long totalCommitted = 0;
  /**
   * total number of transactions for all threads
   */
  public static long totalTotal = 0;
  /**
   * number of committed memory references for all threads
   */
  public static long totalCommittedMemRefs = 0;
  /**
   * total number of memory references for all threads
   */
  public static long totalTotalMemRefs = 0;
  
  static ThreadLocal<ThreadState> _threadState = new ThreadLocal<ThreadState>() {
    protected synchronized ThreadState initialValue() {
      return new ThreadState();
    }
  };
  static ThreadLocal<Thread> _thread = new ThreadLocal<Thread>() {
    protected synchronized Thread initialValue() {
      return null;
    }
  };
  
  private static int MAX_NESTING_DEPTH = 1;
  
  private static Object lock = new Object();
  
  // Memo-ize factories so we don't have to recreate them.
    /*
  private static Map<Class,Factory> factoryTable
      = Collections.synchronizedMap(new HashMap<Class,Factory>());
    */

  /**
   * Create thread to run a method.
   * @param target execute this object's <CODE>run()</CODE> method
   */
  public Thread(final Runnable target) {
    super(new Runnable() {
      public void run() {
        ThreadState threadState = _threadState.get();
        threadState.reset();
        target.run();
        // collect statistics
        synchronized (lock){
          totalCommitted += threadState.committed;
          totalTotal += threadState.total;
          totalCommittedMemRefs += threadState.committedMemRefs;
          totalTotalMemRefs += threadState.totalMemRefs;
        }
      }
    });
  }
  /**
   * no-arg constructor
   */
  public Thread() {
    super();
  }
  
    public static void main (String[] argv) {
	ContentionManager m = Thread.getContentionManager();
    }

  /**
   * Establishes a contention manager.  You must call this method
   * before creating any <code>Thread</code>.
   *
   * @see examples.dstm2fromClass.ContentionManager
   * @param theClass class of desired contention manager.
   */
  public static void setContentionManagerClass(Class theClass) {
    Class cm;
    try {
      cm = Class.forName("examples.dstm2fromClass.ContentionManager");
    } catch (ClassNotFoundException e) {
      throw new PanicException(e);
    }
    try {
      contentionManagerClass = theClass;
    } catch (Exception e) {
      throw new PanicException("The class " + theClass
          + " does not implement examples.dstm2fromClass.ContentionManager");
    }
  }
  
  /**
   * set Adapter class for this thread
   * @param adapterClassName adapter class as string
   */
    /*
  public static void setAdapterClass(String adapterClassName) {
    try {
      adapterClass = (Class<examples.dstm2fromClass.factory.Adapter>)Class.forName(adapterClassName);
    } catch (ClassNotFoundException ex) {
      throw new PanicException("Adapter class not found: %s\n", adapterClassName);
    }
  }
    */

  /**
   * Tests whether the current transaction can still commit.  Does not
   * actually end the transaction (either <code>commitTransaction</code> or
   * <code>abortTransaction</code> must still be called).  The contention
   * manager of the invoking thread is notified if the onValidate fails
   * because a <code>TMObject</code> opened for reading was invalidated.
   *
   * @return whether the current transaction may commit successfully.
   */
  static public boolean validate() {
    ThreadState threadState = _threadState.get();
    return threadState.validate();
  }
  
  /**
   * Gets the current transaction, if any, of the invoking <code>Thread</code>.
   *
   * @return the current thread's current transaction; <code>null</code> if
   *         there is no current transaction.
   */
  static public Transaction getTransaction() {
    return _threadState.get().transaction;
  }
  
  /**
   * Gets the contention manager of the invoking <code>Thread</code>.
   *
   * @return the invoking thread's contention manager
   */
  static public ContentionManager getContentionManager() {
    return _threadState.get().manager;
  }
  
  /**
   * Create a new factory instance.
   * @param _class class to implement
   * @return new factory
   */
    /*
  static public <T> Factory<T> makeFactory(Class<T> _class) {
    try {
      Factory<T> factory = (Factory<T>) factoryTable.get(_class);
      if (factory == null) {
        factory =  new AtomicFactory<T>(_class, adapterClass);
        factoryTable.put(_class, factory);
      }
      return factory;
    } catch (Exception e) {
      throw new PanicException(e);
    }
  }
    */

  /**
   * Execute a transaction
   * @param xaction execute this object's <CODE>call()</CODE> method.
   * @return result of <CODE>call()</CODE> method
   */
  public static <T> T doIt(Callable<T> xaction) {
    ThreadState threadState = _threadState.get();
    ContentionManager manager = threadState.manager;
    T result = null;

    try {
      while (!Thread.stop) {
        threadState.beginTransaction();
        try {
          result = xaction.call();
        } catch (AbortedException d) {
        } catch (SnapshotException s) {
          threadState.abortTransaction();
        } catch (Exception e) {
          e.printStackTrace();
          throw new PanicException("Unhandled exception " + e);
        }
	threadState.totalMemRefs += threadState.transaction.memRefs;
        if (threadState.commitTransaction()) {
          threadState.committedMemRefs += threadState.transaction.memRefs;
          return result;
        }
        threadState.transaction.attempts++;
        // transaction aborted
      }
      if (threadState.transaction != null) {
        threadState.abortTransaction();
      }
    } finally {
      threadState.transaction = null;
    }

    // collect statistics
    synchronized (lock){
      totalTotalMemRefs = threadState.totalMemRefs;
      totalCommittedMemRefs = threadState.committedMemRefs;
      totalCommitted += threadState.committed;
      totalTotal += threadState.total;
      threadState.reset();  // set up for next iteration
    }
    throw new GracefulException();
  }
  /**
   * Execute transaction
   * @param xaction call this object's <CODE>run()</CODE> method
   */
  public static void doIt(final Runnable xaction) {
    doIt(new Callable<Boolean>() {
      public Boolean call() {
        xaction.run();
        return false;
      };
    });
  }
  
  /**
   * number of transactions committed by this thread
   * @return number of transactions committed by this thread
   */
  public static long getCommitted() {
    return totalCommitted;
  }
  
  /**
   * umber of transactions aborted by this thread
   * @return number of aborted transactions
   */
  public static long getAborted() {
    return totalTotal -  totalCommitted;
  }
  
  /**
   * number of transactions executed by this thread
   * @return number of transactions
   */
  public static long getTotal() {
    return totalTotal;
  }
  
  /**
   * Register a method to be called every time this thread validates any transaction.
   * @param c abort if this object's <CODE>call()</CODE> method returns false
   */
  public static void onValidate(Callable<Boolean> c) {
    _threadState.get().onValidate.add(c);
  }
  /**
   * Register a method to be called every time the current transaction is validated.
   * @param c abort if this object's <CODE>call()</CODE> method returns false
   */
  public static void onValidateOnce(Callable<Boolean> c) {
    _threadState.get().onValidateOnce.add(c);
  }
  /**
   * Register a method to be called every time this thread commits a transaction.
   * @param r call this object's <CODE>run()</CODE> method
   */
  public static void onCommit(Runnable r) {
    _threadState.get().onCommit.add(r);
  }
  /**
   * Register a method to be called once if the current transaction commits.
   * @param r call this object's <CODE>run()</CODE> method
   */
  public static void onCommitOnce(Runnable r) {
    _threadState.get().onCommitOnce.add(r);
  }
  /**
   * Register a method to be called every time this thread aborts a transaction.
   * @param r call this objec't <CODE>run()</CODE> method
   */
  public static void onAbort(Runnable r) {
    _threadState.get().onAbort.add(r);
  }
  /**
   * Register a method to be called once if the current transaction aborts.
   * @param r call this object's <CODE>run()</CODE> method
   */
  public static void onAbortOnce(Runnable r) {
    _threadState.get().onAbortOnce.add(r);
  }
  /**
   * get thread ID for debugging
   * @return unique id
   */
  public static int getID() {
    return _threadState.get().hashCode();
  }
  
  /**
   * reset thread statistics
   */
  public static void clear() {
    totalTotal = 0;
    totalCommitted = 0;
    totalCommittedMemRefs = 0;
    totalTotalMemRefs = 0;
    stop = false;
  }
  
  /**
   * Class that holds thread's actual state
   */
  public static class ThreadState {
    
    int depth = 0;
    ContentionManager manager;
    
    private long committed = 0;        // number of committed transactions
    private long total = 0;            // total number of transactions
    private long committedMemRefs = 0; // number of committed reads and writes
    private long totalMemRefs = 0;     // total number of reads and writes
    
    Set<Callable<Boolean>> onValidate = new HashSet<Callable<Boolean>>();
    Set<Runnable>          onCommit   = new HashSet<Runnable>();
    Set<Runnable>          onAbort    = new HashSet<Runnable>();
    Set<Callable<Boolean>> onValidateOnce = new HashSet<Callable<Boolean>>();
    Set<Runnable>          onCommitOnce   = new HashSet<Runnable>();
    Set<Runnable>          onAbortOnce    = new HashSet<Runnable>();
    
    Transaction transaction = null;
    
    /**
     * Creates new ThreadState
     */
    public ThreadState() {
      try {
        manager = (ContentionManager)Thread.contentionManagerClass.newInstance();
      } catch (NullPointerException e) {
        throw new PanicException("No default contention manager class set.");
      } catch (Exception e) {  // Some problem with instantiation
        throw new PanicException(e);
      }
    }
    
    /**
     * Resets any metering information (commits/aborts, etc).
     */
    public void reset() {
      committed = 0;        // number of committed transactions
      total = 0;            // total number of transactions
      committedMemRefs = 0; // number of committed reads and writes
      totalMemRefs = 0;     // total number of reads and writes
    }
    
    /**
     * used for debugging
     * @return string representation of thread state
     */
    public String toString() {
      return
          "Thread" + hashCode() + "["+
          "committed: " +  committed + "," +
          "aborted: " + ( total -  committed) +
          "]";
    }
    
    /**
     * Can this transaction still commit?
     * This method may be called at any time, not just at transaction end,
     * so we do not clear the onValidateOnce table.
     * @return true iff transaction might still commit
     */
    public boolean validate() {
	try {
	    // permanent
	    for (Callable<Boolean> v : onValidate) {
		if (!v.call()) {
		    return false;
		}
	    }
	    // temporary
	    for (Callable<Boolean> v : onValidateOnce) {
		if (!v.call()) {
		    return false;
		}
	    }
	    return transaction.validate();
	} catch (Exception ex) {
	    return false;
	}
    }
      
    /**
     * Call methods registered to be called on commit.
     */
    public void runCommitHandlers() {
      try {
        // permanent
        for (Runnable r: onCommit) {
          r.run();
        }
        // temporary
        for (Runnable r: onCommitOnce) {
          r.run();
        }
        onCommitOnce.clear();
        onValidateOnce.clear();
      } catch (Exception ex) {
        throw new PanicException(ex);
      }
    }
    
    /**
     * Starts a new transaction.  Cannot nest transactions deeper than
     * <code>Thread.MAX_NESTING_DEPTH.</code> The contention manager of the
     * invoking thread is notified when a transaction is begun.
     */
    public void beginTransaction() {
      transaction = new Transaction();
      if (depth == 0) {
        total++;
      }
      // first thing to fix if we allow nested transactions
      if (depth >= 1) {
        throw new PanicException("beginTransaction: attempting to nest transactions too deeply.");
      }
      depth++;
    }
    
    /**
     * Attempts to commit the current transaction of the invoking
     * <code>Thread</code>.  Always succeeds for nested
     * transactions.  The contention manager of the invoking thread is
     * notified of the result.  If the transaction does not commit
     * because a <code>TMObject</code> opened for reading was
     * invalidated, the contention manager is also notified of the
     * inonValidate.
     *
     *
     * @return whether commit succeeded.
     */
    public boolean commitTransaction() {
      depth--;
      if (depth < 0) {
        throw new PanicException("commitTransaction invoked when no transaction active.");
      }
      if (depth > 0) {
        throw new PanicException("commitTransaction invoked on nested transaction.");
      }
      if (depth == 0) {
        if (validate() && transaction.commit()) {
          committed++;
          runCommitHandlers();
          return true;
        }
        abortTransaction();
        return false;
      } else {
        return true;
      }
    }
    
    /**
     * Aborts the current transaction of the invoking <code>Thread</code>.
     * Does not end transaction, but ensures it will never commit.
     */
    public void abortTransaction() {
      runAbortHandlers();
      transaction.abort();
    }
    
    /**
     * Call methods registered to be called on commit.
     */
    public void runAbortHandlers() {
      try {
        // permanent
        for (Runnable r: onAbort) {
          r.run();
        }
        // temporary
        for (Runnable r: onAbortOnce) {
          r.run();
        }
        onAbortOnce.clear();
        onValidateOnce.clear();
      } catch (Exception ex) {
        throw new PanicException(ex);
      }
    }
  }
}
