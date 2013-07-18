
public class D<T> {

	public String meth1(String param1, Integer param2) {

		this.meth2("this is an arg to method2", new Integer(5));

		return "class D::meth1";
	}

	public String meth2(String msg, Integer i) {
		return "class D::meth2";
	}

	public String meth3() {
		return "class D::meth3";
	}
} 
