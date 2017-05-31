package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Intent intent = new Intent(this.getIntent());
        String uriStr = intent.getStringExtra("uri");
        //Uri uri = Uri.parse(uriStr);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webSettings = webView.getSettings();
        //webView.setInitialScale(1);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUserAgentString("Android");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        Log.e("웹뷰 주소>>>>>>",uriStr);
        webView.loadUrl(uriStr);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        });

        //로딩에 많은 시간이 걸려 프로그래스바 설치 필요
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWeb);
        fab.setOnClickListener(new View.OnClickListener() {//사진찍기, 뒤로가기(리스트로), 홈으로, 인스타그램에 바로 공유, 브라우저로 열기
            @Override
            public void onClick(View view) {

            }
        });
    }

}
