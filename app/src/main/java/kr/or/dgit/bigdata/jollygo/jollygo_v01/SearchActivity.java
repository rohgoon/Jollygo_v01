package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import kr.or.dgit.bigdata.jollygo.jollygo_v01.views.adapter.RvAdapter;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView sv;
    TextView tvTitle;
    static int searchCount;
    RecyclerView rv;
    FloatingActionButton fab;
    RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        rv.setLayoutManager(new GridLayoutManager(this,3));
        rvAdapter = new RvAdapter(this,fab);
        rv.setAdapter(rvAdapter);

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
                sv.setQuery("",true);
                rvAdapter.addItem(query);
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
                }else if (searchCount == 0 ){
                    tvTitle.setText("JOLLYGO-Search your own recipe");
                }
                return false;
            }
        });
        rv.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                int searchCount = rv.getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                    return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                    }else if (searchCount == 0 ){
                        tvTitle.setText("JOLLYGO-Search your own recipe");
                    }
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                int searchCount = rv.getAdapter().getItemCount();
                if (tvTitle.getText().toString().equals("")){
                    return;
                }else{
                    if (searchCount>1) {
                        tvTitle.setText("JOLLYGO- " + searchCount + " items were ready");
                    }else if (searchCount == 1){
                        tvTitle.setText("JOLLYGO- " + searchCount + " item was ready");
                    }else if (searchCount == 0){
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
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
