package kr.hongik.mbti;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import kr.hongik.mbti.databinding.FragmentFriendsBinding;


public class FriendsFragment extends Fragment {

    FriendListAdapter adapter;
    ListView lv_friends;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        lv_friends = (ListView) v.findViewById(R.id.lv_friends);


        FriendList friendList = new FriendList();
        adapter = new FriendListAdapter(getActivity(), 0, friendList.Friends);

        //친구목록 가져오기
        friendList.getFriends(adapter);

        lv_friends.setAdapter(adapter);

        return v;

    }


}