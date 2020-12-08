package kr.hongik.mbti;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * FriendList를 위한 Adapter class
 * @author 장혜리
 **/
public class FriendListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendVO> Data;

    public FriendListAdapter(Context context, int textViewResourceId, ArrayList<FriendVO> list) {
        this.context = context;
        this.Data = list;
    }

    public String getUserNum(int position) { // position번째 항목의 id인데 보통 position
        return Data.get(position).getUserNum();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return Data.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id인데 보통 position
        return position;
    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return Data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        ProfileImage profileImage = new ProfileImage(context, Data.get(position).getUserNum());

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.simple_user_info, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_profileImage = (ImageView) convertView.findViewById(R.id.profileImage);
            viewHolder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            viewHolder.tv_state_message = (TextView) convertView.findViewById(R.id.tv_state_message);
            viewHolder.btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //setData
        viewHolder.tv_nickname.setText(Data.get(position).getName());
        viewHolder.tv_state_message.setText(Data.get(position).getStateMessage());
        profileImage.showProfileImage(viewHolder.iv_profileImage);

        //FriendRequestFragment에서만 [수락] 버튼 보이게 하기 위한 설정
        if (parent.getTransitionName() != null)
        {viewHolder.btn_accept.setVisibility(View.VISIBLE);}
        else
            viewHolder.btn_accept.setVisibility(View.GONE);


        viewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendList friendList = new FriendList();
                friendList.acceptFriendRequest(Data.get(position).getUserNum());
            }
        });


        View bodyView = convertView.findViewById(R.id.body);

        bodyView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getUserinfo(position);
            }
        });


        return convertView;

    }
    
    private class ViewHolder {
        ImageView iv_profileImage;
        TextView tv_nickname;
        TextView tv_state_message;
        Button btn_accept;
    }


    void getUserinfo(int position)
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference dr = db.collection("users").document(getUserNum(position));

        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String mp_nickname = document.getString("nickname");
                        String mp_gender = document.getString("gender");
                        String mp_age = document.getString("age");
                        String mp_mbti = document.getString("mbti");
                        String mp_address = document.getString("address");
                        String mp_stateMessage = document.getString("stateMessage");

                        MemberInfo m = new MemberInfo(mp_nickname, mp_gender, mp_age, mp_mbti, mp_address, mp_stateMessage);

                        Intent intent = new Intent(context, SearchingPersonActivity.class);
                        intent.putExtra("MemberInfo", m);
                        intent.putExtra("otherUserNum", document.getId());
                        context.startActivity(intent);
                    }
                } else {
                }
            }
        });

        /*
        ColRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String mp_nickname = document.getString("nickname");
                                String mp_gender = document.getString("gender");
                                String mp_age = document.getString("age");
                                String mp_mbti = document.getString("mbti");
                                String mp_address = document.getString("address");
                                String mp_stateMessage = document.getString("stateMessage");

                                MemberInfo m = new MemberInfo(mp_nickname, mp_gender, mp_age, mp_mbti, mp_address, mp_stateMessage);

                                Intent intent = new Intent(getActivity(), SearchingPersonActivity.class);
                                intent.putExtra("MemberInfo", m);
                                intent.putExtra("otherUserNum", document.getId());
                                startActivity(intent);
                            }
                        } else { }
                    }
                });


         */
    }


}
