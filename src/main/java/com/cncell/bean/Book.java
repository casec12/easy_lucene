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
