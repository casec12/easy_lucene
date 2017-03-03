package maybe_useful;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

public class HelloLucene {
    public static void main(String[] args) throws IOException, ParseException {
        // 0. Specify the analyzer for tokenizing text.
        // The same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();
        // 1. create the index
        Directory index = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        IndexWriter w = new IndexWriter(index, config);
        addDoc(w, "title", "isbn");

//        w.deleteDocuments(new Term("title","_title1"));
        w.close();

        // 2. query
//        String querystr = args.length > 0 ? args[0] : "_title*";
        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
//        Query q = new TermQuery(new Term("isbn", "_isbn1,_isbn2"));
////        Query q = new QueryParser("isbn", analyzer).parse("_isbn1_isbn2");
        Query q1 = new TermQuery(new Term("title", "title"));
        Query q2 = new TermQuery(new Term("isbn", "isbn"));
//        BooleanQuery booleanQuery = new BooleanQuery.Builder()
//                .add(q1, BooleanClause.Occur.MUST)
//                .add(q2, BooleanClause.Occur.MUST)
//                .build();
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(q1, BooleanClause.Occur.MUST);
        builder.add(q2, BooleanClause.Occur.MUST);
        BooleanQuery booleanQuery = builder.build();

        Query q = booleanQuery;

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
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title") + "\t" + d.get("desc"));
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
}