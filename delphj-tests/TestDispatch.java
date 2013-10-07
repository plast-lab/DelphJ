
class TestDispatch {

    public static void main(String[] args) {
		A a = new A();

		B b = new B();

		C c = new C();

		//b.b1 = a;
		b.b1 = a; 

		//c.getData().c1.getData().b1 = a;
		
		//C tmp1 = (C) (c.getData());
		//B tmp2 = (B) (tmp1.c1.getData());
		//A tmp3 = (A) (tmp2.b1);
		//a = tmp3;
		//c.c1.b1 = a; 

		//String result = b.b1.meth1("lala", 10);

		// System.out.println("Result is " + result);

		// result = c.meth1(b);

		// System.out.println("Result is " + result);
    }
}
