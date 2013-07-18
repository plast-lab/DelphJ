package examples;

public class GetterSetter<class X> extends X {
    // TODO: inherit default constructors
    public GetterSetter () { }

    <F>[f] for ( !static !private F f : X.fields; no get#f() : X.methods)
    public F get#f () { return f; }

    // unnecessary, but testing out super calls.
    <F,E*>[f] for ( !static !private F f : X.fields; some F get#f() throws E : X.methods)
    public F get#f () throws E { 
	return super.get#f(); 
    }

    <F>[f] for ( !static !private F f : X.fields; no set#f(F) : X.methods)
    public void set#f (F f) { 
	this.f = f;
    }
}
