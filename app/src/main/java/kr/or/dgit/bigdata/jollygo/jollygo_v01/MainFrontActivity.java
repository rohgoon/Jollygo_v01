package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainFrontActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_front);
        final ImageButton ibOauth= (ImageButton) findViewById(R.id.ibOauth);
        ibOauth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibOauth.setImageResource(R.drawable.oauthbtn_ns);
                Intent intent = new Intent(MainFrontActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
