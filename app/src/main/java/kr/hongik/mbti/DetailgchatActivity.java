package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailgchatActivity extends AppCompatActivity {

    private TextView gct_title;
    private String gc_title, gc_id, my_nickname, dgc_nickname, dgc_message;
    private RecyclerView dgrecyclerView;
    private DetailchatAdapter mAdapter;
    private ArrayList<VODetailgroupchat> mlist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText editText;
    Button btnsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailgchat);

        Intent intent = getIntent();
        gc_title = intent.getStringExtra("title");
        gc_id = intent.getStringExtra("gchatId");
        gct_title = findViewById(R.id.title);
        gct_title.setText(gc_title);
        db.collection("users").document(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                my_nickname = documentSnapshot.getString("nickname");
                            }
                        }
                    }
                });
        mlist = new ArrayList<>();
        dgrecyclerView = (RecyclerView)findViewById(R.id.chat_recyclerview);
        dgrecyclerView.setLayoutManager(new LinearLayoutManager(DetailgchatActivity.this));
        dgrecyclerView.setHasFixedSize(true);
        db.collection("groupchat/"+gc_id+"/detailchat")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentSnapshot snapshot : task.getResult()){
                                    dgc_message = snapshot.getString("message");
                                    dgc_nickname = snapshot.getString("nickname");
                                    VODetailgroupchat VODetailgroupchat = new VODetailgroupchat(dgc_nickname, dgc_message);
                                    mlist.add(VODetailgroupchat);
                                }
                                mAdapter = new DetailchatAdapter(DetailgchatActivity.this, mlist);
                                dgrecyclerView.setAdapter(mAdapter);
                            }
                        }
                    }
                });
        editText = (EditText)findViewById(R.id.edittext);
        btnsend = (Button)findViewById(R.id.btnSend);
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dgcId = db.collection("groupchat/"+gc_id+"/detailchat").document().getId();
                String str_message = editText.getText().toString();
                VODetailgroupchat VODetailgroupchat = new VODetailgroupchat(my_nickname, str_message);
                db.collection("groupchat/"+gc_id+"/detailchat").document(dgcId).set(VODetailgroupchat)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mlist.add(VODetailgroupchat);
                                mAdapter = new DetailchatAdapter(DetailgchatActivity.this, mlist);
                                dgrecyclerView.setAdapter(mAdapter);
                                editText.setText("");
                            }
                        });
            }
        });
    }

    class DetailchatAdapter extends RecyclerView.Adapter<DetailchatAdapter.DetailchatViewHolder>{//그룹 채팅 코멘트 어뎁터
        private ArrayList<VODetailgroupchat> list;
        private Context context;

        public DetailchatAdapter(Context context, ArrayList<VODetailgroupchat> list){
            this.context=context;
            this.list=list;
        }

        @Override
        public DetailchatViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailgchat_item,parent,false);
            DetailchatViewHolder holder = new DetailchatViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(DetailchatViewHolder holder, int position){
            VODetailgroupchat VODetailgroupchat = list.get(position);
            holder.nickname.setText(VODetailgroupchat.getNickname());
            holder.message.setText(VODetailgroupchat.getMessage());
        }

        @Override
        public int getItemCount() {return list.size();}

        public class DetailchatViewHolder extends RecyclerView.ViewHolder {
            private TextView nickname;
            private TextView message;

            public DetailchatViewHolder(View view){
                super(view);
                nickname = view.findViewById(R.id.dgc_nickname);
                message = view.findViewById(R.id.dgc_message);
            }
        }
    }//그룹 채팅 코멘트 어뎁터 마지막

}