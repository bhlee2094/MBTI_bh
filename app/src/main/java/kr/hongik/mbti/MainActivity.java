package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private TextView my_mbti;
    private Button btn_logout2, btn_searching, btn_matching, btn_userdata, btn_friend_list;


    FirebaseAuth mfirebaseAuth;
    FirebaseUser currentUser;
    private static final String TAG = "MainActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usersRef = db.collection("users").document(user.getUid());

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            myStartActivity(MainActivity.class);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mfirebaseAuth.getCurrentUser();
        String userNum = currentUser.getUid();

        my_mbti = findViewById(R.id.mymbti);

        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   DocumentSnapshot document = task.getResult();
                                                   if (document.exists()) {
                                                       Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                       my_mbti.setText("나의 MBTI는 " + document.getString("mbti") + "입니다");

                                                   } else {
                                                       Log.d(TAG, "No such document");
                                                   }
                                               } else {
                                                   Log.d(TAG, "get failed with ", task.getException());
                                               }
                                           }
                                       });

        btn_logout2 = findViewById(R.id.btn_logout2);

        btn_logout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(mfirebaseAuth);
                myStartActivity(LoginActivity.class);
                finish();
            }
        });

        btn_matching = findViewById(R.id.btn_matching);

        btn_matching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(MatchingActivity.class);
            }
        });

        btn_searching = findViewById(R.id.btn_searching);

        btn_searching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(SearchingActivity.class);
                finish();
            }
        });

        btn_userdata = findViewById(R.id.btn_userdata);

        btn_userdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(MyprofileActivity.class);
            }
        });

        btn_friend_list=findViewById(R.id.btn_friend_list);
        btn_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(FriendListActivity.class);
            }
        });

        }

        private void init(){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {
                myStartActivity(LoginActivity.class);
            }
            else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
                documentReference.get().addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                    myStartActivity(JoinActivity.class);

                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                );
            }
        }



    /**
     *     Firebase 로그아웃
     *      @author 장혜리
     */
    public void logout(FirebaseAuth mFirebaseAuth) {

        if (mFirebaseAuth.getCurrentUser() != null) {

            Toast.makeText(MainActivity.this, mFirebaseAuth.getUid() + "님이 로그아웃하셨습니다", Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();

        }
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    /**
     *     뒤로가기 두번 누르면 앱 종료
     *      @author 장혜리
     */
    private long backKeyPressedTime = 0;
    @Override
    public void onBackPressed() {
        // 500 milliseconds = 0.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 500) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

}