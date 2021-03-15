package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import kr.hongik.mbti.navigation.BoardFragment;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText mtitle, mcontent;
    private String mnickname;
    DocumentReference usersRef = db.collection("users").document(user.getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mtitle = findViewById(R.id.post_title);
        mcontent = findViewById(R.id.post_content);
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
            String postId = db.collection("board").document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(Board.p_title, mtitle.getText().toString());
            data.put(Board.p_content, mcontent.getText().toString());
            data.put(Board.p_nickname, mnickname);
            data.put(Board.p_up, "추천 수 : 0");
            data.put(Board.p_comment, "댓글 수 : 0");
            db.collection("board").document(postId).set(data, SetOptions.merge());
            finish();
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}