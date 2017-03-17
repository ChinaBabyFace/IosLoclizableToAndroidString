package com.example.sharkren.myapplication.model;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.sharkren.myapplication.praser.AndroidStringXmlParser;
import com.example.sharkren.myapplication.praser.AndroidStringXmlWriter;
import com.example.sharkren.myapplication.praser.IosStringFileParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * Created by renyuxiang on 2017/3/9.
 */

public class LanguageHelper {
    private String TAG = this.getClass().getSimpleName();
    private Context context;
    private String code;
    private String iosAssetPath;
    private String androidAssetPath;
    private String androidStringXmlSavePath;
    private ArrayMap<String, StringItem> androidKvMap;
    private ArrayMap<String, String> iosKvMap;

    public LanguageHelper(Context context) {
        this.context = context;
    }

    public void initHelper() {
        TAG += " " + androidAssetPath.substring(androidAssetPath.lastIndexOf('/'));
        AndroidStringXmlParser xmlParser = new AndroidStringXmlParser();
        IosStringFileParser iosParser = new IosStringFileParser();

        try {
            androidKvMap = xmlParser.getAndroidKvMap(context.getAssets().open(androidAssetPath));
            iosKvMap = iosParser.getIosKvMap(context.getAssets().open(iosAssetPath));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isAndroidStringItemExist(String androidStringKey) {
        if (androidKvMap.get(androidStringKey) != null) {
            return true;
        }
        return false;
    }

    public boolean isIosStringItemExist(String iosStringKey) {
        if (iosKvMap.get(iosStringKey) != null) {
            return true;
        }
        return false;
    }

    /**
     * 添加一个Android 语言项
     *
     * @param androidStringKey
     * @param iosStringKey
     * @param isOverwrite      是否允许覆盖添加
     */
    public void addAndroidItem(String androidStringKey, String iosStringKey, boolean isOverwrite) {
        if (androidStringKey.equals("ignore_the_device")) {
            Log.e(TAG, "ignore_the_device:" + iosStringKey);
        }
        if (isAndroidStringItemExist(androidStringKey) && !isOverwrite) {
            return;
        }
        if (!isIosStringItemExist(iosStringKey)) {
            return;
        }
        StringItem item = new StringItem();
        item.setName(androidStringKey);
        item.setContent(iosKvMap.get(iosStringKey));
        androidKvMap.put(androidStringKey, item);
    }

    public void synWithOtherHelper(LanguageHelper otherHelper, boolean isOverwrite) {
        for (Map.Entry<String, StringItem> otherItem : otherHelper.getAndroidKvMap().entrySet()) {
            addAndroidItem(otherItem.getKey(), otherItem.getValue().getContent(), isOverwrite);
        }
    }

    public void synForPartString(ArrayMap<String, StringItem> partAndroidKvMap, boolean isOverwrite) {
        for (Map.Entry<String, StringItem> otherItem : partAndroidKvMap.entrySet()) {
            addAndroidItem(otherItem.getKey(), otherItem.getValue().getContent(), isOverwrite);
        }
    }

    public void createAndroidStringXml() {
        int androidItemCount = 0;
        AndroidStringXmlWriter xmlWriter = new AndroidStringXmlWriter(androidStringXmlSavePath);
        xmlWriter.startXml();

        for (Map.Entry<String, StringItem> item : androidKvMap.entrySet()) {
            androidItemCount += 1;
            xmlWriter.appendStringItem(item.getValue());
        }

        xmlWriter.endXml();
        Log.e(TAG, "Android new edition has:" + androidItemCount + " items");
    }

    public boolean checkSelfIosFile() {
        IosStringFileParser parser = new IosStringFileParser();
        try {
            parser.checkFile(context.getAssets().open(iosAssetPath));
            return true;
        } catch (IosStringNullException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setIosAssetPath(String iosAssetPath) {
        this.iosAssetPath = iosAssetPath;
    }

    public String getAndroidAssetPath() {
        return androidAssetPath;
    }

    public void setAndroidAssetPath(String androidAssetPath) {
        this.androidAssetPath = androidAssetPath;
    }

    public void setAndroidStringXmlSavePath(String androidStringXmlSavePath) {
        this.androidStringXmlSavePath = androidStringXmlSavePath;
    }

    public ArrayMap<String, StringItem> getAndroidKvMap() {
        return androidKvMap;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
