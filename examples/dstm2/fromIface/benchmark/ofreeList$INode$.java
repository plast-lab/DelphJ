/* List$INode$ - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package dstm2.benchmark;
import dstm2.exceptions.PanicException;
import dstm2.factory.Adapter;
import dstm2.factory.Releasable;
import dstm2.factory.Snapable;

public class List$INode$ implements List.INode, Releasable, Snapable
{
    static Class _class;
    dstm2.factory.ofree.Adapter adapter
	= new dstm2.factory.ofree.Adapter(_class);
    Adapter.Getter getNext$$;
    Adapter.Setter setNext$$;
    Adapter.Getter getValue$$;
    Adapter.Setter setValue$$;
    
    public List$INode$() {
	try {
	    String string = "dstm2.benchmark.List$INode";
	    Class var_class = Class.forName(string);
	    getNext$$ = adapter.makeGetter("getNext", var_class);
	    setNext$$ = adapter.makeSetter("setNext", var_class);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new PanicException(classnotfoundexception);
	}
	try {
	    String string = "int";
	    String string_0_ = string;
	    Class var_class = Integer.TYPE;
	    getValue$$ = adapter.makeGetter("getValue", var_class);
	    setValue$$ = adapter.makeSetter("setValue", var_class);
	} catch (ClassNotFoundException classnotfoundexception) {
	    throw new PanicException(classnotfoundexception);
	}
    }
    
    public List.INode getNext() {
	return (List.INode) getNext$$.call();
    }
    
    public void setNext(List.INode arg0) {
	setNext$$.call(arg0);
    }
    
    public int getValue() {
	return ((Integer) getValue$$.call()).intValue();
    }
    
    public void setValue(int arg0) {
	setValue$$.call(Integer.valueOf(arg0));
    }
    
    public void release() {
	Releasable releasable = (Releasable) this.adapter;
	releasable.release();
    }
    
    public Object snapshot() {
	Snapable snapable = (Snapable) this.adapter;
	return snapable.snapshot();
    }
    
    public void validate(Object arg0) {
	Snapable snapable = (Snapable) this.adapter;
	snapable.validate(arg0);
    }
    
    public void upgrade(Object arg0) {
	Snapable snapable = (Snapable) this.adapter;
	snapable.upgrade(arg0);
    }
    
    static {
	try {
	    _class = Class.forName("dstm2.benchmark.List$INode");
	} catch (ClassNotFoundException classnotfoundexception) {
	    classnotfoundexception.printStackTrace();
	}
    }
}
