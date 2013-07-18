interface PublicInterface<X> { 
	<R,A*>[m] for (public R m(A) : X.methods ; no R m(A) : Object.methods)
	public R m (A a);
}
