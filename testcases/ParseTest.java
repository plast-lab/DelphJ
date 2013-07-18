package testcases;

/*
class NoTypeParameter<X> {
    [m] for ( void m () : X.methods )
	void m () { }
}

class NoNameParameter<X> {
    <R extends Object,A*> for ( R m (A) : X.methods )
    R m (A args) { return null; }
}

class WithNestedPattern<X,Y> {
    <R>[m] for ( R m () : X.methods ; no R m () : Y.methods ) 
    R m () { return null; }
}

class MultipleNestedPattern<X,Y> {
    <R>[m,n] for ( R m () : X.methods ; 
                   no void m () : Y.methods;
		   some void n() : Y.methods ) 
    R m () { return null; }
}

class FieldPattern<X> {
    <F>[f] for ( F f : X.fields )
    F f;
}

class OptionalReturnType<X> {
    <A>[m] for ( void m (A) : X.methods; no m (A) : X.methods )
	void m (A args) { }
}

class OptionalReturnType2<X> {
    <R,A>[m] for ( m (A) : X.methods; no R m (A) : X.methods )
	void m (A args) { }
}

class OptionalFieldType<X> {
    <R,A>[m] for ( void m (A) : X.methods; no m : X.fields )
    void m (A args) { }
}

class OptionalFieldType2<X> {
    <R,A>[m] for ( m : X.fields; some R m(A) : X.methods )
    void m (A args) { }
}
*/

class Statement<X> {
    X x;

    void foo() {
	<R>[m] for (R m(): X.methods)
	x.m();
    }

    void foo2() {
	<R extends Object>[m] for (R m(): X.methods)
	x.m();
    }

    void foo3() {
	<A* extends Object>[m] for (void m(A): X.methods)
	x.m();
    }

    void foo4() {
	<A*>[m] for (void m(A): X.methods)
	x.m();
    }

    void bar() {
	<R extends Object,A>[m] for (R m(): X.methods)
	x.m();
    }
    void bar2() {
	<R,A>[m] for (R m(A): X.methods)
	x.m();
    }
    void bar3() {
	<R,A*>[m] for (R m(A): X.methods)
	x.m();
    }
    void bar4() {
	<R,A* extends Object>[m] for (R m(A): X.methods)
	x.m();
    }

    void baz() {
	<A*,R extends Object>[m] for (R m(A): X.methods)
	x.m();
    }
    void baz2() {
	<A* extends Object,R extends Object>[m] for (R m(A): X.methods)
	x.m();
    }
}

