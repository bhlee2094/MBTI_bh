package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import kr.hongik.mbti.databinding.ActivityPostBinding;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ActivityPostBinding binding;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase database;
    private String mnickname;
    DocumentReference usersRef = db.collection("users").document(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        mnickname = documentSnapshot.getString("nickname");
                    }
                }
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(mAuth.getCurrentUser()!=null){
            String postId = db.collection("VOBoard").document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(VOBoard.p_title, binding.postTitle.getText().toString());
            data.put(VOBoard.p_content, binding.postContent.getText().toString());
            data.put(VOBoard.p_nickname, mnickname);
            data.put(VOBoard.p_up, "0");
            data.put(VOBoard.p_comment, "0");
            data.put(VOBoard.p_boardId, postId);
            data.put(VOBoard.p_uId, user.getUid());
            db.collection("VOBoard").document(postId).set(data, SetOptions.merge());

            VOBoard VOBoard = new VOBoard();
            database.getReference().child("VOBoard").child(postId).setValue(VOBoard);

            myStartActivity(MainActivity.class);
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}