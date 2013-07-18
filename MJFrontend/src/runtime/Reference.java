package runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class Reference<T> implements IReference<T>, Cloneable {

	public T data;
	public IReference next;

	public Reference(T data, IReference next) {
		this.data = data;
		this.next = next;
	}

	public <X> IReference<X> loadField(String fieldName) {
		Class<?> c = data.getClass();

		IReference<X> tmp1 = null;

		try {
			Field field = c.getDeclaredField(fieldName);
			tmp1 = (IReference<X>) field.get((T) data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		IReference<X> ret = new Reference(tmp1.getData(), this);
        return ret;
	}

	public <X> X invoke(String name, Object[] args) {
		Class<?>[] argsRuntimeTypes = new Class[args.length];

		for (int i = 0; i < argsRuntimeTypes.length; i++)
			argsRuntimeTypes[i] = args[i].getClass();

		return this.invoke(name, argsRuntimeTypes,args);
	}

    public IReference<?> getNext(){
        return this.next;
    }

	public <X> X invoke(final String methodName, Class<?>[] cls, Object[] args) {
		Method finalMethod = null;
		Object thisObject = null;
		for (IReference iterator = this; iterator != null; iterator = iterator.getNext()) {
			final Method nextMethod;
			try {
				nextMethod = iterator.getData().getClass().getDeclaredMethod(methodName, cls);
				finalMethod = nextMethod;
				thisObject = iterator.getData();
				continue;
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// No found method means unsoundness at this point.
		// assert (finalMethod != null);
		X result = null;
		try {
			result = (X) finalMethod.invoke(thisObject, args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public String toString() {
		return "Reference " + data + " -> " + next;
	}

	public T getData() {
		return data;
	}

	/* public static void main(String[] args) {

		// Generated from: A a = new A();
		final A tmp1 = new A();
		final IReference<A> tmp2 = new Reference<A>(tmp1, null);
		IReference<A> a = tmp2;

		// Generated from: B b = new B();
		final B tmp3 = new B();
		final IReference<B> tmp4 = new Reference<B>(tmp3, null);
		IReference<B> b = tmp4;

		// Generated from: C b = new C();
		final C tmp10 = new C();
		final IReference<C> tmp11 = new Reference<C>(tmp10, null);
		IReference<C> c = tmp11;

		// Generated from: b.b1 = a;
        b.getData().b1 = a;

		// Generated from: String result = b.b1.meth1("Test String.", 10);
		final IReference<B> tmp5 		= b.loadField("b1");
		final IReference<String> tmp6 	= new Reference<String>("Test String.", null);
		final IReference<Integer> tmp7 	= new Reference<Integer>(10, null);
		IReference<String> result = tmp5.invoke("meth1", 
					new Class[]  { IReference.class, IReference.class, IReference.class }, 
					new Object[] { tmp5, tmp6, tmp7 });

		// Generated from: System.out.println("Result is " + result);
        System.out.println("Result is " + result.getData());

		// Generated from: result = c.meth1(b);
		final IReference<C> tmp12 		= c;
		final IReference<B> tmp13 		= new Reference<B>(b.getData(), b);
		result = tmp12.invoke("meth1", 
					new Class[]  { IReference.class, IReference.class}, 
					new Object[] { tmp12, tmp13 });

		// Generated from: System.out.println("Result is " + result);
        System.out.println("Result is " + result.getData());     
	}

	static class A {
				
		// Generated from: public String meth1(String param1, Integer param2)
		public IReference<String> meth1(final IReference<A> self, 
					IReference<String> param1, IReference<Integer> param2_ref) {
			
			// Generated from: System.out.println(param1);
			System.out.println(param1.getData());
			
			// Generated from: return this.meth2("this is an arg to method2", 25);
            final IReference<A> tmp1 		= self;
            final IReference<String> tmp2 	= new Reference<String>("this is an arg to method2",null);
            final IReference<Integer> tmp3 	= new Reference<Integer>(25,null);
            final Class[] cls0 				= new Class[]  { IReference.class, IReference.class, IReference.class };
            final Object[] args0 			= new Object[] { tmp1, tmp2, tmp3 };
			return (IReference<String>) self.invoke("meth2",cls0,args0);
		}

		// Generated from: public String meth2(String msg, Integer i)
		public IReference<String> meth2(final IReference<A> self, IReference<String> msg, IReference<Integer> i) {
			
			// Generated from: return "class A::meth2";
			return new Reference("class A::meth2", null);
		}
	}

	static class B {
		// Generated from: public subobject A b1;
		public IReference<A> b1;

		// Generated from: public subobject Integer b2 = 10;
		public IReference<Integer> b2 = new Reference<Integer>(10, null);

		// Generated from: public String meth2(String param1, Integer param2)
		public IReference<String> meth2(final IReference<B> self, IReference<String> param1, IReference<Integer> param2) {

			// Generated from: Integer val = this.b2;
			IReference<Integer> val = new Reference<Integer>(this.b2.getData(), self);
			
			// Generated from: this.b2 = val;
			this.b2 = val;
			
			// Generated from: return "class B::meth2";
			return new Reference<String>("class B::meth2", null);
		}
	}

	static class C {
		// Generated from: public subobject B c1 = null;
		public IReference<B> c1 = new Reference<B>(null, null);

		// Generated from: public String meth1(B param1)
		public IReference<String> meth1(final IReference<C> self, IReference<B> param1) {
			 
			 // Generated from: this.c1 = param1;
			 this.c1 = param1;

			 // Generated from: return "class C::meth1";
			 return new Reference("class C::meth1", null);
		}
	}*/
}
