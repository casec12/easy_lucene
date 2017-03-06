#! /bin/bash
echo "usage run.sh {path-to-lucene}"
java org.apache.lucene.demo.IndexFiles -docs {path-to-lucene}
INDEX_PATH=$0
export CLASSPATH=.:lucene-analyzers-common-6.4.1.jar:lucene-core-6.4.1.jar:lucene-demo-6.4.1.jar:lucene-queryparser-6.4.1.jar
java org.apache.lucene.demo.IndexFiles -docs $0