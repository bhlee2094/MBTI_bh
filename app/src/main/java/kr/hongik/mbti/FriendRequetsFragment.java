package kr.hongik.mbti;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


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

        friendList.makeSampleData(null);

        //친구요청 가져오기
        friendList.getFriendRequets(adapter);

        lv_friend_req.setAdapter(adapter);


        return v;
    }

}