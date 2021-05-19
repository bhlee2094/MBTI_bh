package kr.hongik.mbti;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupchatActivity extends AppCompatActivity {

    private GroupchatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        init();
        getData();
    }

    private void init(){//리사이클러뷰 초기화
        RecyclerView gchat_recyclerView = findViewById(R.id.gchat_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        gchat_recyclerView.setLayoutManager(layoutManager);
        gchat_recyclerView.setHasFixedSize(true);
        gchat_recyclerView.addItemDecoration(new ItemDecoration(gchat_recyclerView.getContext()));
        mAdapter = new GroupchatAdapter();
        gchat_recyclerView.setAdapter(mAdapter);
    }

    private void getData(){//그룹채팅방 데이터 입력
        List<Integer> listResId = Arrays.asList(R.drawable.enfj, R.drawable.enfp, R.drawable.entj, R.drawable.entp, R.drawable.esfj,R.drawable.esfp,R.drawable.estj,R.drawable.estp,R.drawable.infj,R.drawable.infp,R.drawable.intj,R.drawable.intp,R.drawable.isfj,R.drawable.isfp,R.drawable.istj,R.drawable.istp);
        List<String> listId = Arrays.asList("abcdefg1","abcdefg2","abcdefg3","abcdefg4","abcdefg5","abcdefg6","abcdefg7","abcdefg8","abcdefg9","abcdefg10","abcdefg11","abcdefg12","abcdefg13","abcdefg14","abcdefg15","abcdefg16");
        for(int i=0; i<listResId.size(); i++){
            Groupchat Groupchat = new Groupchat();
            Groupchat.setGchat_image(listResId.get(i));
            Groupchat.setGchatId(listId.get(i));

            mAdapter.addItem(Groupchat);
        }
        mAdapter.notifyDataSetChanged();
    }

    public class GroupchatAdapter extends RecyclerView.Adapter<GroupchatAdapter.GroupchatViewHolder>{//그룹채팅 어뎁터
        private ArrayList<Groupchat> list = new ArrayList<>();

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

        void addItem(Groupchat Groupchat){
            list.add(Groupchat);
        }

        public class GroupchatViewHolder extends RecyclerView.ViewHolder {
            private ImageView image;

            public GroupchatViewHolder (View view){
                super(view);
                image = view.findViewById(R.id.gchat_imageView);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(GroupchatActivity.this, DetailgchatActivity.class);
                        intent.putExtra("gchatId", list.get(getAdapterPosition()).getGchatId());
                        startActivityForResult(intent, 102);
                    }
                });
            }

            void onBind(Groupchat Groupchat){
                image.setImageResource(Groupchat.getGchat_image());
            }
        }
    }//그룹채팅 어뎁터 마지막
}
