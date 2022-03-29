package com.sdtechnocrat.webviewapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class ThirdPartyTrailerPlayer extends AppCompatActivity {

    WebView mContentWebView;
    String videoUrlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_trailer_player);

        videoUrlStr = "https://www.youtube.com/watch?v=f6-MCuDbGqc";
        videoUrlStr = "https://www.youtube.com/embed/f6-MCuDbGqc";

        if (videoUrlStr.matches("")){
            finish();
            overridePendingTransition(0, 0);
        }


        mContentWebView = findViewById(R.id.webView);
        mContentWebView.setFocusable(true);
        mContentWebView.setFocusableInTouchMode(true);

        mContentWebView.getSettings().setJavaScriptEnabled(true);


        //mContentWebView.setWebChromeClient(new WebChromeClient());
        /*mContentWebView.setInitialScale(1);
        mContentWebView.getSettings().setAllowFileAccess(true);
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mContentWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        mContentWebView.setWebViewClient(new WebViewClient());
        mContentWebView.getSettings().setJavaScriptEnabled(true);
        mContentWebView.getSettings().setLoadWithOverviewMode(true);
        mContentWebView.getSettings().setUseWideViewPort(true);*/
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;


        String data_html = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe style=\"background:black;\" width=' "+width+"' height='"+height+"' src=\""+videoUrlStr+"\" frameborder=\"0\"></iframe> </body> </html> ";
        String dataHtml = "<iframe width=\""+width+"\" height=\""+height+"\" src=\""+videoUrlStr+"\" frameborder=\"0\" allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
        //mContentWebView.loadDataWithBaseURL("https://youtube.com", data_html, "text/html", "UTF-8", null);
        mContentWebView.loadUrl(videoUrlStr);
        mContentWebView.setWebViewClient(new myWebClient());
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            autoplay(mContentWebView, true);

        }
    }

    public void autoplay(WebView webView, boolean bothUpAndDown){

        long delta = 100;
        long downTime = SystemClock.uptimeMillis();
        float x = webView.getLeft() + (webView.getWidth()/2);
        float y = webView.getTop() + (webView.getHeight()/2);

        MotionEvent tapDownEvent = MotionEvent.obtain(downTime, downTime + delta, MotionEvent.ACTION_DOWN, x, y, 0);
        tapDownEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);
        MotionEvent tapUpEvent = MotionEvent.obtain(downTime, downTime + delta + 2, MotionEvent.ACTION_UP, x, y, 0);
        tapUpEvent.setSource(InputDevice.SOURCE_CLASS_POINTER);

        if (bothUpAndDown)
            webView.dispatchTouchEvent(tapDownEvent);
        webView.dispatchTouchEvent(tapUpEvent);


    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_DOWN) {
            Log.d("Dispatch ", " Called");

            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
                autoplay(mContentWebView, false);
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_ESCAPE || event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                onBackPressed();
                return true;
            } else {
                return false;
            }

        } else {
            return super.dispatchKeyEvent(event);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //showFinishingDialog(this, "Warning!!!", "Are you sure to Exit from Player?");
    }

    public void showFinishingDialog(Context mContext, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
    }
}
