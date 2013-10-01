package main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import AST.*;

class MJCompiler extends Frontend {
    public static void main(String args[]) {
	if (!compile(args))
	    System.exit(1);
    }

    static java.util.Set expansionTypes = new java.util.HashSet();

    public static boolean compile(String args[]) {
	MJCompiler compiler = new MJCompiler();
	boolean b = compiler.process(args, new BytecodeParser(), new JavaParser() {
		public CompilationUnit parse(java.io.InputStream is, String fileName)
		    throws java.io.IOException, beaver.Parser.Exception {
		    return new parser.JavaParser().parse(is, fileName);
		}
	    });

	// save the generated output
	try {
	    PrintWriter writer = new PrintWriter("/home/bibou/Projects/DelphJ/delphj-tests/Generated.java", "UTF-8");
	    writer.write(compiler.program.toString());
	    writer.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
		
		
	if(b) {
	    // generate the expanded classes.
	    for (java.util.Iterator iter = compiler.program.compilationUnitIterator(); iter.hasNext();) {
		CompilationUnit unit = (CompilationUnit) iter.next();

		if (unit.fromSource()) {
		    unit.transformation();
		    unit.generateClassfile();
		}

		for (int i = 0; i < unit.getNumTypeDecl(); i++) {
		    TypeDecl td = unit.getTypeDecl(i);
		    if (td.isGenericType() && td.needsExpansion()) {
			List list = null;
			if (td instanceof GenericClassDecl)
			    list = ((GenericClassDecl) td).getParTypeDeclList();
			else if (td instanceof GenericInterfaceDecl)
			    list = ((GenericInterfaceDecl) td).getParTypeDeclList();

			for (int j = 0; j < list.getNumChild(); j++) {
			    TypeDecl parTypeDecl = (TypeDecl) list.getChild(j);
			    // TODO: need to check for timestamp of maybe
			    // compiled file
			    // to make sure this expanded class doesn't already
			    // exist
			    if (!parTypeDecl.isRawType() && parTypeDecl.grounded())
				parTypeDecl.generateClassfile();
			}
		    }
		}
	    }

	    // clear.
	    for (java.util.Iterator iter = compiler.program.compilationUnitIterator(); iter.hasNext();) {
		CompilationUnit unit = (CompilationUnit) iter.next();
		if (unit.fromSource()) {
		    unit.generateClassfile();
		    for (int i = 0; i < unit.getNumTypeDecl(); i++) {
			unit.getTypeDecl(i).clear();
		    }
		}
	    }
	}
	return b;
    }

    /*
     * protected void processNoErrors(CompilationUnit unit) {
     * unit.transformation(); unit.generateClassfile(); }
     */

    protected String name() {
	return "DelphJ Compiler";
    }

    protected String version() {
	return "R20131002";
    }

    protected void initOptions() {
	super.initOptions();
	program.addKeyOption("-rewritings");
    }

    protected void printUsage() {
	super.printUsage();
	System.out.println("Debugging options:");
	System.out.println("  -rewritings               Print rewritings for debugging ");
    }
}
