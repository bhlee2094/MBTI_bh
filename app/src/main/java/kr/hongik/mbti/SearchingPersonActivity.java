package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SearchingPersonActivity extends AppCompatActivity {

    private TextView textView_memberinfo;
    private Button btn_send_friend_request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_person);

        Intent intent = getIntent();
        MemberInfo memberInfo = (MemberInfo) intent.getSerializableExtra("MemberInfo");
        String otherUserNum =intent.getStringExtra("otherUserNum");
        textView_memberinfo = (TextView) findViewById(R.id.sp_searchingPerson);
        textView_memberinfo.setText("닉네임 : " + memberInfo.getNickname() + "\n\n성별 : " + memberInfo.getGender() + "\n\n나이 : " + memberInfo.getAge() + "\n\nmbti : " + memberInfo.getMbti() + "\n\n주소 : " + memberInfo.getAddress() + "\n\n상태메시지 : " + memberInfo.getStateMessage());

        btn_send_friend_request = findViewById(R.id.btn_send_friend_request);
        btn_send_friend_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendList friendList = new FriendList();
                friendList.sendFriendRequest(otherUserNum);
            }
        });

    }
}