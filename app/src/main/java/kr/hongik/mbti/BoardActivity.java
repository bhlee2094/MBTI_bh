package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mPostRecyclerView = (RecyclerView)findViewById(R.id.board_recyclerView);
        mDatas = new ArrayList<>();

        findViewById(R.id.btn_edit).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mStore.collection(MemberInfo.post)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult() != null){
                                for(DocumentSnapshot snap : task.getResult()){
                                    Map<String, Object> shot = snap.getData();
                                    String documentId = String.valueOf(shot.get(MemberInfo.userNum));
                                    String title = String.valueOf(snap.get(MemberInfo.title));
                                    String contents = String.valueOf(shot.get(MemberInfo.contents));
                                    Post data = new Post(documentId, title, contents);
                                    mDatas.add(data);
                                }

                                mAdapter = new PostAdapter(mDatas);
                                mPostRecyclerView.setAdapter(mAdapter);
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(BoardActivity.this, PostActivity.class));
    }
}