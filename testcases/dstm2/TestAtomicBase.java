package testcases.dstm2;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.*;
import examples.dstm2fromClass.*;
import examples.dstm2fromClass.shadow.*;
import examples.dstm2fromClass.ofree.*;

public class TestAtomicBase extends TestCase {

    /*    
    @Test public void testBackupRecover() {
	atomicbean.setfoo1(3);
	atomicbean.backup();
	atomicbean.setfoo1(7);

	atomicbean.recover();
	assertTrue(atomicbean.getfoo1() == 3);
    }

    @Test public void testBackupRecover2() {
	atomicbean.setfoo2(7.2f);

	// foo2 is not atomic and this should have no effect.
	atomicbean.backup();

	atomicbean.setfoo2(8.4f);
	atomicbean.recover();

	assertTrue(atomicbean.getfoo2() == 8.4f);
    }
    */
    {
	try {
	    Class managerClass = Class.forName("examples.dstm2fromClass.manager.BackoffManager");
	    Thread.setContentionManagerClass(managerClass);
	} catch (ClassNotFoundException ex) {
	    //reportUsageErrorAndDie();
	    System.err.println(ex.getMessage());
	    System.exit(0);
	}
    }
	
    @Test public void testAtomicBase () {
	AtomicBase<INode> atomicINode;
    }
    @Test public void testRecoverable () {
	AtomicBase<INode> atomicINode = new Recoverable<INode>();
	atomicINode.setvalue(2);
    }
    @Test public void testOFree() {
	AtomicBase<INode> atomicINode = new OFree<INode>();
    }

    /*
    @Test public void testShadowGetter() {
	Recoverable<INode> recoverable = new Recoverable<INode>();
	ShadowGetterSetter<INode> shadow = 
	    new ShadowGetterSetter<INode>(recoverable);
    }
    */
}

class INode {
    @atomic int value;
    @atomic INode next;
    @atomic INode shadowvalue;
    double foo3;
}