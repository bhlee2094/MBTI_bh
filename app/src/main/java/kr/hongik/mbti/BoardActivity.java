package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kr.hongik.mbti.databinding.ActivityBoardBinding;

public class BoardActivity extends AppCompatActivity {

    private ActivityBoardBinding binding;

    private ArrayList<VOPostComment> mlist;
    private CommentAdapter commentAdapter;
    private FirebaseDatabase database;
    private FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String str_boardId, str_up, str_comment, str_nickname, com_nickname, com_content;
    public static String commentId;
    private Integer i_up, i_comment;
    private CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boardInit();
    }

    private void boardInit(){
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Intent intent = getIntent();

        str_boardId = intent.getStringExtra("boardId");
        str_up = intent.getStringExtra("up");
        str_comment = intent.getStringExtra("comment");
        str_nickname = intent.getStringExtra("nickname");
        i_up = Integer.parseInt(str_up);
        i_comment = Integer.parseInt(str_comment);

        mlist = new ArrayList<>();
        binding.mboardRecyclerView.setHasFixedSize(true);
        binding.mboardRecyclerView.setLayoutManager(new LinearLayoutManager(BoardActivity.this));
        binding.mboardRecyclerView.addItemDecoration(new DividerItemDecoration(binding.mboardRecyclerView.getContext(),1));
        collectionReference = db.collection("comment/"+str_boardId+"/comment");// collection path 항상 홀수로
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentSnapshot snap : task.getResult()){
                                    com_nickname = snap.getString("nickname");
                                    com_content = snap.getString("comment");
                                    VOPostComment VOPostComment = new VOPostComment(com_nickname, com_content);
                                    mlist.add(VOPostComment);
                                }

                                commentAdapter = new CommentAdapter(BoardActivity.this, mlist);
                                binding.mboardRecyclerView.setAdapter(commentAdapter);
                            }
                        }
                    }
                });

        binding.mboardTitle.setText(intent.getStringExtra("title"));
        binding.mboardContent.setText(intent.getStringExtra("content"));
        binding.mboardUp.setText(str_up);
        binding.mboardComment.setText(str_comment);

        binding.btnComment.setOnClickListener(new View.OnClickListener() {//댓글 버튼
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
                        i_comment++;
                        str_comment = i_comment.toString();
                        binding.mboardComment.setText(str_comment);
                        db.collection("board").document(str_boardId)
                                .update("comment", str_comment);
                        String str_comment2 = editComment.getText().toString();
                        commentId = db.collection("comment/"+str_boardId+"/comment").document().getId();
                        VOPostComment VOPostComment = new VOPostComment(str_nickname, str_comment2, commentId);
                        collectionReference.document(commentId)
                                .set(VOPostComment)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mlist.add(VOPostComment);
                                        commentAdapter = new CommentAdapter(BoardActivity.this, mlist);
                                        binding.mboardRecyclerView.setAdapter(commentAdapter);
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
                dialog.show();
            }
        });//댓글 버튼 마지막

        binding.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStarClicked(database.getReference().child("board").child(str_boardId));
            }
        });

        db.collection("up/"+str_boardId+"/up").document(firebaseAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                Boolean bl_up = documentSnapshot.getBoolean("up");
                                if(bl_up==true){
                                    binding.starButton.setImageResource(R.drawable.baseline_favorite_black_24dp);
                                }else{
                                    binding.starButton.setImageResource(R.drawable.baseline_favorite_border_black_24dp);
                                }
                            }
                        }
                    }
                });
    }

    private void onStarClicked(DatabaseReference postRef) { //좋아요 버튼
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Board p = mutableData.getValue(Board.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(firebaseAuth.getCurrentUser().getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(firebaseAuth.getCurrentUser().getUid());
                    i_up--;
                    str_up = i_up.toString();
                    binding.mboardUp.setText(str_up);
                    db.collection("board").document(str_boardId)
                            .update("up", str_up);
                    binding.starButton.setImageResource(R.drawable.baseline_favorite_border_black_24dp);
                    VOPostUp VOPostUp = new VOPostUp(false);
                    db.collection("up/"+str_boardId+"/up").document(firebaseAuth.getCurrentUser().getUid()).set(VOPostUp);
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(firebaseAuth.getCurrentUser().getUid(), true);
                    i_up++;
                    str_up = i_up.toString();
                    binding.mboardUp.setText(str_up);
                    db.collection("board").document(str_boardId)
                            .update("up", str_up);

                    binding.starButton.setImageResource(R.drawable.baseline_favorite_black_24dp);
                    VOPostUp VOPostUp = new VOPostUp(true);
                    db.collection("up/"+str_boardId+"/up").document(firebaseAuth.getCurrentUser().getUid()).set(VOPostUp);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed,
                                   DataSnapshot currentData) {
                // Transaction completed
            }
        });
    } //좋아요 버튼 마지막

    public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{//댓글 어뎁터

        private ArrayList<VOPostComment> list;
        private Context context;

        public CommentAdapter(Context context, ArrayList<VOPostComment> list){
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
            VOPostComment VOPostComment = list.get(position);
            holder.nickname.setText(VOPostComment.getNickname());
            holder.comment.setText(VOPostComment.getComment());
        }

        @Override
        public int getItemCount(){
            return list.size();
        }

        public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private TextView nickname;
            private TextView comment;

            public CommentViewHolder(View view){
                super(view);
                nickname = view.findViewById(R.id.comment_nickname);
                comment = view.findViewById(R.id.comment_content);
                view.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem Delete = menu.add(Menu.NONE, R.id.menu_delete, 1 , "삭제");
                Delete.setOnMenuItemClickListener(onMenuItemClickListener);
            }
            private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_delete:
                            i_comment--;
                            str_comment = i_comment.toString();
                            binding.mboardComment.setText(str_comment);
                            db.collection("board").document(str_boardId).update("comment", str_comment);
                            collectionReference.document(list.get(getAdapterPosition()).getCommentId()).delete();
                            list.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), list.size());
                    }
                    return true;
                }
            };
        }
    }//댓글 어뎁터 끝

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(BoardActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }
}