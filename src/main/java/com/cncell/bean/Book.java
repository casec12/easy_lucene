package com.cncell.bean;

import java.util.Arrays;

public class Book {
    private String id;
    private String name;
    private String[] describs;
    private int pages;

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
