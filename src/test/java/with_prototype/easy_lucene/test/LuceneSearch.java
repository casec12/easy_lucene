package with_prototype.easy_lucene.test;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import with_prototype.easy_lucene.config.Config;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by chenzhaolei on 2017/3/1.
 */
public class LuceneSearch {
    public static void main(String[] args) throws IOException, ParseException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = FSDirectory.open(Paths.get(Config.getInit_param_fs_index_path()));
        String querystr = args.length > 0 ? args[0] : "id001";
//        Query q = new QueryParser("docId", analyzer).parse(querystr);
        Query q = new TermQuery(new Term("docId",querystr));
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("name") + "\t" + d.get("describs") + "\t" + d.get("pages") + "\t" + d.get("docId") + "\t" + d.get("docType"));
        }
        reader.close();
    }

}
