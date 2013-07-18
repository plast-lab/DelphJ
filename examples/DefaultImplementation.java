package examples;

public class DefaultImplementation<X, interface I> implements I {
    X x;
    public DefaultImplementation(X x) { this.x = x; }

    // for all methods in I, if the same method does
    // not appear in X, provide default implementation.
    <R extends Object,A*,E*>[m]
    for( R m (A) throws E: I.methods ; no R m (A) : X.methods )
    public R m (A a) throws E { return null; }

    <A1*,E*>[m]
    for( void m (A1) throws E : I.methods ; no void m (A1) : X.methods )
    public void m (A1 a) throws E { }

    <A2*,E*>[m]
    for( boolean m (A2) throws E : I.methods ; no boolean m (A2) : X.methods )
    public boolean m (A2 a) throws E { return false; }

    <A3*,E*>[m]
    for( int m (A3) throws E : I.methods ; no int m (A3) : X.methods )
    public int m (A3 a) throws E { return 0; }

    <A*,E*>[m]
    for( byte m (A) throws E : I.methods ; no byte m (A) : X.methods )
    byte m (A a) throws E { return 0; }

    <A*,E*>[m]
    for( short m (A) throws E : I.methods ; no short m (A) : X.methods )
    short m (A a) throws E { return 0; }

    <A*,E*>[m]
    for( long m (A) throws E : I.methods ; no long m (A) : X.methods )
    long m (A a) throws E { return 0; }

    <A*,E*>[m]
    for( char m (A) throws E : I.methods ; no char m (A) : X.methods )
    char m (A a) throws E { return 0; }

    <A*,E*>[m]
    for( float m (A) throws E : I.methods ; no float m (A) : X.methods )
    float m (A a) throws E { return 0; }

    <A*,E*>[m]
    for( double m (A) throws E : I.methods ; no double m (A) : X.methods )
    double m (A a) throws E { return 0; }

    // for all methods in X that *do* correctly override
    // methods in I, we need to copy them. 
    <R,A*,E*>[m]
    for( !final R m (A) throws E : I.methods ; some R m (A) : X.methods )
    public R m (A a) throws E { return x.m(a); }

    <A*,E*>[m]
    for( !final void m (A) throws E : I.methods ; some void m (A) : X.methods )
    public void m (A a) throws E { x.m(a); }

    // for all methods in X, such that there is no method
    // in I with the same name and arguments, copy method.
    <R,A*>[m]for( R m (A) : X.methods; no m (A) : I.methods)
    public R m (A a) { return x.m(a); }

}
