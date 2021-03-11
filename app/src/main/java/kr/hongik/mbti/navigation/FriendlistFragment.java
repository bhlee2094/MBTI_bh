package kr.hongik.mbti.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import kr.hongik.mbti.FriendListActivity;
import kr.hongik.mbti.MatchingActivity;
import kr.hongik.mbti.R;

public class FriendlistFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_friendlist, container, false);
        Button btn1 = (Button)root.findViewById(R.id.btn1);
        Button btn3 = (Button)root.findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn3.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn1:
            {
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn3:
            {
                Intent intent = new Intent(getActivity(), MatchingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
