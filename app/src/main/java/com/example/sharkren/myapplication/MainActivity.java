package com.example.sharkren.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.sharkren.myapplication.controller.MainViewController;
import com.example.sharkren.myapplication.controller.OnLogReceivedListener;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String TAG = this.getClass().getSimpleName();
    private MainViewController controller;
    private TextView logcatTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bind();
    }

    public void init() {
        findViewById(R.id.and_new_item).setOnClickListener(this);
        findViewById(R.id.all_insert).setOnClickListener(this);
        findViewById(R.id.all_update_and_insert).setOnClickListener(this);
        findViewById(R.id.check_ios_file_button).setOnClickListener(this);
        logcatTextView = (TextView) findViewById(R.id.logcat);
        controller = new MainViewController(this);
    }

    public void bind() {
        controller.setOnLogReceivedListener(new OnLogReceivedListener() {
            @Override
            public void onLogReceived(String logMessage) {
                logcatTextView.append("\n" + logMessage);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.and_new_item:
                logcatTextView.setText("");
                controller.addNewStringItem();
                break;
            case R.id.all_insert:
                logcatTextView.setText("");
                controller.synInsertStringItem();
                break;
            case R.id.all_update_and_insert:
                logcatTextView.setText("");
                controller.synUpdateOrInsertStringItem();
                break;
            case R.id.check_ios_file_button:
                logcatTextView.setText("");
                controller.checkAllIosFile();
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
