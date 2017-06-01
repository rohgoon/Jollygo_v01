package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Intent intent = new Intent(this.getIntent());
        String url = intent.getStringExtra("url");
        progressBar = (ProgressBar) findViewById(R.id.webPb);
        //progressBar.setMax(100);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(WebActivity.this,"TIP: 우측 하단 버튼의 브라우저로 이동을 실행하면 둘러보실 수 있어요",Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
       /* webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {//
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }

            }
        });*/
        webSettings = webView.getSettings();
        //webView.setInitialScale(1);
        webSettings.setJavaScriptEnabled(true);
        //String uas ="Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
        String uas ="Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"; //데스크탑으로 나오더라도 가장 동작 잘됨
        //String uas ="Mozilla/5.0 (Linux; Android 4.0.4; Galaxy Nexus Build/IMM76B) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.133 Mobile Safari/535.19";//제공 https://developer.chrome.com/multidevice/user-agent
        //Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Mobile Safari/537.36
        webSettings.setUserAgentString(uas);
        //webSettings.setUserAgentString("Android");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        String[] mobiUrlArr = url.split("/");
        String bnc = mobiUrlArr[2];
        String[] countMur = bnc.split("\\.");
        Log.e("주소변경>>>>>>",countMur[2]);
        if (mobiUrlArr[2].equals("blog.naver.com")){
            mobiUrlArr[2] = "m."+mobiUrlArr[2];
            String newUrl = "";
            for (String s:mobiUrlArr) {
                newUrl += s;
                newUrl += "/";
            }
            url = newUrl;
        }else if (countMur[0].equals("m")){

        }else if (!countMur[0].equals("m") && countMur[2].equals("naver")){
            Log.e("주소변경2>>>>>>",countMur[0]);
            mobiUrlArr[2] = "m.blog.naver.com";
            String newUrl = "";
            for (String s:mobiUrlArr) {
                newUrl += s;
                newUrl += "/";
            }
            url = newUrl;
        }
        Log.e("웹뷰 주소>>>>>>",url);

        webView.loadUrl(url);


        //로딩에 많은 시간이 걸려 프로그래스바 설치 필요
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWeb);
        fab.setOnClickListener(new View.OnClickListener() {//사진찍기, 뒤로가기(리스트로), 홈으로, /*인스타그램에 바로 공유*/, 브라우저로 열기
            @Override
            public void onClick(View view) {

            }
        });
    }

}
