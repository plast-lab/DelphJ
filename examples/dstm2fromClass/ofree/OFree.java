package examples.dstm2fromClass.ofree;

import examples.dstm2fromClass.*;
import examples.dstm2fromClass.exceptions.*;

public class OFree<X> extends AtomicBase<X> implements Releasable, Snapable<AtomicBase<X>> {

    public OFree () {
	adaptor = new OFreeAdaptor<X>(this);
    }

    public void release() {
	((OFreeAdaptor<X>) adaptor).release();
    }

    public AtomicBase<X> snapshot() {
	return ((OFreeAdaptor<X>) adaptor).snapshot();
    }

    public void validate(AtomicBase<X> snap) {
	((OFreeAdaptor<X>) adaptor).validate(snap);
    }

    public void upgrade(AtomicBase<X> snap) {
	((OFreeAdaptor<X>) adaptor).upgrade(snap);
    }
}