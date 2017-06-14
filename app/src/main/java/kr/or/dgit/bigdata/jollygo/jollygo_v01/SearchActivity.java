package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
            TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
            tvTitle.setText("What a lot of chefs in the world");

            //updateUI(currentUser);
        } else if (id == R.id.nav_favlist) {
            //핸들러로 독립
            Message msg = new Message();
            msg.what =1;

            Handler mh = new mHandler();
            mh.sendMessageDelayed(msg,500);


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
                TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
                tvTitle.setText("즐겨찾기");
                removeMessages(msg.what);//
            }
        }
    }
}
