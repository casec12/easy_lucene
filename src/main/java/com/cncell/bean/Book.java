package com.cncell.bean;

import java.util.Arrays;
import java.util.Date;

public class Book {
    private String id;
    private String name;
    private String[] describs;
    private int pages;
    private double width;
    private float height;
    private Date printdate;
    private String rangedate;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Date getPrintdate() {
        return printdate;
    }

    public void setPrintdate(Date printdate) {
        this.printdate = printdate;
    }

    public String getRangedate() {
        return rangedate;
    }

    public void setRangedate(String rangedate) {
        this.rangedate = rangedate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", describs=" + Arrays.toString(describs) +
                ", pages=" + pages +
                '}';
    }
}
