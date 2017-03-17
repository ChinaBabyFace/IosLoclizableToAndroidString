package com.example.sharkren.myapplication.praser;

import android.text.TextUtils;
import android.util.Log;

import com.example.sharkren.myapplication.model.StringItem;

import java.io.File;
import java.io.FileWriter;

/**
 * Created by renyuxiang on 16/11/1.
 */

public class AndroidStringXmlWriter {
    private String TAG = this.getClass().getSimpleName();
    private StringBuilder fileContent;
    private String fileSavePath;
    private int total;

    public AndroidStringXmlWriter(String stringXmlSavePath) {
        fileSavePath = stringXmlSavePath;
        fileContent = new StringBuilder();
    }

    public void appendStringItem(StringItem item) {
        String formatted = "";
        if (!item.isFormatted()) {
            formatted = " formatted=\"false\"";
        }
        String itemString = "<string name=\"" + item.getName() + "\" " + formatted + ">" + item.getContent() +
                "</string>\n";
        //        Log.e(TAG, getCountryCode()+"_XML:" + item);
        fileContent.append(itemString);
        setTotal(getTotal() + 1);
    }

    public void clear() {
        fileSavePath = "";
        fileContent = new StringBuilder();

    }

    public void startXml() {
        fileContent.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        fileContent.append("<resources>\n");
        //        Log.e(TAG, getCountryCode()+"_XML:" + getFileContent().toString());
    }

    public void endXml() {
        fileContent.append("</resources>");
        //        Log.e(TAG, getCountryCode()+"_XML:</resources>");
        createXml();

    }

    private void createXml() {
        try {
            Log.e(TAG, "Start create string xml...");
            long time = System.currentTimeMillis();
            if (TextUtils.isEmpty(getFileSavePath())) {
                Log.e(TAG, "Fail create string xml,path is null!");
                return;
            }

            File xmlFile = new File(getFileSavePath());

            File xmlDir = new File(xmlFile.getParentFile().getAbsolutePath());
            xmlDir.mkdirs();

            Log.e(TAG, "String xml path:" + xmlFile.getAbsolutePath());
            if (!xmlFile.exists()) {
                xmlFile.createNewFile();
            }

            FileWriter fw = new FileWriter(xmlFile.getAbsolutePath(), false);
            fw.flush();
            fw.write(fileContent.toString());
            fw.close();

            //            Log.e(TAG, "String xml path:" + xmlFile.getAbsolutePath());
            Log.e(TAG, "String item total:" + getTotal());
            Log.e(TAG, "Cost time:" + (System.currentTimeMillis() - time));
            Log.e(TAG, "Finish create string  xml!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
