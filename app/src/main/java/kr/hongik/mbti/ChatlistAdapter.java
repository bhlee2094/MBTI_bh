package kr.hongik.mbti;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatlistAdapter extends BaseAdapter {
    Context context;
    ArrayList<MemberInfo> memberInfoArrayList;

    ImageView profileView;
    TextView nicknameView;

    public ChatlistAdapter(Context context, ArrayList<MemberInfo> memberInfoArrayList) {
        this.context = context;
        this.memberInfoArrayList = memberInfoArrayList;
    }

    @Override
    public int getCount() {//리스트뷰가 몇개의 아이템을 가지고 있는지
        return this.memberInfoArrayList.size();
    }

    @Override
    public Object getItem(int position) {//현재 어떤 아이템인지 알려주는 부분
        return this.memberInfoArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {//어떤 포지션인지 알려주는 부분
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//아이템과 xml을 연결하여 화면에 표시
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
            nicknameView = (TextView) convertView.findViewById(R.id.nicknameTextView);
            profileView = (ImageView) convertView.findViewById(R.id.imageView);
            nicknameView.setText(memberInfoArrayList.get(position).getNickname());
            //profileView.setImageResource(memberInfoArrayList.get(position).getProfile_image());
        }
        return convertView;
    }
}
