package kr.hongik.mbti;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import kr.hongik.mbti.navigation.BoardFragment;
import kr.hongik.mbti.navigation.FriendlistFragment;
import kr.hongik.mbti.navigation.MyinfoFragment;
import kr.hongik.mbti.navigation.PhotoFragment;
import kr.hongik.mbti.navigation.SearchFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int FROM_PHOTO = 10;
    private static final int BOARD = 101;
    private FirebaseStorage storage;
    private  StorageReference storageRef;
    private String photoPath;
    private FirebaseDatabase database;

    PhotoFragment photo_fragment;
    SearchFragment search_fragment;
    FriendlistFragment friendlist_fragment;
    BoardFragment board_fragment;
    MyinfoFragment myinfo_fragment;
    FirebaseAuth mfirebaseAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference usersRef = db.collection("users").document(user.getUid());

    private String myUid = user.getUid();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
            dlg.setTitle("로그아웃"); //제목
            dlg.setMessage("로그아웃 하시겠습니까?"); // 메시지
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    mfirebaseAuth = FirebaseAuth.getInstance();
                    logout(mfirebaseAuth);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startToast("취소되었습니다.");
                }
            });
            dlg.show();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        friendlist_fragment = new FriendlistFragment();
        photo_fragment = new PhotoFragment();
        search_fragment = new SearchFragment();
        board_fragment = new BoardFragment();
        myinfo_fragment = new MyinfoFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, photo_fragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.action_board:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, board_fragment).commit();
                                return true;
                            case R.id.action_myinfo:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, myinfo_fragment).commit();
                                return true;
                            case R.id.action_friendlist:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, friendlist_fragment).commit();
                                return true;
                            case R.id.action_search:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, search_fragment).commit();
                                return true;
                            case R.id.action_photo:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, photo_fragment).commit();
                                return true;
                        }
                        return false;
                    }
                }
        );

        }

        private void init(){
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {
                myStartActivity(LoginActivity.class);
            }
            else{
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
                documentReference.get().addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                } else {
                                    Log.d(TAG, "No such document");
                                    myStartActivity(JoinActivity.class);

                                }
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                );
            }
        }

    public void Myinfo() {
        TextView my_nickname = (TextView)findViewById(R.id.my_nickname);
        TextView my_gender = (TextView)findViewById(R.id.my_gender);
        TextView my_age = (TextView)findViewById(R.id.my_age);
        TextView my_mbti = (TextView)findViewById(R.id.my_mbti);
        TextView my_address = (TextView)findViewById(R.id.my_address);
        TextView my_stateMessage = (TextView)findViewById(R.id.my_stateMessage);
        ImageView my_profile = findViewById(R.id.my_profile);

        //프로필 이미지
        ProfileImage profileImage = new ProfileImage(getApplicationContext(),myUid);
        profileImage.showProfileImage(my_profile);
        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                my_nickname.setText(document.getString("nickname"));
                                my_gender.setText(document.getString("gender"));
                                my_age.setText(document.getString("age"));
                                my_mbti.setText(document.getString("mbti"));
                                my_address.setText(document.getString("address"));
                                my_stateMessage.setText(document.getString("stateMessage"));

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     *     Firebase 로그아웃
     *      @author 장혜리
     */
    public void logout(FirebaseAuth mFirebaseAuth) {

        if (mFirebaseAuth.getCurrentUser() != null) {

            Toast.makeText(MainActivity.this, mFirebaseAuth.getUid() + "님이 로그아웃하셨습니다", Toast.LENGTH_SHORT).show();
            mFirebaseAuth.signOut();

        }
    }

    public void selectPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, FROM_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_OK){
            return;
        }

        switch (requestCode){
            case FROM_PHOTO:{
                photoPath = getRealPathFromURI(data.getData());
                upload(photoPath);
                break;
            }
            case BOARD:{
                String mtitle = data.getStringExtra("title");
                String mcontent = data.getStringExtra("content");
                startToast(mtitle + mcontent);

                break;
            }
        }
    }

    private String getRealPathFromURI(Uri uri) {//사진 절대경로

        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
    }

    private void upload(String uri){
        storageRef = storage.getReferenceFromUrl("gs://mbti-bd577.appspot.com/");
        Uri file = Uri.fromFile(new File(uri));
        StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    Photo photo = new Photo();
                    photo.imageurl = downloadUri.toString();
                    database.getReference().child("images").push().setValue(photo);
                }
            }
        });
    }


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

}