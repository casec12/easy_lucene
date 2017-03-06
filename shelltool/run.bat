echo "usage run.sh {path-to-lucene}"
java org.apache.lucene.demo.IndexFiles -docs {path-to-lucene}
set INDEX_PATH=%1
set CLASSPATH=.;lucene-analyzers-common-6.4.1.jar;lucene-core-6.4.1.jar;lucene-demo-6.4.1.jar;lucene-queryparser-6.4.1.jar
java org.apache.lucene.demo.IndexFiles -docs %INDEX_PATH%