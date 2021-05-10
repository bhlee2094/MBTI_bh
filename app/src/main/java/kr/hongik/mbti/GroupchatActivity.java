package kr.hongik.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupchatActivity extends AppCompatActivity {

    private RecyclerView gchat_recyclerView;
    private GroupchatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        init();
        getData();
    }

    private void init(){//리사이클러뷰 초기화
        gchat_recyclerView = findViewById(R.id.gchat_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        gchat_recyclerView.setLayoutManager(linearLayoutManager);
        gchat_recyclerView.setHasFixedSize(true);
        gchat_recyclerView.addItemDecoration(new DividerItemDecoration(gchat_recyclerView.getContext(),1));
        mAdapter = new GroupchatAdapter();
        gchat_recyclerView.setAdapter(mAdapter);
    }

    private void getData(){//그룹채팅방 데이터 입력
        List<String> listTitle = Arrays.asList("게임채팅방", "프로그래머채팅방", "예술가채팅방", "INTJ채팅방");
        List<Integer> listResId = Arrays.asList(R.drawable.game, R.drawable.developer, R.drawable.artist, R.drawable.intj);
        List<String> listId = Arrays.asList("abcdefg1","abcdefg2","abcdefg3","abcdefg4");
        for(int i=0; i<listTitle.size(); i++){
            VOGroupchat VOGroupchat = new VOGroupchat();
            VOGroupchat.setGchat_title(listTitle.get(i));
            VOGroupchat.setGchat_image(listResId.get(i));
            VOGroupchat.setGchatId(listId.get(i));

            mAdapter.addItem(VOGroupchat);
        }
        mAdapter.notifyDataSetChanged();
    }

    public class GroupchatAdapter extends RecyclerView.Adapter<GroupchatAdapter.GroupchatViewHolder>{//그룹채팅 어뎁터
        private ArrayList<VOGroupchat> list = new ArrayList<>();

        @Override
        public GroupchatViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupchat_item, parent, false);
            GroupchatViewHolder holder = new GroupchatViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(GroupchatViewHolder holder, int position){
            holder.onBind(list.get(position));
        }

        @Override
        public int getItemCount(){
            return list.size();
        }

        void addItem(VOGroupchat VOGroupchat){
            list.add(VOGroupchat);
        }

        public class GroupchatViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;
            private TextView title;

            public GroupchatViewHolder (View view){
                super(view);
                image = view.findViewById(R.id.gchat_imageView);
                title = view.findViewById(R.id.gchat_title);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(GroupchatActivity.this, DetailgchatActivity.class);
                        intent.putExtra("title", list.get(getAdapterPosition()).getGchat_title());
                        intent.putExtra("gchatId", list.get(getAdapterPosition()).getGchatId());
                        startActivityForResult(intent, 102);
                    }
                });
            }

            void onBind(VOGroupchat VOGroupchat){
                image.setImageResource(VOGroupchat.getGchat_image());
                title.setText(VOGroupchat.getGchat_title());
            }
        }
    }//그룹채팅 어뎁터 마지막
}
