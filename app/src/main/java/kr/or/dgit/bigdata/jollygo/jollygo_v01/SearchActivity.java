package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private List<Favlink> favlinkList;
    public static int flcount; //해당 아이디 즐겨찾기 갯수 카운트

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

        favlinkList = new ArrayList<>();
        getFavlinkOnce(currentUser);
    }

    private void getFavlinkOnce(FirebaseUser currentUser) {//동작은 잘됨, 마지막 fno를 가져오는 걸로 변경 할것
        DatabaseReference flRef = databaseReference.child("favlink").equalTo(currentUser.getUid(),"uid").getRef();

        flRef.addListenerForSingleValueEvent(new ValueEventListener() {//처음 한번 리스트 불러오기 체크 요망
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               flcount= (int) dataSnapshot.getChildrenCount();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    favlinkList.add(d.getValue(Favlink.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) { //플래그먼트로 구현
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            updateUI(currentUser);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {//signOut
            mAuth.signOut();
            Toast.makeText(getApplicationContext(),"유저가 signOut.",Toast.LENGTH_LONG).show();
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

}
