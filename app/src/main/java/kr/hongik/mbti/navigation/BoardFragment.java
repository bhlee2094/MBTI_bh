package kr.hongik.mbti.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import kr.hongik.mbti.Board;
import kr.hongik.mbti.BoardActivity;
import kr.hongik.mbti.PostActivity;
import kr.hongik.mbti.R;

import static kr.hongik.mbti.BoardActivity.commentId;

public class BoardFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "BoardFragment";
    private ArrayList<Board> mlist;
    private RecyclerView mRecyclerView;
    private BoardAdapter mAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase database;
    private String all_title, all_content,all_nickname, all_boardId, all_up, all_comment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_board, container, false);

        Context context = root.getContext();

        mlist = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

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
                                    all_boardId = snap.getId();
                                    Board board = new Board(all_title, all_content, all_nickname, all_up, all_comment, all_boardId);
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

    public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardViewHolder>{// 게시판 어뎁터

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

        public class BoardViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
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

                view.setOnCreateContextMenuListener(this);//contextMenu 리스너

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), BoardActivity.class);

                        intent.putExtra("title", list.get(getAdapterPosition()).getTitle());
                        intent.putExtra("content", list.get(getAdapterPosition()).getContent());
                        intent.putExtra("up", list.get(getAdapterPosition()).getUp());
                        intent.putExtra("nickname",list.get(getAdapterPosition()).getNickname());
                        intent.putExtra("comment", list.get(getAdapterPosition()).getComment());
                        intent.putExtra("boardId", list.get(getAdapterPosition()).getBoardId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem Edit = menu.add(Menu.NONE, R.id.menu_edit, 1 , "수정");
                MenuItem Delete = menu.add(Menu.NONE, R.id.menu_delete, 2 , "삭제");
                Edit.setOnMenuItemClickListener(onMenuItemClickListener);
                Delete.setOnMenuItemClickListener(onMenuItemClickListener);
            }
            private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_edit:
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            View view = LayoutInflater.from(context).inflate(R.layout.edit_box, null, false);
                            builder.setView(view);
                            final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
                            final EditText editTitle = (EditText) view.findViewById(R.id.edit_title);
                            final EditText editContent = (EditText) view.findViewById(R.id.edit_content);
                            editTitle.setText(list.get(getAdapterPosition()).getTitle());
                            editContent.setText(list.get(getAdapterPosition()).getContent());

                            final AlertDialog dialog = builder.create();
                            ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String strTitle = editTitle.getText().toString();
                                    String strContent = editContent.getText().toString();
                                    String strnickname = list.get(getAdapterPosition()).getNickname();
                                    String strup = list.get(getAdapterPosition()).getUp();
                                    String strcomment = list.get(getAdapterPosition()).getComment();
                                    String strboardId = list.get(getAdapterPosition()).getBoardId();
                                    Board board = new Board(strTitle, strContent, strnickname, strup, strcomment, strboardId);

                                    db.collection("board").document(list.get(getAdapterPosition()).getBoardId())
                                            .update("title", strTitle, "content", strContent)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    list.set(getAdapterPosition(), board);
                                                    notifyItemChanged(getAdapterPosition());
                                                    dialog.dismiss();
                                                }
                                            });
                                }
                            });
                            dialog.show();
                            break;

                        case R.id.menu_delete:
                            db.collection("up/"+all_boardId+"/up").document(firebaseAuth.getCurrentUser().getUid()).delete();
                            db.collection("comment/"+all_boardId+"/comment").document(commentId).delete();
                            db.collection("board").document(list.get(getAdapterPosition()).getBoardId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            database.getReference().child("board").child(all_boardId).removeValue();
                                            list.remove(getAdapterPosition());
                                            notifyItemRemoved(getAdapterPosition());
                                            notifyItemRangeChanged(getAdapterPosition(), list.size());
                                        }
                                    });
                            break;
                    }
                    return true;
                }
            };
        }
    }//어뎁터 마지막
}
