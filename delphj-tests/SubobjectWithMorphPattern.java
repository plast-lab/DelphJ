
public class SubobjectWithMorphPattern<T> {

	public subobject T d1;

	String meth2(String param1, Integer param2)
	{
		return "class B::meth2";
	}

	<R,A>[m] for (public R m(A) : T.methods)
	public R m (A a) { 
		System.out.println("method called with arg" + a); 
		return d1.m(a); 
	}

} 
