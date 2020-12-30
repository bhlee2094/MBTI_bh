package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class JoinActivity extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE = 200;
    private ImageView imageview;
    private Button JoinButton, mbti_link;
    private static final String TAG = "JoinActivity";
    FirebaseAuth mfirebaseAuth;
    FirebaseUser currentUser;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mfirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mfirebaseAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        imageview = (ImageView)findViewById(R.id.Image);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        mbti_link = findViewById(R.id.mbtiLink);

        mbti_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        JoinButton = findViewById(R.id.JoinButton);

        JoinButton.setOnClickListener(mClickListener);


    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            profileUpdate();
            myStartActivity(MainActivity.class);
            finish();
        }

};


    private void profileUpdate(){

        String nickname = ((EditText)findViewById(R.id.nickname)).getText().toString();
        String gender = ((EditText)findViewById(R.id.gender)).getText().toString();
        String age = ((EditText)findViewById(R.id.age)).getText().toString();
        String mbti = ((EditText)findViewById(R.id.mbti)).getText().toString();
        String address = ((EditText)findViewById(R.id.address)).getText().toString();
        String stateMessage = ((EditText)findViewById(R.id.stateMessage)).getText().toString();

        if(nickname.length()>0 && gender.length()>0 && age.length()>0 && mbti.length()>0 && address.length()>0 && stateMessage.length()>0){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(nickname, gender, age, address, mbti, stateMessage);
            if(user !=null){
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록을 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록에 실패하였습니다.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }

        }
        else{
            startToast("회원정보를 입력해주세요.");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri profileImage = data.getData();
            imageview.setImageURI(profileImage);
            ProfileImage profile = new ProfileImage(getApplicationContext(),currentUser.getUid());
            profile.uploadProfileImage(profileImage);
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}