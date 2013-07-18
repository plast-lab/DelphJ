package testcases.junit;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import examples.*;

public class TestSynchronizeMe extends TestCase {

    @Test public void testSynchronizedList () {
	List myList = new ArrayList();
	SynchronizeMe<List> synchronizedList = 
	    new SynchronizeMe<List>(myList);
	synchronizedList.add("a");
	synchronizedList.add("b");

	int lsize = synchronizedList.size();
	assertTrue(lsize == 2);

	List sublist = synchronizedList.subList(0,1);
	int newsize = sublist.size();
	assertTrue(newsize == 1);

	myList.clear();
	assertTrue(myList.size() == 0 );
    }


    @Test public void testSynchronizedObject() {
	SynchMe s = new SynchMe();
	SynchronizeMe<NeedSynch> synchObj = new SynchronizeMe<NeedSynch>(s);
    }
}

interface NeedSynch {
    public int foo();
    public void bar ( Object o ) ; 
}

class SynchMe implements NeedSynch {
    public int foo () { return 1; }
    public void bar (Object o) {}
}