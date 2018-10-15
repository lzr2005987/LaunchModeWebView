package com.lzr.lunchmodewebapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2018/9/23 0023.
 */

public class LunchModeWebView extends WebView {
    private Mode mode = Mode.STANDARD;
    private WebBackForwardList webBackForwardList;

    public LunchModeWebView(Context context) {
        super(context);
        init(context);
    }

    public LunchModeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LunchModeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWebChromeClient(webChromeClient);
        setWebViewClient(webViewClient);

        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);//允许使用js

        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，只从网络获取数据.

        //支持屏幕缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);

        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {//页面加载完成
            webBackForwardList = copyBackForwardList();

            if (mode == Mode.SINGLE_TASK) {
                if (url.equals(view.getOriginalUrl()) && webBackForwardList.getCurrentIndex() != 0) {
                    int step = -webBackForwardList.getCurrentIndex();
                    view.goBackOrForward(step);
                    Log.d("lizheren", "shouldOverrideUrlLoading:aaaaa " + step);
                    Log.d("lizheren", "shouldOverrideUrlLoading:aaaaa " + view.canGoBackOrForward(step));
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webBackForwardList = copyBackForwardList();
            int size = webBackForwardList.getSize();
            for (int i = 0; i < size; i++) {
                Log.d("lizheren", "shouldOverrideUrlLoading: " + i + ":  " + webBackForwardList.getItemAtIndex(i).getUrl());
            }
            if (mode == Mode.SINGLE_TOP) {
                int currentIndex = webBackForwardList.getCurrentIndex();
                Log.d("lizheren", "current: " + currentIndex);
                if (currentIndex == 0 && url.equals(view.getOriginalUrl()))
                    return true;

                if (url.equals(webBackForwardList.getItemAtIndex(currentIndex).getUrl())) {
                    Log.d("lizheren", "shouldOverrideUrlLoading: ");
                    return true;
                }
            }

            if (mode == Mode.SINGLE_TASK) {
                if (url.equals(view.getOriginalUrl())) return true;
                int i;
                for (i = 0; i < size; i++) {
                    String historyUrl = webBackForwardList.getItemAtIndex(i).getUrl();
                    if (url.equals(historyUrl)) break;
                }
                Log.d("lizheren", "shouldOverrideUrlLoading: " + i);
                if (i != size) {
                    int step = i - webBackForwardList.getCurrentIndex();
                    Log.d("lizheren", "shouldOverrideUrlLoading:jhkjg " + step);
                    Log.d("lizheren", "shouldOverrideUrlLoading:jhkjg " + view.canGoBackOrForward(step));
                    view.goBackOrForward(step);
                    return false;
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient webChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }
    };

    public void setLunchMode(Mode mode) {
        this.mode = mode;
    }

    enum Mode {
        SINGLE_TOP, SINGLE_INSTANCE, SINGLE_TASK, STANDARD
    }

}
