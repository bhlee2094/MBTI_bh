package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import kr.hongik.mbti.databinding.ActivityJoinBinding;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GET_GALLERY_IMAGE = 200;
    private ActivityJoinBinding binding;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.Image.setOnClickListener(this);
        binding.mbtiLink.setOnClickListener(this);
        binding.JoinButton.setOnClickListener(this);
    }

    private void profileUpdate(){

        String nickname = binding.nickname.getText().toString();
        String gender = binding.gender.getText().toString();
        String age = binding.age.getText().toString();
        String mbti = binding.mbti.getText().toString();
        String address = binding.address.getText().toString();
        String stateMessage = binding.stateMessage.getText().toString();

        if(nickname.length()>0 && gender.length()>0 && age.length()>0 && mbti.length()>0 && address.length()>0 && stateMessage.length()>0){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String UserNum = user.getUid();

            MemberInfo MemberInfo = new MemberInfo(nickname, gender, age, address, mbti, stateMessage, UserNum);
            if(user !=null){
                db.collection("users").document(user.getUid()).set(MemberInfo)
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
            binding.Image.setImageURI(profileImage);
            ProfileImage profile = new ProfileImage(getApplicationContext(),user.getUid());
            profile.uploadProfileImage(profileImage);
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Image :
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
            }
            case R.id.mbtiLink :
            {
                Uri uri = Uri.parse("https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
            case R.id.JoinButton :
            {
                profileUpdate();
                myStartActivity(MainActivity.class);
                finish();
                break;
            }
        }
    }
}