package com.example.sharkren.myapplication.controller;

import android.content.Context;
import android.os.Environment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.sharkren.myapplication.model.IosStringNullException;
import com.example.sharkren.myapplication.praser.AndroidStringXmlParser;
import com.example.sharkren.myapplication.praser.AndroidStringXmlWriter;
import com.example.sharkren.myapplication.praser.IosStringFileParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by renyuxiang on 2017/1/19.
 */

public class StringConverter {
    private String TAG = this.getClass().getSimpleName();
    /**
     * 最终生成的文件在手机的存储位置
     */
    private String baseSavePath;
    private Context context;
    /**
     * 指定存储在Asset的ios与Android语言文件的映射关系
     */
    private HashMap<String, String> stringFilePairMap;

    public StringConverter(Context context, HashMap<String, String> filePairMap) {
        this.context = context;
        this.stringFilePairMap = filePairMap;
        this.baseSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "AndroidStringXml/";
    }

    /**
     * 遍历两次
     * 第一次呢找到所有Android和ios共同项的集合
     * 拿这个集合去第二次遍历
     */
    public void convertAll() {
                /*第一次遍历*/
        ArrayMap<String, HashSet<String>> keyMap = new ArrayMap<>();
        Iterator it = stringFilePairMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String androidAssetPath = (String) entry.getKey();
            String iosAssetPath = (String) entry.getValue();

                    /*匹配同一个国家语言的共同项*/
            ArrayMap<String, HashSet<String>> temp = null;
            try {
                temp = getIosAndroidPublicItemList(
                        context.getAssets().open(androidAssetPath)
                        , context.getAssets().open(iosAssetPath));
            } catch (IOException e) {
                e.printStackTrace();
            }

                    /*将拿到的单个国家的共同项和总Map中的项目进行合并，已有的合并，没有的直接追加到总Map中*/
            for (Map.Entry<String, HashSet<String>> tempItem : temp.entrySet()) {
                HashSet<String> tempSet = keyMap.get(tempItem.getKey());
                if (tempSet == null) {
                    tempSet = new HashSet<>();
                }
                tempSet.addAll(tempItem.getValue());
                keyMap.put(tempItem.getKey(), tempSet);
            }
        }

                /*第二次遍历*/
        it = stringFilePairMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String androidAssetPath = (String) entry.getKey();
            String iosAssetPath = (String) entry.getValue();
            try {
                synAndroidAndIosLanguageItem(baseSavePath + androidAssetPath
                        , context.getAssets().open(androidAssetPath)
                        , context.getAssets().open(iosAssetPath)
                        , keyMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 返回的是KV结构是：
     * K Android 语言条目的name
     * V 所有与Anroid语言条目name对应的内容相同的IOS条目的Name集合
     * <Android key,<Ios key,IosKey....>>
     *
     * @param androidAssetStream
     * @param iosAssetStream
     * @return
     */
    public ArrayMap<String, HashSet<String>> getIosAndroidPublicItemList(InputStream androidAssetStream,
                                                                         InputStream iosAssetStream) {
        ArrayMap<String, HashSet<String>> keyMap = new ArrayMap();
        if (androidAssetStream == null || iosAssetStream == null) {
            return keyMap;
        }
        try {
            AndroidStringXmlParser xmlParser = new AndroidStringXmlParser();
            ArrayMap<String, String> androidDeMap = xmlParser.readXmlToMap(androidAssetStream);

            IosStringFileParser iosParser = new IosStringFileParser();
            ArrayMap<String, HashSet<String>> iosDeMap = iosParser.parseIosStringFileToReverseMap(iosAssetStream);

            Log.e(TAG, "Load android item count:" + androidDeMap.size()
                    + "\n"
                    + "Load ios item count:" + iosDeMap.size());

            for (Map.Entry<String, String> androidItem : androidDeMap.entrySet()) {
                /*直接ios的V与Android的V进行比较*/
                HashSet<String> iosSet = iosDeMap.get(androidItem.getValue().replace(" ", "").toLowerCase());
                if (iosSet != null) {
                    HashSet set = keyMap.get(androidItem.getKey());
                    if (set == null) {
                        set = new HashSet();
                    }
                    set.addAll(iosSet);
                    keyMap.put(androidItem.getKey(), set);
                }
            }
            Log.e(TAG, "Find public item count:" + keyMap.size());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyMap;
    }


    /**
     * 同步Android和ios的语言条目
     * 将Android已有的和缺失的但是ios有的，全部写入Android
     * 从而实现对Android语言条目的补全
     * 因为假设前提是ios的语言条目比Android全，所以Android有可能出现没有某个国家的问题
     *
     * @param androidStringXmlSavePath
     * @param androidAssetStream
     * @param iosAssetStream
     * @param keyCollectMap
     */

    public void synAndroidAndIosLanguageItem(String androidStringXmlSavePath,
                                             InputStream androidAssetStream,
                                             InputStream iosAssetStream,
                                             ArrayMap<String, HashSet<String>> keyCollectMap) {
        try {
            int androidItemCount = 0;
            AndroidStringXmlWriter xmlWriter = new AndroidStringXmlWriter(androidStringXmlSavePath);
            xmlWriter.startXml();

            IosStringFileParser iosParser = new IosStringFileParser();
            ArrayMap<String, String> iosStringMap = iosParser.parseIosStringFileToNormalMap(
                    iosAssetStream);


            ArrayMap<String, String> androidDeMap = null;
            if (androidAssetStream != null) {
                AndroidStringXmlParser xmlParser = new AndroidStringXmlParser();
                 /*将Android已有的条目无需判断直接加入Android Map中*/
                androidDeMap = xmlParser.readXmlToMap(androidAssetStream);
                androidItemCount = androidDeMap != null ? androidDeMap.size() : 0;
            }

            Log.e(TAG, "Public item  count:" + keyCollectMap.size());
            Log.e(TAG, "Android old edition has:" + androidItemCount + " items");

            /*将ios有的但是Android缺的国际化语言化条目加入Android Map中*/
            for (Map.Entry<String, HashSet<String>> keyItem : keyCollectMap.entrySet()) {
                if (androidDeMap == null || androidDeMap.get(keyItem.getKey()) == null) {
                    HashSet<String> keySet = keyItem.getValue();
                    for (String iosKey : keySet) {
                        if (iosStringMap.get(iosKey) != null) {
                            androidDeMap.put(keyItem.getKey(), iosStringMap.get(iosKey));
                            androidItemCount++;
                            break;
                        }
                    }
                }
            }

            for (Map.Entry<String, String> item : androidDeMap.entrySet()) {
                xmlWriter.appendStringItem(item.getKey(), item.getValue());
            }

            xmlWriter.endXml();
            Log.e(TAG, "Android new edition has:" + androidItemCount + " items");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    public void checkAllIosFile() {
        Iterator it = stringFilePairMap.entrySet().iterator();
        while (it.hasNext()) {

            Map.Entry entrySet = (Map.Entry) it.next();
            String path = (String) entrySet.getValue();
            try {
                Log.e(TAG, "Check Ios File:" + path);
                checkIosFile(path);
                Log.e(TAG, "Check Ios File Complete:" + path);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Check Ios File Fail:" + path);
                break;
            } catch (IosStringNullException e) {
                e.printStackTrace();
                Log.e(TAG, "Check Ios File Fail:" + path);
                break;
            }
        }
    }


    public void checkIosFile(String iosFileAssetPath) throws IOException, IosStringNullException {
        IosStringFileParser parser = new IosStringFileParser();
        parser.checkFile(context.getAssets().open(iosFileAssetPath));
    }

    public String getBaseSavePath() {
        return baseSavePath;
    }
}
