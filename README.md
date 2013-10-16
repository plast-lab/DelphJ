DelphJ
======

DelphJ is a Java-based OO language (MorphJ with delegation features) that
eschews inheritance completely, in favor of a combination of class morphing and
(deep) delegation. DelphJ runs on top of the JVM and is based on the MorphJ
compiler. `delphjc` is implemented via AST rewriting using JastAddJ. A numerous
files where added. In a nutshell the implementation of the compiler is based on
two things:

1. A runtime system that is implemented in `Reference.java` (and the supporting interface)
2. A rewrite system ontop the MorphJ compiler.

You can see differences between MorphJ and DelphJ with the following git
command:

`git diff -w morphj..master`
     
Rewritings employed
-------------------
-1.1 Field declaration.
-2.1 New instance expression.
-3.1 Method declaration, adding one parameter with changed reference types.
-4.1 Method invocation to a reflective invocation with self as first parameter.
-4.2 Exposing subobject field for path merging.
-4.3 Adding getData() method access when invoking method on subobject fields.
-5.1 Disambiguate package access to variable access.
-6.1 Variable declaration.

Running the compiler
--------------------

The compiler runs via the `delphjc` executable script (which must be part of the
PATH envirnment variable) and requires one more to be set which points to the
DelphJ root directory:

`export mj=<path-to>/DelphJ`

And one for the java classpath:

`export CLASSPATH=$mj/MJBackend/src:$mj/MJBackend/thirdparty/asm-all-3.1.jar:/usr/share/java/junit4.jar`

The compiler can be run via command prompt with the `delphjc` command:

`usage: delphjc [[-r | --rewritings ] | [-h] | [--whatif]] files`

