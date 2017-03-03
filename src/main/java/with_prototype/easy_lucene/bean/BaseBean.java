package with_prototype.easy_lucene.bean;

/**
 * EasyLucene框架的基本Bean
 */
public class BaseBean {
    private String docId;
    private String docType;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
