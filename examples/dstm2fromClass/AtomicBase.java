package examples.dstm2fromClass;

import examples.dstm2fromClass.ofree.*;
import examples.dstm2fromClass.shadow.*;

@atomic public abstract class AtomicBase<X> {

    <F> errorif ( some F adaptor : X.fields )
    protected Adaptor<X> adaptor;

    /**
     * Choice #2: make field types atomic.
     **/
    <F extends Object>[f] for ( @atomic F f : X.fields ; 
				no get#f () : Object.methods ) {|
    AtomicBase<F> f;
    public AtomicBase<F> get#f() { return adaptor.get#f(); }
    public void set#f( AtomicBase<F> value ) { adaptor.set#f(value); }
    public AtomicBase<F> notransGet#f() { return f; }
    public void notransSet#f( AtomicBase<F> value ) { f = value; }
    |}

    // TODO primitives. arrays?
    [fi] for ( @atomic int fi : X.fields ;
	       no get#fi () : Object.methods ) {|
    int fi;
    public int get#fi() { return adaptor.get#fi(); }
    public void set#fi( int value ) { adaptor.set#fi(value); }
    public int notransGet#fi() { return fi; }
    public void notransSet#fi( int value) { fi = value; }
    |}

    /** choice 1, but doesn't work for INode.
    <F>[f] for ( @atomic F f : X.fields ) {|
	F f;
	public abstract F get#f();
	public abstract void set#f( F value );
    |}
    **/

}