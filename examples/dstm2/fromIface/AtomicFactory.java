package examples.dstm2fromInterface;

import examples.dstm2fromInterface.shadow.Recoverable;
import examples.dstm2fromInterface.shadow.OFree;

public class AtomicFactory {

    public enum Policy { SHADOW, OFREE, TWOPHASE };

    public static <X> AtomicBase<X> newInstance (Policy p) {
	if ( p == Policy.SHADOW )
	    return new Recoverable<X>();
	//if ( p == Policy.OFREE )
	    return new OFree<X>();
	//	return new TwoPhase<X>();

    }

}