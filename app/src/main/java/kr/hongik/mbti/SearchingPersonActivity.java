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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.hongik.mbti.databinding.ActivitySearchingPersonBinding;

public class SearchingPersonActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySearchingPersonBinding binding;

    private static final String TAG = "SearchingPersonActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String UserNum = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String otherUserNum;
    private VOMemberInfo VOMemberInfo;
    private DocumentReference docRef;
    CollectionReference friendRef = db.collection("friendList/"+ UserNum + "/friends");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchingPersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        VOMemberInfo = (VOMemberInfo) intent.getSerializableExtra("VOMemberInfo");
        otherUserNum =intent.getStringExtra("otherUserNum");
        binding.setVOMemberInfo(VOMemberInfo);

        docRef = friendRef.document(otherUserNum);
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

        binding.btnSendFriendRequest.setOnClickListener(this);
        binding.btnChatOhters.setOnClickListener(this);
        binding.btnDeleteFriend.setOnClickListener(this);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_chatOhters :
            {
                Intent intent = new Intent(SearchingPersonActivity.this, ChatActivity.class);
                intent.putExtra("otherUserNum", otherUserNum);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.btn_delete_friend :
            {
                docRef.delete();
                db.collection("friendList/" + VOMemberInfo.getUserNum() +"/friends").document(UserNum).delete();
                startToast("친구 삭제 성공");
                finish();
                break;
            }
            case R.id.btn_send_friend_request :
            {
                FriendList friendList = new FriendList();
                friendList.sendFriendRequest(otherUserNum);
                startToast("친구신청완료");
                finish();
                break;
            }
        }
    }
}