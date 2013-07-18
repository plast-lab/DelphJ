package examples.dstm2fromClass;

import examples.dstm2fromClass.shadow.Recoverable;

public class AtomicFactory {

    public enum Policy { SHADOW, OFREE, TWOPHASE };

    public static <X> AtomicBase<X> newInstance (Policy p) {
	Recoverable<X> x = new Recoverable<X>();
	return x;

	/*
	if ( p == Policy.SHADOW )
	    return new Recoverable<X>();
	if ( p == Policy.OFREE )
	    return new OFree<X>();
	return new TwoPhase<X>();
	*/
    }

}