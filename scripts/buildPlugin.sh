#!/bin/bash
javac  -d ./build -cp ".:BitSequencer" BitSequencer/*.java
cd build && jar cvf ./BitSequencer.jar ./BitSequencer/* && cd ..
javac  -d ./build -cp ".:./ART/art.jar:./build/BitSequencer.jar" ValueUserPlugin.java
cd build && jar uf ./BitSequencer.jar *.class && cd ..
