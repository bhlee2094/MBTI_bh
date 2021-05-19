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

    private String gc_id;
    private String my_nickname;
    private String dgc_nickname;
    private String dgc_message;
    private RecyclerView dgrecyclerView;
    private DetailchatAdapter mAdapter;
    private ArrayList<Detailgroupchat> mlist;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    EditText editText;
    Button btnsend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailgchat);

        Intent intent = getIntent();
        gc_id = intent.getStringExtra("gchatId");
        TextView gct_title = findViewById(R.id.title);
        switch (gc_id){
            case "abcdefg1" : {
                gct_title.setText("ENFJ");
                break;
            }
            case "abcdefg2" : {
                gct_title.setText("ENFP");
                break;
            }
            case "abcdefg3" : {
                gct_title.setText("ENTJ");
                break;
            }
            case "abcdefg4" : {
                gct_title.setText("ENTP");
                break;
            }
            case "abcdefg5" : {
                gct_title.setText("ESFJ");
                break;
            }
            case "abcdefg6" : {
                gct_title.setText("ESFP");
                break;
            }
            case "abcdefg7" : {
                gct_title.setText("ESTJ");
                break;
            }
            case "abcdefg8" : {
                gct_title.setText("ESTP");
                break;
            }
            case "abcdefg9" : {
                gct_title.setText("INFJ");
                break;
            }
            case "abcdefg10" : {
                gct_title.setText("INFP");
                break;
            }
            case "abcdefg11" : {
                gct_title.setText("INTJ");
                break;
            }
            case "abcdefg12" : {
                gct_title.setText("INTP");
                break;
            }
            case "abcdefg13" : {
                gct_title.setText("ISFJ");
                break;
            }
            case "abcdefg14" : {
                gct_title.setText("ISFP");
                break;
            }
            case "abcdefg15" : {
                gct_title.setText("ISTJ");
                break;
            }
            case "abcdefg16" : {
                gct_title.setText("ISTP");
                break;
            }
        }
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
                                    Detailgroupchat Detailgroupchat = new Detailgroupchat(dgc_nickname, dgc_message);
                                    mlist.add(Detailgroupchat);
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
                Detailgroupchat Detailgroupchat = new Detailgroupchat(my_nickname, str_message);
                db.collection("groupchat/"+gc_id+"/detailchat").document(dgcId).set(Detailgroupchat)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mlist.add(Detailgroupchat);
                                mAdapter = new DetailchatAdapter(DetailgchatActivity.this, mlist);
                                dgrecyclerView.setAdapter(mAdapter);
                                editText.setText("");
                            }
                        });
            }
        });
    }

    class DetailchatAdapter extends RecyclerView.Adapter<DetailchatAdapter.DetailchatViewHolder>{//그룹 채팅 코멘트 어뎁터
        private ArrayList<Detailgroupchat> list;
        private Context context;

        public DetailchatAdapter(Context context, ArrayList<Detailgroupchat> list){
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
            Detailgroupchat Detailgroupchat = list.get(position);
            holder.nickname.setText(Detailgroupchat.getNickname());
            holder.message.setText(Detailgroupchat.getMessage());
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