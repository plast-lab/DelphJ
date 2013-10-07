
public class TestAssignments<T> {

    public subobject T d1;
    public subobject T d2;

    public void meth(T param1){

	//d1 = d2;
	d1 = d2;

	//d1 = param1;
	d1 = param1;

	//IReference<T> field1 = new Reference<T>(d1.getData(), self);
	T field1 = d1;

	//IReference<Integer> val = new Reference<Integer>(new Integer(5), null);
	Integer val = new Integer(5);
    }
} 
