package with_prototype.easy_lucene.tools;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import with_prototype.easy_lucene.config.Config;

import java.io.IOException;
import java.nio.file.Paths;

public class ConfigTools {
    public static<T> T getDefaultAnalyzer(){
        T Analyzer = null;
        try {
            Analyzer = (T) Config.getDefaultAnalyzerClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return Analyzer;
    }

    public static Directory getDefaultFSDirectory(){
        Directory defaultFSDirectory = null;
        try {
            defaultFSDirectory =  FSDirectory.open(Paths.get(Config.getInit_param_fs_index_path()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return defaultFSDirectory;
    }
}
