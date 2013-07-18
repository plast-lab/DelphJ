package examples.dstm2fromClass.shadow;

import examples.dstm2fromClass.atomic;
import examples.dstm2fromClass.Adaptor;
import examples.dstm2fromClass.AtomicBase;

@atomic public class Recoverable<Z> extends AtomicBase<Z> { 
    // implements Releasable {

    public Recoverable() {
	adaptor = new ShadowGetterSetter<Z>(this);
    }

    // shadow fields.
    // TODO: this requires knowledge that all of AtomicBase<Z>'s fields
    // has the same name as Z's fields.
    <F1>[f1]for ( !private !static F1 f1 : AtomicBase<Z>.fields
		  ; no shadow#f1 : AtomicBase<Z>.fields ) 	 
    F1 shadow#f1;

    // TODO: errorif.
    // this condition rules out the case where Z has a method recover()
    // that returns a non-void type.
    public void recover () {
	<F2>[f2] for ( !private !static F2 f2 : AtomicBase<Z>.fields ; 
		       no shadow#f2 : AtomicBase<Z>.fields )
	f2 = shadow#f2;

	return;
    }

    public void backup () {
	<F3>[f3] for ( !private !static F3 f3 : AtomicBase<Z>.fields ; 
		       no shadow#f3 : AtomicBase<Z>.fields )
	shadow#f3 = f3; 

	// TODO: bug! Why does bytecode generated without return act
	// so f***ed up?!?!?!?
	return;
    }
}  

