package examples.dstm2fromInterface;

/**
 * Declare a field for each get*() set*(TYPE) pair in I.
 **/
abstract public class MakeFields<interface I> {
    
    <@atomic F extends Object>[f] 
    for ( F get#f() : I.methods ; 
	  some void set#f(F) : I.methods ;
	  // no way to catch that here.
	  no        set#f()  : I.methods ) 
}