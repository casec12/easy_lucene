package maybe_useful.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * Created by chenzhaolei on 2017/2/14.
 */
@SuppressWarnings("ALL")
public class FSLuceneTools {
    private static LuceneConfig mLuceneConfig;
//    static{
//        mLuceneConfig = new LuceneConfig().getConfig();
//    }

    public void writeRAMIndex(String fieldName, String fieldText) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        // Store the index in memory:
        Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
//        Directory directory = FSDirectory.open(Paths.get(mLuceneConfig.getLucene_path()));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        doc.add(new Field(fieldName, fieldText, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();
    }

    public static void main(String[] args) throws IOException {
        new FSLuceneTools().writeFSIndex("field_name","This is the text to be indexed.");
    }
    //TODO

    public void writeFSIndex(String fieldName, String fieldText) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();
        // Store the index in memory:
//        Directory directory = new RAMDirectory();
        // To store an index on disk, use this instead:
//        Directory directory = FSDirectory.open(Paths.get(mLuceneConfig.getLucene_path()));
        Directory directory = FSDirectory.open(Paths.get("/Users/chenzhaolei/Develop/lucene_libs/lucene_fsdirectory_1/index"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter iwriter = new IndexWriter(directory, config);
        Document doc = new Document();
        doc.add(new Field(fieldName, fieldText, TextField.TYPE_STORED));
        iwriter.addDocument(doc);
        iwriter.close();
    }
}

