package testcases.junit;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import examples.*;

public class TestGetterSetter extends TestCase {
    GetterSetter<MyBeanClass> gs = new GetterSetter<MyBeanClass>();

    @Test public void testGetterSetter() {
	assertTrue(gs.getf1() == 0);
    }
    @Test public void testGetterSetter2() {
	assertTrue(gs.getf2() == null);
    }

    @Test public void testGetterSetter3() {
	assertTrue(gs.getf3() != null);
    }

    @Test public void testGetterSetter4() {
	gs.setf1(3);
	System.out.println("f1: " + gs.getf1());
	assertTrue(gs.getf1() == 3);
    }

    @Test public void testGetterSetter5() {
	gs.setf2(new HashMap());
	assertTrue(gs.getf2().get("foo").equals("bar"));
    }
    
    @Test public void testGetterSetter6() {
	gs.setf5(7.78d);
	System.out.println("f5: " + gs.getf5());
	assertTrue(gs.getf5() == 7.78d);
    }

}

class MyBeanClass {
    public int f1;
    protected HashMap f2;
    private List f3;
    HashMap f4;
    public double f5;

    public List getf3() { return new ArrayList(); }
    public void setf2(HashMap m) {
	f2 = new HashMap();
	f2.put("foo", "bar");
    }
}

class GetterSetterMyBeanClass extends MyBeanClass {
    /*
    public List getf3() { return super.getf3(); }
    public void setf1(int i) { this.f1 = i; }
    */
    public double getf5 () { return super.f5; }
    public void setf5 ( double d ) { f5 = d; }
}
