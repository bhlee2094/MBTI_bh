package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MiniGameActivity extends AppCompatActivity {

    private RecyclerView mini_RecyclerView;
    private ArrayList<VOMiniGame> mini_list;
    private MiniGameAdapter miniGameAdapter;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_game);

        MiniInit();
    }

    private void MiniInit(){ //미니게임 초기설정
        mini_RecyclerView = (RecyclerView) findViewById(R.id.mini_RecyclerView);
        mini_list = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mini_RecyclerView.setHasFixedSize(true);
        mini_RecyclerView.setLayoutManager(layoutManager);
        mini_RecyclerView.addItemDecoration(new DividerItemDecoration(mini_RecyclerView.getContext(),1));
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("friendList/" + user.getUid() + "/friends");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult()!=null){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                    VOMiniGame VOMiniGame = new VOMiniGame(user.getUid(), documentSnapshot.getId());
                                    mini_list.add(VOMiniGame);
                                }

                                miniGameAdapter = new MiniGameAdapter(MiniGameActivity.this, mini_list);
                                mini_RecyclerView.setAdapter(miniGameAdapter);
                            }
                        }
                    }
                });
    } //미니게임 초기설정 끝

    public class MiniGameAdapter extends RecyclerView.Adapter<MiniGameAdapter.MiniGameViewHolder>{ //미니게임 어뎁터

        private ArrayList<VOMiniGame> list;
        private Context context;

        public MiniGameAdapter(Context context, ArrayList<VOMiniGame> list){
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public MiniGameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minigame_item, parent, false);
            MiniGameViewHolder holder = new MiniGameViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MiniGameAdapter.MiniGameViewHolder holder, int position) {
            VOMiniGame VOMiniGame = list.get(position);
            ProfileImage profileImage = new ProfileImage(context, VOMiniGame.getFriendUid());
            profileImage.showProfileImage(holder.mini_ImageView);
            holder.tv_myUid.setText(VOMiniGame.getMyUid());
            holder.tv_friendUid.setText(VOMiniGame.getFriendUid());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MiniGameViewHolder extends RecyclerView.ViewHolder{
            private ImageView mini_ImageView;
            private TextView tv_myUid;
            private TextView tv_friendUid;

            public MiniGameViewHolder(View view) {
                super(view);
                mini_ImageView = view.findViewById(R.id.mini_ImageView);
                tv_myUid = view.findViewById(R.id.tv_myUid);
                tv_friendUid = view.findViewById(R.id.tv_friendUid);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MiniGameActivity.this, SubMiniGameActivity.class);
                        intent.putExtra("friendUid",list.get(getAdapterPosition()).getFriendUid());
                        startActivity(intent);
                    }
                });
            }
        }
    }//미니게임 어뎁터 끝

}