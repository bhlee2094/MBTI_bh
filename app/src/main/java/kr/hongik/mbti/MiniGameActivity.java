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
        VOMiniGame VOMiniGame = new VOMiniGame("숫자야구(클릭)");
        mini_list.add(VOMiniGame);
        miniGameAdapter = new MiniGameAdapter(this, mini_list);
        mini_RecyclerView.setAdapter(miniGameAdapter);
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
            holder.mini_name.setText(VOMiniGame.getMininame());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MiniGameViewHolder extends RecyclerView.ViewHolder{
            private TextView mini_name;

            public MiniGameViewHolder(View view) {
                super(view);
                mini_name = view.findViewById(R.id.mini_name);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MiniGameActivity.this, SubMiniGameActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }
    }//미니게임 어뎁터 끝

}