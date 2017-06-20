package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Allword;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Uword;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private FloatingActionMenu fam;
    private FloatingActionButton fabPhoto, fabBack,fabHome,fabBrowser,fabFav;
    private String urlRes;
    private String imgurl;
    private String blogname;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int flcount; //해당 아이디 즐겨찾기 갯수 카운트
    private String url;
    private static final int PICTUREACT = 101;
    private static final int GALLERYOPEN = 102;
    private File pictureFile;
    public static final String AUTHORITY = "kr.or.dgit.bigdata.jollygo.jollygo_v01.fileprovider";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        Intent intent = new Intent(this.getIntent());
        url = intent.getStringExtra("url");
        imgurl = intent.getStringExtra("imgurl");
        blogname = intent.getStringExtra("blogname");
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

        boolean fav = intent.getBooleanExtra("byfav",false);//즐겨찾기에서 오면 즐겨찾기 버튼 숨기기
        if (fav){
            fabFav.setVisibility(View.GONE);
        }else{
            fabFav.setVisibility(View.VISIBLE);
        }
        //이미 존재하는 즐겨찾기인지 검증 // 쿼리로 변경
        Query fld = databaseReference.child("favlink").orderByChild("uid").equalTo(currentUser.getUid());
        fld.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Favlink> favlinks = new ArrayList<Favlink>();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        Favlink fl = new Favlink(Integer.parseInt(d.child("fno").getValue().toString()),
                                d.child("furl").getValue().toString(),
                                d.child("fimgurl").getValue().toString(),
                                d.child("uid").getValue().toString(),
                                d.child("fname").getValue().toString(),
                                Integer.parseInt(d.child("fcount").getValue().toString())
                        );
                        favlinks.add(fl);
                    }
                    for (Favlink f : favlinks) {
                        if (f.getFurl().equals(url)) {//이미 존재
                            fabFav.setImageResource(R.drawable.fabfav);
                            Toast.makeText(getApplicationContext(),"즐겨찾기에 이미 들어있어요.",Toast.LENGTH_SHORT).show();
                            fabFav.setClickable(false);
                            break;
                        } else {
                            fabFav.setImageResource(R.drawable.fabemptyfav);
                            fabFav.setClickable(true);
                        }
                     }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }//onCreate
    private View.OnClickListener onButtonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("플로팅버튼 확인",view.getId()+"");
                if (view == fabPhoto) {//사진 저장소 지정요망
                   /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivity(intent);*/
                    picClick();

                } else if (view == fabBrowser) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlRes));
                    startActivity(intent);

                } else if (view == fabBack) {//DB에 즐겨찾기 새로 추가 // 버튼 자체가 동작안함
                    finish();
                } else if (view == fabHome) { // 홈화면가기
                    Intent intent = new Intent(WebActivity.this,SearchActivity.class);
                    startActivity(intent);
                } else {//즐겨찾기 //이부분이 자꾸 먹통됨
                    Toast.makeText(getApplicationContext(),"즐겨찾기에 추가했습니다.",Toast.LENGTH_SHORT).show();
                    fabFav.setImageResource(R.drawable.fabfav);
                    DatabaseReference fld = databaseReference.child("favlink");//fno를 굳이 아이디별로 특정화 시킬 이유가 없음
                    fld.orderByChild("fno").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot d:dataSnapshot.getChildren()) {
                                Favlink fl = new Favlink(Integer.parseInt(d.child("fno").getValue().toString()),
                                        d.child("furl").getValue().toString(),
                                        d.child("fimgurl").getValue().toString(),
                                        d.child("uid").getValue().toString(),
                                        d.child("fname").getValue().toString(),
                                        Integer.parseInt(d.child("fcount").getValue().toString())
                                );
                                if (fl== null){
                                    flcount = 0;
                                }else{
                                    flcount = fl.getFno()+1;
                                }
                                Favlink favlinkAfter = new Favlink(flcount,urlRes,imgurl,
                                        currentUser.getUid(),blogname,0);
                                databaseReference.child("favlink").push().setValue(favlinkAfter);
                                fabFav.setClickable(false);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                fam.close(true);
            }
        };
    }
    public void picClick(){
        Intent intent;
        pictureFile = createImageFile();
        //가로찍기도 가능한 모드로 변경
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(WebActivity.this, AUTHORITY,pictureFile));
        startActivityForResult(intent, PICTUREACT);

    }

    private void sendBroadcastNotify() {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.parse("file://"+pictureFile.getAbsolutePath()));
        sendBroadcast(intent);

    }

    private Uri getCaptureBitmapUri() {
        Uri uri = null;
        try {
            pictureFile.createNewFile();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                uri = FileProvider.getUriForFile(WebActivity.this,AUTHORITY,pictureFile);
            }else {
                uri = Uri.fromFile(pictureFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private File createImageFile() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        String mPath = Environment.getExternalStorageDirectory().getAbsolutePath();//체크
        //+"/"+"wls"+sdf.format(date)+".jpg";
        File storageDir = new File(mPath+"/whatchefs");
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = null;
        try {
            image = File.createTempFile("wls"+sdf.format(date),".jpg",storageDir);
        }catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICTUREACT) {
                Log.e("결과코드>>>>>>>>>>>>>>>", "PICTUREACT");
                Uri uri = getCaptureBitmapUri();
                try {
                    Bitmap captureBmp = rotate(getExifOrientation(pictureFile.getAbsolutePath()), MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                    sendBroadcastNotify();
                    //찍은 사진 바로 올리기
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
                    builder.setMessage("사진을 바로 공유하시겠습니까?").setCancelable(false)
                            .setPositiveButton("좋아요",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getApplicationContext(),"TIP: 작업이 끝나시면 휴대폰의 뒤로가기 버튼을 눌러주세요.",Toast.LENGTH_LONG).show();
                                            //구버전 처리 넣기
                                            Uri uriPic;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                uriPic = FileProvider.getUriForFile(WebActivity.this,AUTHORITY,pictureFile);
                                            }else {
                                                uriPic = Uri.fromFile(pictureFile);
                                            }
                                            /*Intent i= new Intent(Intent.ACTION_PICK);*/
                                            Intent i= new Intent(Intent.ACTION_VIEW);
                                            //FileProvider.getUriForFile(WebActivity.this, AUTHORITY,pictureFile)
                                            i.setDataAndType(uriPic,"image/*");
                                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//
                                            startActivity(i);
                                        }
                                    }
                            ).setNegativeButton("싫어요",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }
                    );
                    builder.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public int getExifOrientation(String filepath){
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (exif != null){
            degree = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,-1);
        }
        return degree;
    }

    public Bitmap rotate(int degree, Bitmap bitmap){
        if(bitmap == null){
            return null;
        }
        Bitmap res = bitmap;
        if (degree > 0){
            Matrix matrix = getRotateMatrix(degree);
            res = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            if (bitmap != res){
                bitmap.recycle();
            }
        }
        return null;
    }
    private Matrix getRotateMatrix(int ori){
        Matrix matrix = new Matrix();
        switch (ori){
            case 0:
                matrix.setRotate(90);
                break;
            case 2:
                matrix.setScale(-1,1);
                break;
            case 3:
                matrix.setRotate(180);
                break;
            case 4:
                matrix.setRotate(180);
                matrix.postScale(-1,1);
                break;
            case 5:
                matrix.setRotate(90);
                matrix.postScale(-1,1);
                break;
            case 6:
                matrix.setRotate(90);
                break;
            case 7:
                matrix.setRotate(-90);
                matrix.postScale(-1,1);
                break;
            case 8:
                matrix.setRotate(-90);
                break;
        }
        return matrix;
    }
}
