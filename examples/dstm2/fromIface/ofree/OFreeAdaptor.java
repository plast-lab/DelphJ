package examples.dstm2.fromIface.ofree;

import examples.dstm2.*;
import examples.dstm2.exceptions.*;
import java.util.concurrent.atomic.AtomicReference;

public class OFreeAdaptor<Y> extends Adaptor<Y> implements Releasable, Snapable<AtomicBase<Y>> {
    OFree<Y> ofreeObj;
    AtomicReference<Locator<Y>> start;
    
    /**
     * Creates a new instance of Adapter
     */
    public OFreeAdaptor ( OFree<Y> o ) {
	ofreeObj = o;
	Locator<Y> locator = new Locator<Y>(Transaction.COMMITTED, 
					    new Copyable<Y>(ofreeObj));
	start = new AtomicReference<Locator<Y>>(locator);
    }
    

    <F4 extends Object>[f4] for ( @atomic F4 f4 : Y.fields ; 
				  no get#f4 () : Object.methods ) {|
    public AtomicBase<F4> get#f4() {
	Copyable<Y> version = start.get().newVersion;
	try {
            Transaction me  = Thread.getTransaction();
            Locator<Y> oldLocator = start.get();
            version = oldLocator.fastPath(me);
            if (version == null) {
		ContentionManager manager = Thread.getContentionManager();
		Locator<Y> newLocator = new Locator<Y>();
		while (true) {
		    oldLocator.readPath(me, manager, newLocator);
		    if (start.compareAndSet(oldLocator, newLocator)) {
			version = newLocator.newVersion;
			break;
		    }
		    oldLocator = start.get();
		}
		if (!me.isActive()) {
		    throw new AbortedException();
		}
            }
	    return ofreeObj.notransGet#f4();
	} catch (SecurityException e) {
            throw new PanicException(e);
	}
    }
    public void set#f4 ( AtomicBase<F4> value ) {
	Copyable<Y> version = start.get().newVersion;
	
	Transaction me  = Thread.getTransaction();
	Locator<Y> oldLocator = start.get();
	version = oldLocator.fastPath(me);
	if (version != null) {
	    ofreeObj.notransSet#f4(value);
	    return;
	}
	
	ContentionManager manager = Thread.getContentionManager();
	Locator<Y> newLocator = new Locator<Y>(me, new Copyable<Y>(new OFree<Y>()));
	version = newLocator.newVersion;
	while (true) {
	    oldLocator.writePath(me, manager, newLocator);
	    if (!me.isActive()) {
		throw new AbortedException();
	    }
	    ofreeObj.notransSet#f4(value);
	    if (start.compareAndSet(oldLocator, newLocator)) {
		return;
	    }
	    oldLocator = start.get();
	}
    }
    |}

    public void release() {
	Transaction me  = Thread.getTransaction();
	Locator<Y> oldLocator = start.get();
	Copyable<Y> version = oldLocator.fastPath(me);
	if (version == null) {
	    ContentionManager manager = Thread.getContentionManager();
	    Locator<Y> newLocator = new Locator<Y>();
	    version = newLocator.newVersion;
	    while (true) {
		oldLocator.releasePath(me, manager, newLocator);
		if (start.compareAndSet(oldLocator, newLocator)) {
		    break;
		}
		oldLocator = start.get();
	    }
	    if (!me.isActive()) {
		throw new AbortedException();
	    }
	}
	return;
    }
    
    public AtomicBase<Y> snapshot() {
	Transaction me  = Thread.getTransaction();
	Locator<Y> oldLocator = this.start.get();
	Copyable<Y> version = oldLocator.fastPath(me);
	if (version == null) {
	    ContentionManager manager = Thread.getContentionManager();
	    return oldLocator.snapshot(me, manager).getDest();
	} else {
	    return version.getDest();
	}
    }
    
    public void validate(AtomicBase<Y> snap) {
	if (snap != snapshot()) {
	    throw new SnapshotException();
	}
    }
  
    public void upgrade(AtomicBase<Y> snap) {
	Transaction me  = Thread.getTransaction();
	Locator<Y> oldLocator = this.start.get();
	Copyable<Y> version = oldLocator.fastPath(me);
	if (version != null) {
	    if (version.getDest() != snap) {
		throw new SnapshotException();
	    } else {
		return;
	    }
	}
	ContentionManager manager = Thread.getContentionManager();
	Locator<Y> newLocator = new Locator<Y>(me, new Copyable<Y>(new OFree<Y>()));
	while (true) {
	    oldLocator.writePath(me, manager, newLocator);
	    if (!me.isActive()) {
		throw new AbortedException();
	    }
	    if (snap != newLocator.oldVersion.getDest()) {
		throw new SnapshotException();
	    }
	    if (this.start.compareAndSet(oldLocator, newLocator)) {
		return;
	    }
	    oldLocator = this.start.get();
	}
    }    

}
