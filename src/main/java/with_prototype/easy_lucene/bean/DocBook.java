package with_prototype.easy_lucene.bean;

public class DocBook extends BaseBean {
    private String id;
    private String name;
    private String[] describs;
    private String pages;
    private String printdate;
    private String width;
    private String height;
    private String rangedate;

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public DocBook() {
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public String getPrintdate() {
        return printdate;
    }

    public void setPrintdate(String printdate) {
        this.printdate = printdate;
    }

    public String getRangedate() {
        return rangedate;
    }

    public void setRangedate(String rangedate) {
        this.rangedate = rangedate;
    }
}
