package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

import kr.hongik.mbti.databinding.ActivityUpdateBinding;
import kr.hongik.mbti.navigation.MyinfoFragment;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityUpdateBinding binding;

    final String TAG = UpdateActivity.class.getName();
    private ProfileImage profileImage;
    private Uri profileImageUri;
    private final int GET_GALLERY_IMAGE = 200;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final DocumentReference usersRef = db.collection("users").document(user.getUid());
    private final String myUid = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getCurrentProfile();
        profileImage = new ProfileImage(getApplicationContext(),myUid);
        profileImage.showProfileImage(binding.ImageForUpdate);

        binding.mbtiLink2.setOnClickListener(this);
        binding.btnUpdateButton.setOnClickListener(this);
        binding.ImageForUpdate.setOnClickListener(this);
    }


    private void profileUpdate2(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .update("nickname",((EditText)findViewById(R.id.u_nickname)).getText().toString(),
                        "age",((EditText)findViewById(R.id.u_age)).getText().toString(),
                        "mbti",((EditText)findViewById(R.id.u_mbti)).getText().toString(),
                        "address",((EditText)findViewById(R.id.u_address)).getText().toString(),
                        "stateMessage",((EditText)findViewById(R.id.u_stateMessage)).getText().toString());

        if(profileImageUri!=null) {
            profileImage.uploadProfileImage(profileImageUri);
            File localFile = new File(getRealPathFromURI(profileImageUri));
            profileImage.makeCacheProfileImage(localFile);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //imageview에 보여지는 image 변경
            profileImageUri = data.getData();
            binding.ImageForUpdate.setImageURI(profileImageUri);
        }
    }



    /**
     * document로부터 현재 프로필 정보를 가져오는 함수입니다.
     **/
    private void getCurrentProfile(){
        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                binding.uNickname.setText(document.getString("nickname"));
                                binding.uAge.setText(document.getString("age"));
                                binding.uMbti.setText(document.getString("mbti"));
                                binding.uAddress.setText(document.getString("address"));
                                binding.uStateMessage.setText(document.getString("stateMessage"));
                            } else {
                                Log.d(TAG, "getCurrentProfile: No such document");
                            }
                        } else {
                            Log.d(TAG, "getCurrentProfile: failed to get document");
                        }
                    }
                });

    }

    /**
     * Uri을 File path를 리턴하는 함수입니다.
     * @param contentUri
     * @return File path
     */
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));
        cursor.close();
        return path;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mbtiLink2 :
            {
                Uri uri = Uri.parse("https://www.16personalities.com/ko/%EB%AC%B4%EB%A3%8C-%EC%84%B1%EA%B2%A9-%EC%9C%A0%ED%98%95-%EA%B2%80%EC%82%AC");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            }
            case R.id.btn_updateButton :
            {
                profileUpdate2();
                myStartActivity(MainActivity.class);
                break;
            }
            //binding.ImageForUpdate 클릭시 프로필이미지 재업로드 가능
            case R.id.ImageForUpdate :
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
                break;
            }
        }
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}