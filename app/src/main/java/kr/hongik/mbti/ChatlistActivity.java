package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatlistActivity extends AppCompatActivity {

    ListView listView;
    ChatlistAdapter chatlistAdapter;
    ArrayList<MemberInfo> memberInfoArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        listView = (ListView) findViewById(R.id.Chatlist);

        memberInfoArrayList = new ArrayList<MemberInfo>(); //arrayList 객체 생성

        /*memberInfoArrayList.add(
                new MemberInfo()
        );*/
        chatlistAdapter = new ChatlistAdapter(ChatlistActivity.this,memberInfoArrayList);
        listView.setAdapter(chatlistAdapter);
    }
}