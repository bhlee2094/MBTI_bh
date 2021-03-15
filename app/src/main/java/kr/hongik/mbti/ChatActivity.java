package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    EditText editText;
    Button btnFinish, btnSend;
    String otherUserNum;
    private String uid;
    private String chatRoomUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //로그인된 아이디
        otherUserNum =intent.getStringExtra("otherUserNum"); //채팅 당하는 아이디

        editText = (EditText) findViewById(R.id.edittext);

        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener((v) -> {finish();});

        recyclerView = (RecyclerView) findViewById(R.id.chat_recyclerview);
        recyclerView.setHasFixedSize(true); //높이 고정

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Chat chat = new Chat();
                chat.users.put(uid,true);
                chat.users.put(otherUserNum,true);

                if(chatRoomUid == null){
                    btnSend.setEnabled(false);
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            checkChatRoom();
                        }
                    });

                }else{
                    Chat.Comment comment = new Chat.Comment();
                    comment.uid = uid;
                    comment.message = editText.getText().toString();
                    FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            editText.setText("");
                        }
                    });
                }
            }
        });
        checkChatRoom();
    }

    void checkChatRoom(){
        FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot item : snapshot.getChildren()){
                    Chat chat = item.getValue(Chat.class);
                    if(chat.users.containsKey(otherUserNum)){
                        chatRoomUid = item.getKey();
                        btnSend.setEnabled(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                        recyclerView.setAdapter(new RecyclerViewAdapter());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        List<Chat.Comment> comments;

        public RecyclerViewAdapter(){
            comments = new ArrayList<>();

            FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();
                    for(DataSnapshot item : snapshot.getChildren()){
                        comments.add(item.getValue(Chat.Comment.class));
                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @Override
        public int getItemViewType(int position){
            if(comments.get(position).uid.equals(otherUserNum)){
                return 1;
            }else{
                return 2;
            }
        }
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.right_row_item, parent, false);

            if(viewType == 1){
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
            }

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
            ((MessageViewHolder)holder).textView_message.setText(comments.get(position).message);
        }

        @Override
        public int getItemCount(){
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = view.findViewById(R.id.tvChat);
            }
        }
    }
}