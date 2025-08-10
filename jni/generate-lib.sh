#!/bin/bash
echo "Using JAVA_HOME: ${JAVA_HOME}"
g++ -shared -fpic -o target/libjava_shared_memory_lib.so -I./target -I${JAVA_HOME}/include -I${JAVA_HOME}/include/linux jni/java_shared_memory.cpp