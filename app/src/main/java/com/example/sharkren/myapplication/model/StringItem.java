package com.example.sharkren.myapplication.model;

/**
 * Created by renyuxiang on 16/11/1.
 */

public class StringItem {
    private String name;
    private String content;
    private boolean isFormatted = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFormatted() {
        return isFormatted;
    }

    public void setFormatted(boolean formatted) {
        isFormatted = formatted;
    }
}
