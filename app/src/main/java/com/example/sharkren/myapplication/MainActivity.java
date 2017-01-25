package com.example.sharkren.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.sharkren.myapplication.controller.StringConverter;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = this.getClass().getSimpleName();
    /*Android文件名为Key，对应的Ios文件名放到Set里*/
    private HashMap<String, String> relationMap;
    private StringConverter stringConverter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bind();
    }

    public void init() {
        findViewById(R.id.start_convert_button).setOnClickListener(this);
        findViewById(R.id.check_ios_file_button).setOnClickListener(this);
        initAndroidIosRelations();
        stringConverter = new StringConverter(this, relationMap);
    }

    public void bind() {

    }

    /**
     * 设定Android和Ios语言文件的关系
     */
    public void initAndroidIosRelations() {
        relationMap = new HashMap<>();
        String androidPath = "android/values";
        String androidFileName = "/strings.xml";

        String iosPath = "ios";
        String iosFileName = "/Localizable.strings";
        String lproj = ".lproj";
        /*英语美语*/
        relationMap.put(androidPath + androidFileName, iosPath + iosFileName);
        /*阿拉伯*/
        relationMap.put(androidPath + "-ar" + androidFileName, iosPath + "/ar" + lproj + iosFileName);
        /*捷克*/
        relationMap.put(androidPath + "-cs" + androidFileName, iosPath + "/cs" + lproj + iosFileName);
        /*德国*/
        relationMap.put(androidPath + "-de" + androidFileName, iosPath + "/de" + lproj + iosFileName);
        /*希腊*/
        relationMap.put(androidPath + "-el" + androidFileName, iosPath + "/el" + lproj + iosFileName);
        /*西班牙*/
        relationMap.put(androidPath + "-es" + androidFileName, iosPath + "/es" + lproj + iosFileName);
        /*伊朗*/
        relationMap.put(androidPath + "-fa" + androidFileName, iosPath + "/fa" + lproj + iosFileName);
        /*法国*/
        relationMap.put(androidPath + "-fr" + androidFileName, iosPath + "/fr" + lproj + iosFileName);
        /*匈牙利*/
        relationMap.put(androidPath + "-hu" + androidFileName, iosPath + "/hu" + lproj + iosFileName);
        /*意大利*/
        relationMap.put(androidPath + "-it" + androidFileName, iosPath + "/it" + lproj + iosFileName);
        /*日本*/
        relationMap.put(androidPath + "-ja" + androidFileName, iosPath + "/ja" + lproj + iosFileName);
        /*荷兰*/
        relationMap.put(androidPath + "-nl" + androidFileName, iosPath + "/nl" + lproj + iosFileName);
        /*葡萄牙*/
        relationMap.put(androidPath + "-pt" + androidFileName, iosPath + "/pt" + lproj + iosFileName);
        /*罗马尼亚*/
        relationMap.put(androidPath + "-ro" + androidFileName, iosPath + "/ro" + lproj + iosFileName);
        /*俄罗斯*/
        relationMap.put(androidPath + "-ru" + androidFileName, iosPath + "/ru" + lproj + iosFileName);
        /*斯洛伐克*/
        relationMap.put(androidPath + "-sk" + androidFileName, iosPath + "/sk" + lproj + iosFileName);
        /*中国*/
        relationMap.put(androidPath + "-zh-rCN" + androidFileName, iosPath + "/zh-Hans" + lproj + iosFileName);
        /*中国香港*/
        relationMap.put(androidPath + "-zh-rHK" + androidFileName, iosPath + "/zh-Hant" + lproj + iosFileName);
        /*中国台湾*/
        relationMap.put(androidPath + "-zh-rTW" + androidFileName, iosPath + "/zh-Hant" + lproj + iosFileName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_convert_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stringConverter.convertAll();
//                        shareEmail(
//                                "reyuxiang@ihealthlabs.com.cn",
//                                "生成的Android语言文件",
//                                new File(stringConverter.getBaseSavePath()));
                    }
                }).start();

                break;
            case R.id.check_ios_file_button:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        stringConverter.checkAllIosFile();
                    }
                }).start();
                break;
        }
    }

    public void shareEmail(String email, String shareTxt, File file) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTxt);
        shareIntent.putExtra(Intent.EXTRA_EMAIL, email);
        startActivity(shareIntent);
    }

}
