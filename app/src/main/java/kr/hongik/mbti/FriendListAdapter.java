package kr.hongik.mbti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


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

        ProfileImage profileImage = new ProfileImage(context.getCacheDir(), Data.get(position).getUserNum());

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

        return convertView;

    }
    
    private class ViewHolder {
        ImageView iv_profileImage;
        TextView tv_nickname;
        TextView tv_state_message;
        Button btn_accept;
    }
}
