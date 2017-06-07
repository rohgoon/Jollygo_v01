package kr.or.dgit.bigdata.jollygo.jollygo_v01;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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

public class MainFrontActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private ImageButton ibOauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_front);
        ibOauth= (ImageButton) findViewById(R.id.ibOauth);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.android_client_id))
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
                /*Intent intent = new Intent(MainFrontActivity.this,SearchActivity.class);
                startActivityForResult(intent,0);*/
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }//onCreate

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }//onActivityResult

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {//GoogleSignInAccount 개체에서 ID 토큰을 가져와서 Firebase 사용자 인증 정보로 교환하고 Firebase 사용자 인증 정보를 사용해 Firebase에 인증합니다.
        Log.e("로그인정보", "firebaseAuthWithGoogle:" + acct.getDisplayName());
        //getDisplayName 유저이름 받아옴
        //getUid
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainFrontActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }//firebaseAuthWithGoogle
  private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(),user.getDisplayName(),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"유저가 없습니다.",Toast.LENGTH_LONG).show();
        }
    }
  private void signOut() {
      // Firebase sign out
      mAuth.signOut();

      // Google sign out
      Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
              new ResultCallback<Status>() {
                  @Override
                  public void onResult(@NonNull Status status) {
                      updateUI(null);
                  }
              });
  }

 /*   @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        ibOauth.setImageResource(R.drawable.oauthbtn02);
    }*/
}
