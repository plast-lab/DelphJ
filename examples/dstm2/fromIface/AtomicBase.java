package examples.dstm2.fromIface;

import examples.dstm2.fromIface.ofree.*;
import examples.dstm2.fromIface.shadow.*;
import examples.DefaultImplementation;

public class AtomicBase<interface I> 
    extends DefaultImplementation<AtomicBase<I>, I> implements I {

    public AtomicBase () {
	super(null);
	super.x = this;
    }

    <F1,F2> errorif ( some F1 getadaptor() : I.methods ; 
		      some F2 adaptor : DefaultImplementation<AtomicBase<I>,I>.fields )
    protected Adaptor<I> adaptor;

    // todo: need to match @atomic F
    <F>[f] for ( F get#f() : I.methods; 
		 some set#f(F) : I.methods ;
		 error some get#f() : Object.methods ;
		 error some F f : DefaultImplementation<AtomicBase<I>,I>.fields 
) {|    
    F f;
    public F get#f() { return adaptor.get#f(); }
    public void set#f( F value ) { adaptor.set#f(value); }
    public F notransGet#f() { return f; }
    public void notransSet#f( F value ) { f = value; }
    |}
}