package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.hongik.mbti.databinding.ActivitySearchingPersonBinding;

public class SearchingPersonActivity extends AppCompatActivity {

    private ActivitySearchingPersonBinding binding;

    private static final String TAG = "SearchingPersonActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserNum = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference friendRef = db.collection("friendList/"+ UserNum + "/friends");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchingPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        MemberInfo memberInfo = (MemberInfo) intent.getSerializableExtra("MemberInfo");
        String otherUserNum =intent.getStringExtra("otherUserNum");
        binding.spSearchingPerson.setText("닉네임 : " + memberInfo.getNickname() + "\n\n성별 : " + memberInfo.getGender() + "\n\n나이 : " + memberInfo.getAge() + "\n\nmbti : " + memberInfo.getMbti() + "\n\n주소 : " + memberInfo.getAddress() + "\n\n상태메시지 : " + memberInfo.getStateMessage());

        DocumentReference docRef = friendRef.document(otherUserNum);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            binding.btnDeleteFriend.setVisibility(View.VISIBLE);
                            binding.btnChatOhters.setVisibility(View.VISIBLE);
                            binding.btnSendFriendRequest.setVisibility(View.GONE);
                        } else {
                            binding.btnDeleteFriend.setVisibility(View.GONE);
                            binding.btnChatOhters.setVisibility(View.GONE);
                            binding.btnSendFriendRequest.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        binding.btnSendFriendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendList friendList = new FriendList();
                friendList.sendFriendRequest(otherUserNum);
                startToast("친구신청완료");
                finish();
            }
        });

        binding.btnChatOhters.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchingPersonActivity.this, ChatActivity.class);
                intent.putExtra("otherUserNum", otherUserNum);
                startActivity(intent);
                finish();
            }
        });

        binding.btnDeleteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docRef.delete();
                db.collection("friendList/" + memberInfo.getUserNum() +"/friends").document(UserNum).delete();
                startToast("친구 삭제 성공");
                finish();
            }
        });


    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}