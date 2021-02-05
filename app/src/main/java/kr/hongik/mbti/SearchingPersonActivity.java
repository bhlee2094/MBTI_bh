package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class SearchingPersonActivity extends AppCompatActivity {

    private TextView textView_memberinfo;
    private Button btn_send_friend_request, btn_chatOthers, btn_deleteFriend;
    private static final String TAG = "SearchingPersonActivity";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserNum = user.getUid();
    CollectionReference friendRef = db.collection("friendList/"+ UserNum + "/friends");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_person);

        Intent intent = getIntent();
        MemberInfo memberInfo = (MemberInfo) intent.getSerializableExtra("MemberInfo");
        String otherUserNum =intent.getStringExtra("otherUserNum");
        textView_memberinfo = (TextView) findViewById(R.id.sp_searchingPerson);
        textView_memberinfo.setText("닉네임 : " + memberInfo.getNickname() + "\n\n성별 : " + memberInfo.getGender() + "\n\n나이 : " + memberInfo.getAge() + "\n\nmbti : " + memberInfo.getMbti() + "\n\n주소 : " + memberInfo.getAddress() + "\n\n상태메시지 : " + memberInfo.getStateMessage());

        btn_send_friend_request = findViewById(R.id.btn_send_friend_request);
        btn_chatOthers = findViewById(R.id.btn_chatOhters);
        btn_deleteFriend=findViewById(R.id.btn_delete_friend);

        DocumentReference docRef = friendRef.document(memberInfo.getUserNum());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            btn_deleteFriend.setVisibility(View.VISIBLE);
                            btn_chatOthers.setVisibility(View.VISIBLE);
                            btn_send_friend_request.setVisibility(View.GONE);
                        } else {
                            btn_deleteFriend.setVisibility(View.GONE);
                            btn_chatOthers.setVisibility(View.GONE);
                            btn_send_friend_request.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        btn_send_friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendList friendList = new FriendList();
                friendList.sendFriendRequest(otherUserNum);
                startToast("친구신청완료");
                myStartActivity(FriendListActivity.class);
            }
        });

        btn_chatOthers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myStartActivity(ChatActivity.class);
                finish();
            }
        });

        btn_deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.delete();
                db.collection("friendList/" + memberInfo.getUserNum() +"/friends").document(UserNum).delete();
                startToast("친구 삭제 성공");
                myStartActivity(FriendListActivity.class);
            }
        });


    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}