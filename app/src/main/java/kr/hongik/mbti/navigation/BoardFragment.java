package kr.hongik.mbti.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import kr.hongik.mbti.MemberInfo;
import kr.hongik.mbti.Post;
import kr.hongik.mbti.PostActivity;
import kr.hongik.mbti.PostAdapter;
import kr.hongik.mbti.R;


public class BoardFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_board, container, false);
        Button btn_edit = (Button)root.findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(this);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPostRecyclerView = (RecyclerView) getView().findViewById(R.id.board_recyclerView);
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
        switch (v.getId()){
            case R.id.btn_edit:
            {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        }
    }
}
