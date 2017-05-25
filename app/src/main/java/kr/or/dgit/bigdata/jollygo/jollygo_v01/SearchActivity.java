package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.imgmanage.ImgMaching;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView sv;
    TextView tvTitle;
    static int searchCount;
    RecyclerView rv;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;
    FloatingActionButton fab;
    private List<String> mDataset = new ArrayList<>(); //재료 검색어 리스트
    //private List<String> mImgset; //이미지 url 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //final ImgMaching imgMaching = new ImgMaching();


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recyclerView
        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);
        rvLayoutManager = new GridLayoutManager(this,3);
        rv.setLayoutManager(rvLayoutManager);
        //RecyclerView animation

        sv = (SearchView) findViewById(R.id.search_view);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        sv.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitle.setText("");
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchCount++;
                mDataset.add(query);
                /*//send ImgMaching
                imgMaching.setmDataset(mDataset);
                //matching process
                imgMaching.getWikiRes();
                //matching image feedback
                mImgset = imgMaching.getmImgset();*/

                sv.setQuery("",true);
                rvAdapter = new RvAdapter(mDataset,getApplicationContext(),fab); // List<String> 입력
                rv.setAdapter(rvAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchCount = rv.getAdapter().getItemCount();
                if (searchCount>1) {
                    tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                }else if (searchCount == 1){
                    tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                }else if (searchCount == 0 || mDataset == null){
                    tvTitle.setText("JOLLYGO-Search your own recipe");
                }
                return false;
            }
        });
        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

                searchCount = rv.getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                   return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                    }else if (searchCount == 0 || mDataset == null){
                        tvTitle.setText("JOLLYGO-Search your own recipe");
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                searchCount = rv.getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                    return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                    }else if (searchCount == 0 || mDataset == null){
                        tvTitle.setText("JOLLYGO-Search your own recipe");
                    }
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
