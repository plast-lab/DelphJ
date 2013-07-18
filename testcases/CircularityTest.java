package testcases;

class CircularA<class X extends CircularA<X>> extends X { 
}

class CircularB<class X extends CircularB<X>> { 
    <R>[m]for (R m() : X.methods)
    R m () { return null; }
} 

class CircularC<X extends D> {
    <R>[m]for (R m() : X.methods)
    R m () { return null; }
}

class D extends CircularC<D> {
}
