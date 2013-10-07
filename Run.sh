#!/bin/bash

# java main.MJCompiler -rewritings /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/Reference.java \
#     /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/IReference.java \
#     /home/bibou/Projects/DelphJ/delphj-tests/TestAssignments.java


java main.MJCompiler -rewritings /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/Reference.java \
    /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/IReference.java \
    /home/bibou/Projects/DelphJ/delphj-tests/A.java 
    # /home/bibou/Projects/DelphJ/delphj-tests/B.java \
    # /home/bibou/Projects/DelphJ/delphj-tests/C.java \
    # /home/bibou/Projects/DelphJ/delphj-tests/TestDispatch.java \
