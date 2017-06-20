package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;


public class MainFrontActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final int SEARCHACTIVITY = 9002;
    private ImageButton ibOauth;
    private ImageView ivFront;
    private ProgressDialog mDialog;
    private String[] permissions = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_front);
        checkPermissions();
        ibOauth= (ImageButton) findViewById(R.id.ibOauth);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();*/
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, null) //바뀔 것들
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ibOauth.setOnClickListener(new View.OnClickListener() {//
            @Override
            public void onClick(View v) {
                ibOauth.setImageResource(R.drawable.oauthbtn_ns);
                signIn();

            }
        });
        mAuth = FirebaseAuth.getInstance();

        ivFront = (ImageView) findViewById(R.id.ivFront);
        ivFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                updateUI(currentUser);
            }
        });


    }//onCreate

    @Override
    protected void onStart() {
        super.onStart();
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mGoogleApiClient.connect();
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {//어디에서 온건지 분류 필요
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }else if (requestCode == SEARCHACTIVITY){
            String res = data.getExtras().getString(SearchActivity.APP_RESULT);
            if (res.equals(SearchActivity.APP_FINISH)){
                finish();
            }

        }
    }
    // [END onactivityresult]

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + acct.getId());
        mDialog = ProgressDialog.show(MainFrontActivity.this,null, "Please wait...", true);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("isSuccessful", "signInWithCredential:success");
                            ibOauth.setImageResource(R.drawable.oauthbtn02);
                            Intent intent = new Intent(MainFrontActivity.this,SearchActivity.class);
                            startActivityForResult(intent,SEARCHACTIVITY);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("isNotSuccessful", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainFrontActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        mDialog.dismiss();
                    }
                });
    }//firebaseAuthWithGoogle
  private void updateUI(FirebaseUser user) {
        if (user != null) {
           Toast.makeText(getApplicationContext(),user.getDisplayName(),Toast.LENGTH_SHORT).show();
        } else {
           Toast.makeText(getApplicationContext(),"유저가 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }
  private void signOut() {
      // Firebase sign out
      mAuth.signOut();
      Log.e("싸인아웃","signOut");
      // Google sign out
      Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
              new ResultCallback<Status>() {
                  @Override
                  public void onResult(@NonNull Status status) {
                      updateUI(null);
                  }
              });
  }

   /* @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        ibOauth.setImageResource();
    }*/
   @Override
   public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
       // An unresolvable error has occurred and Google APIs (including Sign-In) will not
       // be available.
       Log.d("커넥팅 실패", "onConnectionFailed:" + connectionResult);
       Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
   }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "사진과 사용자 로그인을 위한 권한 설정입니다. 동의해주시면 어플리케이션 사용이 가능합니다.", Toast.LENGTH_SHORT).show();
        checkPermissions();
    }
}
