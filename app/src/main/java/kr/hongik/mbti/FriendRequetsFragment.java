package kr.hongik.mbti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


/**
 * 나에게 온 친구 요청을 확인하고 수락하기 위한 Fragment
 * @author 장혜리
 **/
public class FriendRequetsFragment extends Fragment {

    final String TAG = FriendRequetsFragment.class.getName();
    FriendListAdapter adapter;
    ListView lv_friend_req;

    public FriendRequetsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v= inflater.inflate(R.layout.fragment_friending, container, false);
        lv_friend_req = (ListView) v.findViewById(R.id.lv_friend_req);

        FriendList friendList = new FriendList();
        adapter = new FriendListAdapter(getActivity(),0, friendList.Friends);

        //친구요청 가져오기
        friendList.getFriendRequets(adapter);

        lv_friend_req.setAdapter(adapter);
        return v;
    }
}