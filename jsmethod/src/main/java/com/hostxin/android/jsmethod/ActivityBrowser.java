package com.hostxin.android.jsmethod;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ActivityBrowser extends BaseActivityBrowser{
    private MeWebView webView;
    private ProgressBar progress_horizontal;

    private String title = "";
    protected String url = "";

    private HandlerThread mHandlerThread;
    private Handler mOtherThreadHandler;

    /**
     * @param context
     * @param title
     * @param url
     */
    public static void openWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, ActivityBrowser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            title = bundle.getString("title");
            url = bundle.getString("url");
        } else {
            finish();
        }

        setContentView(R.layout.activity_browser);

        mHandlerThread = new HandlerThread("ActivityBrowser");
        mHandlerThread.start();
        mOtherThreadHandler = new Handler(mHandlerThread.getLooper());
        webView = (MeWebView) findViewById(R.id.browser_web);

        progress_horizontal = (ProgressBar) findViewById(R.id.progress_horizontal);


        webView.init(this);
        webView.setWebViewClientListener(webViewClientListener);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();

        runOnOtherThread(new Runnable() {

            @Override
            public void run() {
                runOnUiThread(loadRunnable);
            }
        });
    }

    private void runOnOtherThread(Runnable runnable) {
        mOtherThreadHandler.post(runnable);
    }

    private Runnable loadRunnable = new Runnable() {

        @Override
        public void run() {
            if (!isFinishing()) {
                webView.loadUrl(url);
            }
        }
    };

    private void autoLoad() {
        progress_horizontal.setVisibility(View.VISIBLE);
    }

    private void loadComplete() {
        progress_horizontal.setVisibility(View.GONE);
        webView.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView != null
                && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(final int requestCode,
                                    final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.handleActivityResult(requestCode, resultCode, data);
            }
        });

    }

    MeWebView.WebViewClientListener webViewClientListener = new MeWebView.WebViewClientListener() {

        public void onReceivedTitle(WebView view, String t) {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progress_horizontal.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            autoLoad();
        }

        public void onPageFinished(WebView view, String url) {
            loadComplete();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    };

    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.clearHistory();
            webView.clearCache(true);
            webView.destroyDrawingCache();
            webView.destroy();
        }
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
            mOtherThreadHandler = null;
        }
        super.onDestroy();
    }

}
