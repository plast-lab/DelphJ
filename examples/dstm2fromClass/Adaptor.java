package examples.dstm2fromClass;

public abstract class Adaptor<X> {

    <F extends Object>[f] for ( @atomic F f : X.fields ; 
				no get#f () : Object.methods ) {|
    public abstract AtomicBase<F> get#f();
    public abstract void set#f(AtomicBase<F> value);
    |}

    [fi] for ( @atomic int fi : X.fields ;
	       no get#fi () : Object.methods ) {|
    public abstract int get#fi();
    public abstract void set#fi(int value);
    |}
}