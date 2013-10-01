#!/bin/bash

java main.MJCompiler -rewritings /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/Reference.java \
    /home/bibou/Projects/DelphJ/MJFrontend/src/runtime/IReference.java \
    /home/bibou/Projects/DelphJ/delphj-tests/D.java
