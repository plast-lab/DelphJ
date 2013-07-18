package examples.dstm2.fromIface.ofree;

import examples.dstm2.*;
import examples.dstm2.exceptions.*;
import examples.dstm2.fromIface.AtomicBase;

public class OFree<interface X> extends AtomicBase<X> implements Releasable, Snapable<AtomicBase<X>> {

    public OFree () {
	adaptor = new OFreeAdaptor<X>(this);
    }

    errorif ( some release() : AtomicBase<X>.methods )
    public void release() {
	((OFreeAdaptor<X>) adaptor).release();
    }

    errorif ( some snapshot() : AtomicBase<X>.methods )
    public AtomicBase<X> snapshot() {
	return ((OFreeAdaptor<X>) adaptor).snapshot();
    }

    errorif ( some validate(AtomicBase<X>) : AtomicBase<X>.methods )
    public void validate(AtomicBase<X> snap) {
	((OFreeAdaptor<X>) adaptor).validate(snap);
    }

    errorif ( some upgrade(AtomicBase<X>) : AtomicBase<X>.methods )
    public void upgrade(AtomicBase<X> snap) {
	((OFreeAdaptor<X>) adaptor).upgrade(snap);
    }
}