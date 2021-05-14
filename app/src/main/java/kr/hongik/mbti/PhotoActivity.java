package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private ImageView photo_ImageView;
    private String imageUrl, imageKey, imageId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);


        Intent intent = getIntent();
        imageId = intent.getStringExtra("photoid");
        imageKey = intent.getStringExtra("photokey");
        imageUrl = intent.getStringExtra("photourl");
        photo_ImageView = (ImageView)findViewById(R.id.photo_imageView);
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_image).into(photo_ImageView);
        /*FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://mbti-bd577.appspot.com/");
        storageReference.child("images/" + imageId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.ic_image).into(photo_ImageView);
            }
        });*/

    }
}