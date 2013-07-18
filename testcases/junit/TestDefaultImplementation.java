package testcases.junit;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import examples.*;

public class TestDefaultImplementation extends TestCase {

    @Test public void testDefaultImplementation() {
	IncompleteList il = new IncompleteList();

	DefaultImplementation<IncompleteList, List> l = 
	    new DefaultImplementation<IncompleteList, List>(il);

	l.clear(); // OK.

	assertTrue(7 == l.size());
    }

    @Test public void testDefaultImplementation2() {
	IncompleteList il = new IncompleteList();

	DefaultImplementation<IncompleteList, List> l = 
	    new DefaultImplementation<IncompleteList, List>(il);

	assertTrue(l.foo().equals("foo"));
    }

    @Test public void testDefaultImplementation3() {
	IncompleteList il = new IncompleteList();

	DefaultImplementation<IncompleteList, List> l = 
	    new DefaultImplementation<IncompleteList, List>(il);
	assertTrue(l.iterator() == null);
    }

    @Test public void testDefaultImplementation4() {
	IncompleteList il = new IncompleteList();

	DefaultImplementation<IncompleteList, List> l = 
	    new DefaultImplementation<IncompleteList, List>(il);

	assertTrue(l.remove(null) == false);
    }

    @Test public void testDefaultImplementation5() {
	IncompleteList il = new IncompleteList();

	DefaultImplementation<IncompleteList, List> l = 
	    new DefaultImplementation<IncompleteList, List>(il);

	assertTrue(l.indexOf(null) == 0);
    }

    @Test public void testDefaultImplementation6() {
	IncompleteList il = new IncompleteList();

	List l = new DefaultImplementation<IncompleteList, List>(il);
	assertTrue(l.indexOf(null) == 0);
    }
}

class IncompleteList {
    // only implement clear() and size()
    public void clear() {}
    public int size() { return 7; }
    public String foo() { return "foo"; }
}

/*
public class DefaultImplementationOfIncompleteList implements List {
    IncompleteList x;
    public DefaultImplementationOfIncompleteList(IncompleteList x) {
	this.x = x;
    }

    public void clear() {}
    public int size() { return 7; }
    public String foo() { return "foo"; }

    public boolean addAll(java.util.Collection c ) { return false; }
    //    public int size() { return 0; }
    public Iterator iterator() { return null; }
    public boolean add(java.lang.Object o) { return false; }
    public boolean containsAll(java.util.Collection c) { return false; }
    public boolean retainAll(java.util.Collection c) { return false; }
    //    public void clear() { }
    public List subList(int i, int j) { return null; }
    public boolean addAll(int i, java.util.Collection c) { return false; }
    public Object[] toArray(java.lang.Object[] a ) { return null; }
    public Object get(int i) { return null; }
    public int indexOf(java.lang.Object a) { return 0; }
    public int lastIndexOf(java.lang.Object a) { return 0; }
    public ListIterator listIterator(int i) { return null;}
    public Object set(int i, java.lang.Object o) { return null; }
    public void add(int i, java.lang.Object o) { }
    public Object[] toArray() { return null; } 
    public boolean contains(java.lang.Object o) { return false; }
    public Object remove(int i) { return null; }
    public ListIterator listIterator() { return null; }
    public boolean isEmpty() { return false; }
    public boolean remove(java.lang.Object o) { return false; }
    public boolean removeAll(java.util.Collection c) { return false; }    
}
*/
