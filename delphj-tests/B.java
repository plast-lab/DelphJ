
public class B {

	public subobject A b1;

	public subobject Integer b2 = 10;

	public String meth2(){
		Integer val = this.b2;

		this.b2 = val;

		return "class B::meth2";
	}
} 
