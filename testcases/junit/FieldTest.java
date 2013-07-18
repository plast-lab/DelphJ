package testcases.junit;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import testcases.*;

public class FieldTest extends TestCase {
    RefField1<FieldTestClass> f1 = new RefField1<FieldTestClass>();
    RefField2<FieldTestClass> f2 = new RefField2<FieldTestClass>();

    @Test public void testField11() {
	assertTrue(f1.publicfoo == 0);
    }
    @Test public void testField12() {
	assertTrue(f1.publiclist1 == null);
    }
    @Test public void testField13() {
	assertTrue(f1.publiclist2 == null);
    }

    @Test public void testField21() {
	assertTrue(f2.getpublicfoo() == 0);
    }
}

class RefField1<X> {
    <F1>[f1]for(F1 f1 : X.fields)
    public F1 f1;
}

class RefField2<X> {
    <F2>[f2] for (F2 f2 : RefField1<X>.fields)
    protected F2 f2;

    <F3>[f3] for (F3 f3 : RefField1<X>.fields; no get#f3() : Object.fields )
    public F3 get#f3() { return f3; }
}

class FieldTestClass {
    public int publicfoo = 0;
    public List publiclist1 = null;
    public List publiclist2 = new ArrayList();

    protected int protectedfoo = 0;
    protected List protectedlist1 = null;
    protected List protectedlist2 = new ArrayList();

    private int privatefoo = 0;
    private List privatelist1 = null;
    private List privatelist2 = new ArrayList();
    
    int nomodfoo;
    List noModList1 = null;
    List noModList2 = new ArrayList();
}
