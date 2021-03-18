package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class BoardActivity extends AppCompatActivity {

    private TextView b_title, b_content, b_up, b_comment;
    private Button btn_comment;
    private ArrayList<PostComment> mlist;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String str_boardId, str_up, str_nickname, com_nickname, com_content;
    private Integer i_up;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(BoardActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();

        b_title = findViewById(R.id.mboard_title);
        b_content = findViewById(R.id.mboard_content);
        b_up = findViewById(R.id.mboard_up);
        b_comment = findViewById(R.id.mboard_comment);

        str_boardId = intent.getStringExtra("boardId");
        str_up = intent.getStringExtra("up");
        str_nickname = intent.getStringExtra("nickname");
        i_up = Integer.parseInt(str_up);

        mlist = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.mboard_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BoardActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),1));
        CollectionReference collectionReference = db.collection("comment/"+str_boardId+"/comment");// collection path 항상 홀수로
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentSnapshot snap : task.getResult()){
                                    com_nickname = snap.getString("nickname");
                                    com_content = snap.getString("comment");
                                    PostComment postComment = new PostComment(com_nickname, com_content);
                                    mlist.add(postComment);
                                }

                                commentAdapter = new CommentAdapter(BoardActivity.this, mlist);
                                recyclerView.setAdapter(commentAdapter);
                            }
                        }
                    }
                });

        b_title.setText(intent.getStringExtra("title"));
        b_content.setText(intent.getStringExtra("content"));
        b_up.setText(str_up);
        b_comment.setText(intent.getStringExtra("comment"));

        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.btn_up);//추천 토글 버튼
        SharedPreferences sharedPreferences = getSharedPreferences("up",MODE_PRIVATE);
        Boolean is_up = sharedPreferences.getBoolean("b_up",false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        toggleButton.setChecked(is_up);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){//추천취소
                    i_up--;
                    String c_up = Integer.toString(i_up);
                    b_up.setText(c_up);
                    editor.putBoolean("b_up", true);
                    editor.commit();
                    db.collection("board").document(str_boardId)
                            .update("up", c_up)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                }else{//추천하기
                    i_up++;
                    String c_up = Integer.toString(i_up);
                    b_up.setText(c_up);
                    editor.putBoolean("b_up",false);
                    editor.commit();
                    db.collection("board").document(str_boardId)
                            .update("up", c_up)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            });
                }
            }
        });//추천 토글 버튼 마지막

        btn_comment = (Button)findViewById(R.id.btn_comment);//댓글 버튼
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BoardActivity.this);
                View view = LayoutInflater.from(BoardActivity.this).inflate(R.layout.comment_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit2);
                final EditText editComment = (EditText) view.findViewById(R.id.edit_comment);

                final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str_comment = editComment.getText().toString();
                        String commentId = db.collection("comment/"+str_boardId+"/comment").document().getId();
                        PostComment postComment = new PostComment(str_nickname, str_comment);
                        collectionReference.document(commentId)
                                .set(postComment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mlist.add(postComment);
                                        commentAdapter = new CommentAdapter(BoardActivity.this, mlist);
                                        recyclerView.setAdapter(commentAdapter);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });//댓글 버튼 마지막
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{//댓글 어뎁터

        private ArrayList<PostComment> list;
        private Context context;

        public CommentAdapter(Context context, ArrayList<PostComment> list){
            this.context = context;
            this.list = list;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
            CommentViewHolder holder = new CommentViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position){
            PostComment postComment = list.get(position);
            holder.nickname.setText(postComment.getNickname());
            holder.comment.setText(postComment.getComment());
        }

        @Override
        public int getItemCount(){
            return list.size();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder {
            private TextView nickname;
            private TextView comment;

            public CommentViewHolder(View view){
                super(view);
                nickname = view.findViewById(R.id.comment_nickname);
                comment = view.findViewById(R.id.comment_content);
            }
        }
    }
}