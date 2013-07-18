
public class TestsAssignments<T> {

	public subobject T d1;
	public subobject T d2;

	public void meth(T param1){

		//this.d1 = d2;
		this.d1 = d2;

		//this.d1 = param1;
		this.d1 = param1;

		//IReference<T> field1 = new Reference<T>(this.d1.getData(), self);
		T field1 = this.d1;

		//IReference<Integer> val = new Reference<Integer>(new Integer(5), null);
		Integer val = new Integer(5);
	}
} 
