package examples.dstm2.fromIface;

import examples.DefaultImplementation;

public abstract class Adaptor<interface X> {

    // todo: need to match @atomic F
    <F>[f] for ( F get#f() : X.methods; 
		 some set#f(F) : X.methods ;
		 error some get#f() : Object.methods ;
		 error some F f : DefaultImplementation<AtomicBase<X>,X>.fields ) {|    
    public abstract F get#f();
    public abstract void set#f(F value);
    |}
}