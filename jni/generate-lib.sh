#!/bin/bash
echo "Using JAVA_HOME: ${JAVA_HOME}"
mkdir src/main/resources/
g++ -shared -fpic -o src/main/resources/libjava_shared_memory_lib.so -I./target -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux jni/java_shared_memory.cpp
cp src/main/resources/libjava_shared_memory_lib.so target/