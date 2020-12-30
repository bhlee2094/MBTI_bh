package kr.hongik.mbti;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * class LoginActivity
 * @author 장혜리
 */

public class LoginActivity extends AppCompatActivity {

    final String TAG = LoginActivity.class.getName();

    // 버튼
    private SignInButton btn_login_google;
    private LoginButton btn_login_facebook;

    //result 상수
    private static final int RC_SIGN_IN = 900;

    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setLoginButton();

        checkPermission();

        //1.파이어베이스 인증 객체 선언
        mFirebaseAuth= FirebaseAuth.getInstance();

        //2.Google Sign In 설정
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btn_login_google.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create a new instance of GoogleSignInClient
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



        //3.facebook login 설정
        mCallbackManager = CallbackManager.Factory.create();
        btn_login_facebook.setReadPermissions("email", "public_profile");
        btn_login_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser  = mFirebaseAuth.getCurrentUser();
        checkUser(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //페이스북 로그인
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 로그인 성공
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }


    }


    /**
     * google Token으로 firebase 로그인
     * 공식 문서를 바탕으로 변형하였습니다. 자세한 사항은 공식 문서를 참조바랍니다.
     * @param acct
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Log.d(TAG, "signInWithGoogle:success");
                            // 토큰 얻은 후 google 로그아웃
                            mGoogleSignInClient.signOut();
                            checkUser(mFirebaseAuth.getCurrentUser());
                        } else {
                            // 로그인 실패
                            Log.w(TAG, "signInWithGoogle:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    /**
     * facebook Token으로 firebase 로그인
     * 공식 문서를 바탕으로 변형하였습니다. 자세한 사항은 공식 문서를 참조바랍니다.
     * @param token
     */
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Log.d(TAG, "signInWithFacebook:success");
                            // 토큰 얻은 후 Facebook 로그아웃
                            LoginManager.getInstance().logOut();
                            checkUser(mFirebaseAuth.getCurrentUser());
                        } else {
                            // 로그인 실패
                            Log.w(TAG, "signInWithFacebook:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.failed_login, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    /**
     * firebae 유저 확인 및 화면 전환
     * @param user
     */
    public void checkUser(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(LoginActivity.this, mFirebaseAuth.getUid() + "님이 현재 접속중입니다", Toast.LENGTH_SHORT).show();
            myStartActivity(MainActivity.class);
            finish();
        }

    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * 외부저장소 권한허가, 거부 시 프로필 이미지 이용에 문제 있을 수 있음
     */
    private void checkPermission()    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Toast.makeText(this, "외부 저장소 사용을 위한 읽기/쓰기 권한을 요청합니다", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);

            } 
        }
    }


    /**
     *  로그인 버튼 text 변경
     */
    private void setLoginButton(){

        btn_login_google = findViewById(R.id.btn_google_login);
        TextView textView = (TextView)btn_login_google.getChildAt(0);
        textView.setText(getString(R.string.sing_in_google));

        btn_login_facebook = (LoginButton) findViewById(R.id.btn_facebook_login);
        btn_login_facebook.setLoginText(getString(R.string.sing_in_facebook));

    }
}
