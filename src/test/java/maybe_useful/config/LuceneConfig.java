package maybe_useful.config;

import java.io.IOException;
import java.util.Properties;

public class LuceneConfig {

    public LuceneConfig getConfig() {
        Properties prop = new Properties();
        try {
            prop.load(LuceneConfig.class.getResourceAsStream("lucene_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lucene_path = prop.getProperty("lucene_path");
        return this;
    }

    private String lucene_path = "";

    public String getLucene_path() {
        return lucene_path;
    }

    public void setLucene_path(String lucene_path) {
        this.lucene_path = lucene_path;
    }
}
