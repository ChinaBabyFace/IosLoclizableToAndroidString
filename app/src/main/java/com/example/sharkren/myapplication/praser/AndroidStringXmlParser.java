package com.example.sharkren.myapplication.praser;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.sharkren.myapplication.model.StringItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by renyuxiang on 16/11/1.
 */

public class AndroidStringXmlParser {
    private String TAG = this.getClass().getSimpleName();

    public List<StringItem> readXML(InputStream inputstream) throws XmlPullParserException, IOException {
        long time = System.currentTimeMillis();
        List<StringItem> stringList = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputstream, "UTF-8");
        int eventCode = parser.getEventType();
        int count = 1;
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:
                    stringList = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    if ("string".equals(parser.getName())) {
                        StringItem item = new StringItem();
                        item.setName(parser.getAttributeValue(0));
                        item.setContent(parser.nextText());
                        stringList.add(item);
                        Log.e("Dom", "String name:" + item.getName());
                        Log.e("Dom", "String content:" + item.getContent());
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.END_DOCUMENT:
                    Log.e(TAG, "String item total:" + count);
                    Log.e(TAG, "Parse cost time:" + (System.currentTimeMillis() - time));
                    Log.e(TAG, "Parse finish");
                    break;
            }
            count += 1;
            eventCode = parser.next();
        }

        return stringList;
    }

    public ArrayMap<String, StringItem> getAndroidKvMap(InputStream inputstream) throws XmlPullParserException,
            IOException {
        long time = System.currentTimeMillis();
        ArrayMap<String, StringItem> stringMap = new ArrayMap<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputstream, "UTF-8");
        int eventCode = parser.getEventType();
        int count = 1;
        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (eventCode) {
                case XmlPullParser.START_DOCUMENT:
                    Log.e(TAG, "Android XML Parse Start");
                    break;
                case XmlPullParser.START_TAG:
                    if ("string".equals(parser.getName())) {
                        StringItem item = new StringItem();
                        item.setName(parser.getAttributeValue(null, "name"));
                        String formattedString = parser.getAttributeValue(null, "formatted");
                        if (!TextUtils.isEmpty(formattedString)) {
                            item.setFormatted(Boolean.parseBoolean(formattedString));
                        }
                        item.setContent(parser.nextText());
                        stringMap.put(item.getName(), item);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
                case XmlPullParser.END_DOCUMENT:
                    Log.e(TAG, "String item total:" + count);
                    Log.e(TAG, "Parse cost time:" + (System.currentTimeMillis() - time));
                    Log.e(TAG, "Android XML Parse finish");
                    break;
            }
            count += 1;
            eventCode = parser.next();
        }

        return stringMap;
    }
}
