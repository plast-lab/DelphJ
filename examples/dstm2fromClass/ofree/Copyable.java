package examples.dstm2fromClass.ofree;

import examples.dstm2fromClass.atomic;
import examples.dstm2fromClass.AtomicBase;

@atomic public class Copyable<X> {
    AtomicBase<X> dest;

    public Copyable( AtomicBase<X> x ) { dest = x; }
    
    // provide copy method.
    public void copyFrom(AtomicBase<X> src) {
	int i =0;
	<F>[f]for ( public F get#f() : AtomicBase<X>.methods; 
		    some public set#f(F) : AtomicBase<X>.methods )
	dest.set#f(src.get#f());		    

	i++;
    }

    public AtomicBase<X> getDest() { return dest; }
}