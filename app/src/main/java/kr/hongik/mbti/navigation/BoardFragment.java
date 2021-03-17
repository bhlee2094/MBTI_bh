package kr.hongik.mbti.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;


import kr.hongik.mbti.Board;
import kr.hongik.mbti.BoardActivity;
import kr.hongik.mbti.Chat;
import kr.hongik.mbti.ChatActivity;
import kr.hongik.mbti.FriendListActivity;
import kr.hongik.mbti.MemberInfo;
import kr.hongik.mbti.PostActivity;
import kr.hongik.mbti.R;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class BoardFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "BoardFragment";
    private ArrayList<Board> mlist;
    private RecyclerView mRecyclerView;
    private BoardAdapter mAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String all_title, all_content,all_nickname, all_up, all_comment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_board, container, false);

        Context context = root.getContext();
        mlist = new ArrayList<>();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.board_RecyclerView);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), 1));
        db.collection("board")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult() != null){
                                for(DocumentSnapshot snap : task.getResult()){
                                    all_title = snap.getString("title");
                                    all_content = snap.getString("content");
                                    all_nickname = snap.getString("nickname");
                                    all_up = snap.getString("up");
                                    all_comment = snap.getString("comment");
                                    Board board = new Board(all_title, all_content, all_nickname, all_up, all_comment);
                                    mlist.add(board);
                                }

                                mAdapter = new BoardAdapter(getActivity(), mlist);
                                mRecyclerView.setAdapter(mAdapter);

                            }else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    }
                });

        Button btnEdit = (Button)root.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnEdit :
            {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivityForResult(intent, 100);
                break;
            }
        }
    }

    class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder>{// 게시판 어뎁터

        private ArrayList<Board> list;
        private Context context;

        public BoardAdapter(Context context, ArrayList<Board> list){
            this.context = context;
            this.list = list;
        }


        @Override
        public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);
            BoardViewHolder holder = new BoardViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(BoardViewHolder holder, int position){
            Board board = list.get(position);
            holder.title.setText(board.getTitle());
            holder.content.setText(board.getContent());
            holder.nickname.setText(board.getNickname());
            holder.up.setText(board.getUp());
            holder.comment.setText(board.getComment());

        }

        @Override
        public int getItemCount(){
            return list.size();
        }

        public class BoardViewHolder extends RecyclerView.ViewHolder {
            private TextView title;
            private TextView content;
            private TextView nickname;
            private TextView up;
            private TextView comment;

            public BoardViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.board_title);
                content = view.findViewById(R.id.board_content);
                nickname = view.findViewById(R.id.board_nickname);
                up = view.findViewById(R.id.board_up);
                comment = view.findViewById(R.id.board_comment);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(getActivity(), BoardActivity.class);
                        startActivityForResult(intent, 101);
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }
        }
    }//어뎁터 마지막
}
