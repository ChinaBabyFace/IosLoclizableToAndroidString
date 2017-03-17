package com.example.sharkren.myapplication.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.sharkren.myapplication.model.LanguageHelper;
import com.example.sharkren.myapplication.model.StringItem;
import com.example.sharkren.myapplication.praser.AndroidStringXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renyuxiang on 2017/3/9.
 */

public class MainViewController {
    public static final String CUSTOM_STRING_CONFIG = "part_string.xml";
    public static final String XML_FILE_BASE_PATH = "AndroidStringXml/";
    private String TAG = this.getClass().getSimpleName();
    private Context context;
    private HashMap<String, LanguageHelper> languageMap;
    private OnLogReceivedListener onLogReceivedListener;

    public MainViewController(Context context) {
        this.context = context;
        languageMap = getLanguageList();
    }

    public void addNewStringItem() {
        new AsyncTask<HashMap<String, LanguageHelper>, String, Void>() {
            @Override
            protected Void doInBackground(HashMap<String, LanguageHelper>... params) {
                AndroidStringXmlParser xmlParser = new AndroidStringXmlParser();
                ArrayMap<String, StringItem> partAndroidKvMap = null;

                try {
                    partAndroidKvMap = xmlParser.getAndroidKvMap(context.getAssets().open(CUSTOM_STRING_CONFIG));
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (partAndroidKvMap == null) {
                    return null;
                }
                for (Map.Entry<String, LanguageHelper> entry : params[0].entrySet()) {
                    String startMessage = "[" + entry.getValue().getCode() + "] addNewStringItem start";
                    Log.e(TAG, startMessage);
                    publishProgress(startMessage);

                    entry.getValue().synForPartString(partAndroidKvMap, true);

                    String endMessage = "[" + entry.getValue().getCode() + "] addNewStringItem complete!";
                    Log.e(TAG, endMessage);
                    publishProgress(endMessage);

                    entry.getValue().createAndroidStringXml();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (onLogReceivedListener != null && values != null && values.length > 0) {
                    onLogReceivedListener.onLogReceived(values[0]);
                }
            }
        }.execute(languageMap);
    }

    public void synInsertStringItem() {
        new AsyncTask<HashMap<String, LanguageHelper>, String, Void>() {
            @Override
            protected Void doInBackground(HashMap<String, LanguageHelper>... params) {
                LanguageHelper enHelper = params[0].get("en");
                for (Map.Entry<String, LanguageHelper> entry : params[0].entrySet()) {
                    String startMessage = "[" + entry.getValue().getCode() + "] synInsertStringItem start";
                    Log.e(TAG, startMessage);
                    publishProgress(startMessage);

                    entry.getValue().synWithOtherHelper(enHelper, false);

                    String endMessage = "[" + entry.getValue().getCode() + "] synInsertStringItem complete!";
                    Log.e(TAG, endMessage);
                    publishProgress(endMessage);

                    entry.getValue().createAndroidStringXml();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (onLogReceivedListener != null && values != null && values.length > 0) {
                    onLogReceivedListener.onLogReceived(values[0]);
                }
            }
        }.execute(languageMap);
    }

    public void synUpdateOrInsertStringItem() {
        new AsyncTask<HashMap<String, LanguageHelper>, String, Void>() {
            @Override
            protected Void doInBackground(HashMap<String, LanguageHelper>... params) {
                LanguageHelper enHelper = params[0].get("en");
                for (Map.Entry<String, LanguageHelper> entry : params[0].entrySet()) {
                    String startMessage = "[" + entry.getValue().getCode() + "] synUpdateOrInsertStringItem start";
                    Log.e(TAG, startMessage);
                    publishProgress(startMessage);

                    entry.getValue().synWithOtherHelper(enHelper, true);

                    String endMessage = "[" + entry.getValue().getCode() + "] synUpdateOrInsertStringItem complete!";
                    Log.e(TAG, endMessage);
                    publishProgress(endMessage);

                    entry.getValue().createAndroidStringXml();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (onLogReceivedListener != null && values != null && values.length > 0) {
                    onLogReceivedListener.onLogReceived(values[0]);
                }
            }
        }.execute(languageMap);
    }


    public void checkAllIosFile() {
        new AsyncTask<HashMap<String, LanguageHelper>, String, Void>() {
            @Override
            protected Void doInBackground(HashMap<String, LanguageHelper>... params) {
                for (Map.Entry<String, LanguageHelper> entry : params[0].entrySet()) {
                    String startMessage = "Check [" + entry.getValue().getCode() + "] ios file start";
                    Log.e(TAG, startMessage);
                    publishProgress(startMessage);

                    entry.getValue().checkSelfIosFile();

                    String endMessage = "Check [" + entry.getValue().getCode() + "] ios file complete!";
                    Log.e(TAG, endMessage);
                    publishProgress(endMessage);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                if (onLogReceivedListener != null && values != null && values.length > 0) {
                    onLogReceivedListener.onLogReceived(values[0]);
                }
            }
        }.execute(languageMap);
    }

    public HashMap<String, LanguageHelper> getLanguageList() {
        String androidXmlSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                XML_FILE_BASE_PATH;
        String androidPath = "android/values";
        String androidFileName = "/strings.xml";
        String iosPath = "ios";
        String iosFileName = "/Localizable.strings";
        String lproj = ".lproj";

        HashMap<String, String> relationMap = new HashMap<>();
        //*英语美语*/
        relationMap.put("en", "en");
        //*阿拉伯*/
        relationMap.put("ar", "ar");
        //*捷克*/
        relationMap.put("cs", "cs");
        //*德国*/
        relationMap.put("de", "de");
        //*希腊*/
        relationMap.put("el", "el");
        //*西班牙*/
        relationMap.put("es", "es");
        //*伊朗*/
        relationMap.put("fa", "fa");
        //*法国*/
        relationMap.put("fr", "fr");
        //*匈牙利*/
        relationMap.put("hu", "hu");
        //*意大利*/
        relationMap.put("it", "it");
        //*日本*/
        relationMap.put("ja", "ja");
        //*荷兰*/
        relationMap.put("nl", "nl");
        //*葡萄牙*/
        relationMap.put("pt", "pt");
        //*罗马尼亚*/
        relationMap.put("ro", "ro");
        //*俄罗斯*/
        relationMap.put("ru", "ru");
        //*斯洛伐克*/
        relationMap.put("sk", "sk");
        //*中国*/
        relationMap.put("zh-rCN", "zh-Hans");
        //*中国香港*/
        relationMap.put("zh-rHK", "zh-Hant");
        //*中国台湾*/
        relationMap.put("zh-rTW", "zh-Hant");


        HashMap<String, LanguageHelper> languageMap = new HashMap<>();
        for (Map.Entry<String, String> codeEntry : relationMap.entrySet()) {
            LanguageHelper helper = new LanguageHelper(context);
            helper.setCode(codeEntry.getKey());

            if (codeEntry.getKey().equals("en")) {
                helper.setAndroidAssetPath(androidPath + androidFileName);
                helper.setIosAssetPath(iosPath + iosFileName);
            } else {
                helper.setAndroidAssetPath(androidPath + "-" + codeEntry.getKey() + androidFileName);
                helper.setIosAssetPath(iosPath + "/" + codeEntry.getValue() + lproj + iosFileName);
            }
            helper.setAndroidStringXmlSavePath(androidXmlSavePath + "/" + helper.getAndroidAssetPath());
            helper.initHelper();
            languageMap.put(helper.getCode(), helper);
        }


        return languageMap;
    }

    public OnLogReceivedListener getOnLogReceivedListener() {
        return onLogReceivedListener;
    }

    public void setOnLogReceivedListener(OnLogReceivedListener onLogReceivedListener) {
        this.onLogReceivedListener = onLogReceivedListener;
    }
}
