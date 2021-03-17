package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class BoardActivity extends AppCompatActivity {

    private TextView b_title;
    private TextView b_content;
    private TextView b_up;
    private TextView b_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        Intent intent = getIntent();

        b_title = findViewById(R.id.mboard_title);
        b_content = findViewById(R.id.mboard_content);
        b_up = findViewById(R.id.mboard_up);
        b_comment = findViewById(R.id.mboard_comment);

        b_title.setText(intent.getStringExtra("title"));
        b_content.setText(intent.getStringExtra("content"));
        b_up.setText(intent.getStringExtra("up"));
        b_comment.setText(intent.getStringExtra("comment"));
    }
}