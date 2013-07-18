class TestParsing<interface Subj> {

	public subobject Subj ref;
	private subobject Subj ref1;
	subobject Subj ref2;
	
	TestParsing (Subj s) {ref = s;}

	<R,A>[m] for (public R m(A) : Subj.methods)
	public R m (A a) { 
		System.out.println("method called with arg" + a); 
		return ref.m(a); 
	}
}
