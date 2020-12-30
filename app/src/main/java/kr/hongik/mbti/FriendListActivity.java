package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * FriendListActivity는 FriendsFragment,FriendRequetsFragment, 총두개의 Fragment로 구성됩니다.
 * @author 장혜리
 **/

public class FriendListActivity extends AppCompatActivity {

    final String TAG = FriendListActivity.class.getName();

    Button btn_my_friend_list;

    final Map<String, Object> emptyObject = new HashMap<>();

    private boolean isFreindsFragment = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, new FriendsFragment());
        fragmentTransaction.commit();

        btn_my_friend_list= findViewById(R.id.btn_my_friend_list);
        btn_my_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new FriendsFragment());
            }
        });

        btn_my_friend_list= findViewById(R.id.btn_friending);
        btn_my_friend_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new FriendRequetsFragment() );
            }
        });

    }


    /**
     * fragment 전환해주는 함수
     * @param fr
     */
    public void switchFragment(Fragment fr) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fr);
        fragmentTransaction.commit();
    }


}

