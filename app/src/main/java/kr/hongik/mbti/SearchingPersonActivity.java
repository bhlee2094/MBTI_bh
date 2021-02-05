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

        CollectionReference friendRef2 = db.collection("friendlist/" + memberInfo.getUserNum() +"/friends");

        btn_send_friend_request = findViewById(R.id.btn_send_friend_request);
        btn_chatOthers = findViewById(R.id.btn_chatOhters);

        /*friendRef.whereIn("friends", Arrays.asList(memberInfo.getUserNum()))
                 .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                btn_send_friend_request.setVisibility(View.GONE);
                                btn_chatOthers.setVisibility(View.VISIBLE);
                            }
                        } else {
                            btn_send_friend_request.setVisibility(View.VISIBLE);
                        }
                    }
                });*/


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

        btn_deleteFriend=findViewById(R.id.btn_delete_friend);
        btn_deleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendRef.document(memberInfo.getUserNum())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("친구 삭제 완료");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("친구 삭제 실패");
                            }
                        });
                friendRef2.document(UserNum)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myStartActivity(FriendListActivity.class);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("친구 삭제 실패");
                            }
                        });
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