package com.lzr.lunchmodewebapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private LunchModeWebView webView;
    private String[] url = {"http://www.baidu.com", "http://www.youdao.com/", "https://www.so.com/"};
    private int index = 0;
    private Button bt360;
    private Button btBaidu;
    private Button btYoudao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (LunchModeWebView) findViewById(R.id.wv_main);
        bt360 = (Button) findViewById(R.id.bt_360);
        btBaidu = (Button) findViewById(R.id.bt_baidu);
        btYoudao = (Button) findViewById(R.id.bt_youdao);
        bt360.setOnClickListener(this);
        btBaidu.setOnClickListener(this);
        btYoudao.setOnClickListener(this);

        /**
         * 在这里设置launchMode，目前支持singleTop、singleTask和standard，singleInstance对webView意义不大
         */
        webView.setLunchMode(LunchModeWebView.Mode.SINGLE_TOP);
        webView.loadUrl(url[0]);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_360:
                webView.loadUrl(url[2]);
                break;
            case R.id.bt_youdao:
                webView.loadUrl(url[1]);
                break;
            case R.id.bt_baidu:
                webView.loadUrl(url[0]);
                break;
        }
    }
}
