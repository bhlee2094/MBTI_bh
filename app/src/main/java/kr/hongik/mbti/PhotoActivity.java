package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private ImageView photo_ImageView;
    private String imageUrl, imageKey, imageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();
        imageUrl = intent.getStringExtra("photourl");
        photo_ImageView = findViewById(R.id.photo_ImageView);
        Picasso.get().load(imageUrl).into(photo_ImageView);

    }
}