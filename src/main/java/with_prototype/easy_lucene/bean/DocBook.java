package with_prototype.easy_lucene.bean;

public class DocBook extends BaseBean {
    private String id;
    private String name;
    private String[] describs;
    private String pages;

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public DocBook() {
    }

    public String[] getDescribs() {
        return describs;
    }

    public void setDescribs(String[] describs) {
        this.describs = describs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
