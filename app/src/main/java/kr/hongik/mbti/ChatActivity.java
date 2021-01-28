package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    EditText editText;
    Button btnFinish, btnSend;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    Calendar c =Calendar.getInstance();
    ArrayList<Chat> chatArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        editText = (EditText) findViewById(R.id.edittext);
        chatArrayList = new ArrayList<>();

        btnFinish = (Button) findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener((v) -> {finish();});

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true); //높이 고정

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] myDataset = {"test1", "test2"};
        mAdapter = new chatAdapter(chatArrayList);
        recyclerView.setAdapter(mAdapter);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Chat chat = dataSnapshot.getValue(Chat.class);
                String stText = chat.getText();
                chatArrayList.add(chat);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                Chat chat = dataSnapshot.getValue(Chat.class);
                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                Comment movedComment = dataSnapshot.getValue(Comment.class);
                String commentKey = dataSnapshot.getKey();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ChatActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("message");
        ref.addChildEventListener(childEventListener);

        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stText = editText.getText().toString();
                Toast.makeText(ChatActivity.this, stText, Toast.LENGTH_LONG).show();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String datetime = dateFormat.format(c.getTime());
                DatabaseReference myRef = database.getReference("message").child(datetime);

                Hashtable<String, String> numbers
                        = new Hashtable<String, String>();
                numbers.put("text", stText);

                myRef.setValue(numbers);
            }
        });

    }
}