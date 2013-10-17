
class TestDispatch {

    public static void main(String[] args) {
	A a = new A();

	B b = new B();

	C c = new C();

	c.c1.b1 = a;
	
	// b.b1.compareTo(5);

	// c.c1.b1 = a; 

	// String result = b.b1.meth1("lala", 10);

	// System.out.println("Result is " + result);

	// result = c.meth1(b);

	// System.out.println("Result is " + result);
    }
}
