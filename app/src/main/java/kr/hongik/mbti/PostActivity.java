package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mTitle, mContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTitle = findViewById(R.id.title_edit);
        mContents = findViewById(R.id.contents_edit);

        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser()!=null){
            String postId = mStore.collection(MemberInfo.post).document().getId();
            Map<String, Object> data = new HashMap<>();
            data.put(MemberInfo.userNum, mAuth.getCurrentUser().getUid());
            data.put(MemberInfo.title, mTitle.getText().toString());
            data.put(MemberInfo.contents, mContents.getText().toString());
            mStore.collection(MemberInfo.post).document(postId).set(data, SetOptions.merge());
            finish();
        }
    }
}