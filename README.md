DelphJ
======

DelphJ is a Java-based OO language (MorphJ with delegation features) that
eschews inheritance completely, in favor of a combination of class morphing and
(deep) delegation. DelphJ runs on top of the JVM and is based on the [MorphJ
compiler][1]. `delphjc` is implemented via AST rewriting using JastAddJ. In a
nutshell the implementation of the compiler is based on two things:

1. A runtime system that is implemented in a core class named `Reference` that implements the dynamic semantics described in the paper.  
2. A rewrite system on-top the MorphJ compiler.

You can see differences between the original MorphJ implementation and DelphJ with the following git
command:

`git diff -w morphj..master`
     
Rewritings employed
-------------------
1. Field declaration.
2. New instance expression.
3. Method declaration, adding one parameter with changed reference types.
4. Dot accesses.
  1. Method invocation to a reflective invocation with self as first parameter.
  2. Exposing subobject field for path merging.
  3. Adding getData() method access when invoking method on subobject fields.
5. Disambiguate package access to variable access.
6. Variable declaration.

Building & Running the compiler
-------------------------------

To build the compiler run ant with DelphJ's backend build.xml

`ant -f ~/DelphJ/MJBackend/src/build.xml`

The compiler runs via the `delphjc` executable script (which must be part of the
PATH envirnment variable) and requires one more to be set which points to the
DelphJ root directory:

`export mj=<path-to>/DelphJ`

And one for the java classpath:

`export CLASSPATH=$mj/MJBackend/src:$mj/MJBackend/thirdparty/asm-all-3.1.jar:/usr/share/java/junit4.jar`

The compiler can be run via command prompt with the `delphjc` command:

`usage: delphjc [[-r | --rewritings ] | [-h] | [--whatif]] files`

Documentation
-------------
Description, examples and formal discussion of DelphJ:
- [Forsaking Inheritance: Supercharged Delegation in DelphJ][5]

MorphJ papers:
- [Expressive and Safe Static Reflection with MorphJ][2]
- [Morphing: Safely Shaping a Class in the Image of Others][3]
- [Morphing Software for Easier Evolution][4]

* This project is a work in progress and is based on our [OOPSLA'13 paper][5];it is for demonstration purposes only.*

[1]: http://code.google.com/p/morphing/wiki/MorphJ
[2]: http://www.cs.umass.edu/~yannis/morphj-pldi08.pdf
[3]: http://www.cs.umass.edu/~yannis/mj.pdf
[4]: http://www.cs.umass.edu/~yannis/ramse07.pdf
[5]: http://www.di.uoa.gr/~biboudis/forsaking-inheritance-oopsla13.pdf
