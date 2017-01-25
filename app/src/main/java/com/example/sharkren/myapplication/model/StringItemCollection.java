package com.example.sharkren.myapplication.model;

import java.util.List;

/**
 * Created by renyuxiang on 16/11/2.
 */

public class StringItemCollection {
    private List<String> iosKeyList;
    private StringItem androidItem;

    public List<String> getIosKeyList() {
        return iosKeyList;
    }

    public void setIosKeyList(List<String> iosKeyList) {
        this.iosKeyList = iosKeyList;
    }

    public StringItem getAndroidItem() {
        return androidItem;
    }

    public void setAndroidItem(StringItem androidItem) {
        this.androidItem = androidItem;
    }
}
