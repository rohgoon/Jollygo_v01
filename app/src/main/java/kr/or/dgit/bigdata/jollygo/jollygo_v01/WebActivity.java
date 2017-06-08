package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;

import android.provider.MediaStore;
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

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private FloatingActionMenu fam;
    private FloatingActionButton fabPhoto, fabBack,fabHome,fabBrowser,fabFav;
    private String urlRes;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Intent intent = new Intent(this.getIntent());
        String url = intent.getStringExtra("url");
        progressBar = (ProgressBar) findViewById(R.id.webPb);
        fabPhoto = (FloatingActionButton) findViewById(R.id.fabPhoto);
        fabBack = (FloatingActionButton) findViewById(R.id.fabBack);
        fabHome = (FloatingActionButton) findViewById(R.id.fabHome);
        fabBrowser = (FloatingActionButton) findViewById(R.id.fabBrowser);
        fabFav = (FloatingActionButton) findViewById(R.id.fabFav);
        fam = (FloatingActionMenu) findViewById(R.id.fab_menu);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();//유저정보

        //fam.setMenuButtonColorNormal(Color.BLUE);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Toast.makeText(WebActivity.this,"TIP: 우측 하단 버튼 속의 [브라우저로]를 실행하시면 자세히 둘러보실 수 있어요",Toast.LENGTH_SHORT).show();
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
        urlRes =url;
        //플로팅버튼 관련 //fabPhoto, fabBack,fabHome,fabBrowser,fabFav;
        fabPhoto.setOnClickListener(onButtonClick());
        fabBrowser.setOnClickListener(onButtonClick());
        fabBack.setOnClickListener(onButtonClick());
        fabHome.setOnClickListener(onButtonClick());
        fabFav.setOnClickListener(onButtonClick());
        fam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fam.isOpened()) {
                    fam.close(true);
                }
            }
        });
    }//onCreate
    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == fabPhoto) {//저장소 지정요망
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);

                } else if (view == fabBrowser) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlRes));
                    startActivity(intent);

                } else if (view == fabFav) {
                    Favlink favlink = new Favlink(SearchActivity.flcount,urlRes,"이미지",
                            currentUser.getUid(),0);
                    databaseReference.child("favlink").push().setValue(favlink);
                    
                } else if (view == fabHome) { // 홈화면가기
                    Intent intent = new Intent(WebActivity.this,SearchActivity.class);
                    startActivity(intent);
                } else {//뒤로가기
                    finish();
                }
                fam.close(true);
            }
        };
    }

}
