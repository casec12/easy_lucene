package maybe_useful;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenzhaolei on 2017/2/22.
 */
public class HelloLuceneFS {
    public static void main(String[] args) throws IOException, ParseException {
        // 0. Specify the analyzer for tokenizing text.
        // The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();
        // 1. create the index
        Directory index = new RAMDirectory();
        /*Directory index = FSDirectory.open(Paths.get("/Users/chenzhaolei/Develop/lucene_libs/lucene_fsdirectory_1/index"));*/
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);
//        addDoc(w, "Lucene in Action", "193398817");
//        addDoc(w, "Lucene for Dummies", "55320055Z");
//        addDoc(w, "Managing Gigabytes", "55063554A");
//        addDoc(w, "The Art of Computer Science", "9900333X");
//        addDoc(w, "Hello,World", "1900333A");
//        addDoc(w, "Hello,World", "1900333A");
        addDoc2(w, "Lucene in Action","Lucene for Dummies", "193398817");

        w.close();
        // 2. query
        String querystr = args.length > 0 ? args[0] : "Lucene for Dummies";
        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser("title", analyzer).parse(querystr);
        // 3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
//            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.getFields("title")[0].stringValue() + "\t" + d.getFields("title")[1].stringValue());
        }
        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
    }

    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    private static void addDoc2(IndexWriter w, String title, String title2, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("title", title2, Field.Store.YES));
        // use a string field for isbn because we don't want it tokenized
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
    //1.create index(add lucene)
    //1.1 get input data
    //1.2 analyze
    //1.3 trans to lucene and add to index
    private int indexUpdate(List<Book> tBookList) throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = FSDirectory.open(Paths.get("/Users/chenzhaolei/Develop/lucene_libs/lucene_fsdirectory_1/index"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);

        for(Book tBook: tBookList){
            String title = tBook.getTitle();
            String isbn = tBook.getIsbn();
            addDoc(w, title, isbn);
        }
//        addDoc(w, "Lucene in Action", "193398817");
//        addDoc(w, "Lucene for Dummies", "55320055Z");
//        addDoc(w, "Managing Gigabytes", "55063554A");
//        addDoc(w, "The Art of Computer Science", "9900333X");
//        addDoc(w, "Hello,World", "1900333A");

        w.close();
        return 0;
    }


    //2.query
    //2.1 get input data
    //2.2 analyze
    //2.3 search
    //2.4 show lucene's data
    private Object[] topDocumentsSearch(Object[] objects){

        return new Object[]{};
    }


    //3.batchCreateIndex
    //3.0 trans input data to format data , and ready add to index(out of the function)
    //3.1 recycle create index(batch add lucene)
    private int batchIndexUpdate(Object[] objects){
        //recycle invork indexUpdate methord
        return 0;
    }

    //a example bean to be indexed
    class Book{
        private String title;
        private String isbn;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }
    }

    abstract class DocumentBean{
        /**
         * 文档类型
         * 用于控制搜索结果的大类正确
         * e.g 以用户为核心的文档，documentType="User"，以商品为核心的文档，documentType="Product"。
         */
        private String documentType;

        public String getDocumentType() {
            return documentType;
        }

        public void setDocumentType(String documentType) {
            this.documentType = documentType;
        }

        public Map<String, String> getFieldMap() {
            return fieldMap;
        }

        public void setFieldMap(Map<String, String> fieldMap) {
            this.fieldMap = fieldMap;
        }

        public DocumentBean(){};
        Map<String,String> fieldMap = new HashMap<String,String>();

    }
}
