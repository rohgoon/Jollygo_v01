package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.firebasedto.Favlink;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments.FavlinkFragment;
import kr.or.dgit.bigdata.jollygo.jollygo_v01.fragments.SearchMainFragment;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static int searchCount;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public static final String APP_RESULT = "appresult";
    public static final String APP_FINISH = "finish";
    public static final String APP_SIGNOUT = "signout";
    private static final int SEARCHACTIVITY = 9002;
    private boolean checkBack = false;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private FrameLayout frameLayout;
    private FloatingActionButton floatingActionButton;
    private SearchView sv;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //->차후 프래그먼트로 이동
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();//유저정보
        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        SearchMainFragment smf = new SearchMainFragment();
        ft.replace(R.id.fragment,smf,"nav_home");
        ft.commit();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setVisibility(View.VISIBLE);
        ImageView tbtitle = (ImageView) findViewById(R.id.toolbarTitle);
        tbtitle.setVisibility(View.VISIBLE);
        tbtitle.setImageResource(R.drawable.toolbartitle);
        sv = (SearchView)findViewById(R.id.search_view);
    }



    @Override
    public void onBackPressed() {//앱 종료 관련 수정 요망
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (checkBack){
                Intent intent = new Intent();
                intent.putExtra(APP_RESULT,APP_FINISH);
                setResult(SEARCHACTIVITY,intent);
                finish();
                //super.onBackPressed();
            }else{
                checkBack = true;
                Toast.makeText(this, "뒤로가기를 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();


            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("메뉴 열기","onOptionsItemSelected");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //플래그먼트로 구현
        Log.e("메뉴 열기","onNavigationItemSelected");
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       /* mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.fragment);*/
        //작동 안됨
        if (id == R.id.nav_home) {
            // Handle the camera action
            Log.e("프래그먼트 교체","nav_home");
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            SearchMainFragment smf = new SearchMainFragment();
            ft.replace(R.id.fragment,smf,"nav_home");
            ft.commit();
            floatingActionButton.setVisibility(View.VISIBLE);
           /* TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvTitle.setText("What a lot of chefs in the world");*/
            sv.setVisibility(View.VISIBLE);
            sv.setIconified(true);

            ImageView tbtitle = (ImageView) findViewById(R.id.toolbarTitle);
            tbtitle.setVisibility(View.VISIBLE);
            tbtitle.setImageResource(R.drawable.toolbartitle);
            //updateUI(currentUser);
        } else if (id == R.id.nav_favlist) {
            //핸들러로 독립
            ImageView tbtitle = (ImageView) findViewById(R.id.toolbarTitle);
            tbtitle.setVisibility(View.VISIBLE);
            tbtitle.setImageResource(R.drawable.favtbtitle);
            Message msg = new Message();
            msg.what =1;

            Handler mh = new mHandler();
            mh.sendMessageDelayed(msg,500);
        } else if (id == R.id.nav_gallery) {
            Toast.makeText(getApplicationContext(),"TIP: 작업이 끝나시면 휴대폰의 뒤로가기 버튼을 눌러주세요.",Toast.LENGTH_LONG).show();
            String mPath = Environment.getExternalStorageDirectory().getAbsolutePath();//체크
            File storageDir = new File(mPath+"/whatchefs/");
            if (!storageDir.exists()){
                storageDir.mkdirs();
            }
            Uri uriPic;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                uriPic = FileProvider.getUriForFile(this,WebActivity.AUTHORITY,storageDir);
            }else {
                uriPic = Uri.fromFile(storageDir);
            }
            Intent i= new Intent(Intent.ACTION_VIEW,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//되긴하는데 열람만 가능
            //Intent i= new Intent(Intent.ACTION_VIEW);//되긴하는데 열람만 가능
            //FileProvider.getUriForFile(WebActivity.this, AUTHORITY,pictureFile)
            //i.setData(uriPic);
            //i.setDataAndType(uriPic, "*/*");
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//
            startActivity(i);
            //startActivity(Intent.createChooser(i, "갤러리 앱을 선택하세요"));
            /*if (i.resolveActivityInfo(getPackageManager(), 0) != null)
            {
                startActivity(i);
            }else {
                Toast.makeText(getApplicationContext(),"NULL",Toast.LENGTH_LONG).show();
            }*/
            //startActivity(i);


        } else if (id == R.id.nav_signout) {//signOut
            mAuth.signOut();
            Intent intent = new Intent();
            intent.putExtra(APP_RESULT,APP_SIGNOUT);
            setResult(SEARCHACTIVITY,intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(),user.getDisplayName(),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"유저가 없습니다.",Toast.LENGTH_LONG).show();
        }
    }
    private class mHandler extends Handler { // 리사이클링 뷰 갱신하기 전에 프로그래스바 보이기 위한 핸들러
        @Override
        public void handleMessage(Message msg) {
            if (msg.what ==1){
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                FavlinkFragment flf = new FavlinkFragment();
                ft.replace(R.id.fragment,flf,"nav_favlist");
                ft.commit();
                floatingActionButton.setVisibility(View.GONE);
                ImageView tbtitle = (ImageView) findViewById(R.id.toolbarTitle);
                tbtitle.setVisibility(View.VISIBLE);
                tbtitle.setImageResource(R.drawable.favtbtitle);/*
                TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
                tvTitle.setText("즐겨찾기");*/

                sv.setVisibility(View.GONE);
                removeMessages(msg.what);//
            }
        }
    }
}
