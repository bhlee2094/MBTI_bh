package kr.hongik.mbti;

import androidx.annotation.NonNull;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * profileImage를 처리하기 위한 클래스. (Model)
 * 프로필 이미지는 프로필 뿐만 아니라 채팅 등 여러 뷰에서 사용되는 만큼 profile과 따로 분리하여 작성하였습니다.
 * NOTE: 화면 업데이트는 Model에 있을 기능이 아님. 추후 수정할 것.
 * @author 장혜리
 **/

public class ProfileImage {


    private StorageReference mStorageRef;
    final String TAG = ProfileImage.class.getName();
    File cacheDir;
    File cacheFile;
    String userNum;
    Context context;
    ImageView tempiv;

    /**
     *
     * @param cacheDir
     * @param userNum 이 이름으로 cache파일이 생성됩니다.
     */
    public ProfileImage(Context context, String userNum){
        this.context = context;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        cacheDir = context.getCacheDir();
        this.userNum=userNum;
        cacheFile = new File(cacheDir.getAbsolutePath(), userNum + ".jpeg");
    }

    /**
     * ProfileImage를 ImageView에 보여줌
     *  cache가 있으면 cache 파일 사용
     *  cache가 없으면 firebase Strorage에서 다운로드
     * @param iv
     */
    public void showProfileImage(ImageView iv)
    {
        if((cacheFile.exists()))
        {
            setProfileImageCache(iv);
        }
        else {
            tempiv = iv;
            downloadProfileImage(userNum);
        }


    }

    /**
     * firebase Storage에 profileImage를 업로드 합니다.
     * @param profileImageUri 이미지 URI
     */
    public void uploadProfileImage(Uri profileImageUri) {

        int compressQuality = 20;

        //Uri to bitmap
        try {

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), profileImageUri);
                //bitmap to cacheFile and compress
                FileOutputStream filestream = null;
                try {
                    filestream = new FileOutputStream(cacheFile);
                    //quality : 숫자가 작을수록 압축률 높음
                    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, filestream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "uploaded file size: " + cacheFile.length()/1000+"kb");

                StorageReference imageRef = mStorageRef.child("profileImages/" + userNum + ".jpeg");

                imageRef.putFile(Uri.fromFile(cacheFile))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "uploadProfileImage :Success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.d(TAG, "uploadProfileImage : Fail");
                            }
                        });
            } catch (FileNotFoundException ex) {
                // insert code to run when exception occurs
            }

        } catch (IOException ioe) {

        }


    }

    /**
     * firebase Storage에서 profileImage 다운로드 후 cache를 생성합니다.
     * 모든 이미지 파일은 jpeg파일이라고 가정
     * @param userNum 이 유저의 프로필을 가져옵니다.
     *
     */
    public boolean downloadProfileImage(String userNum){
        try {
            File localFile = File.createTempFile("Temp", "");
            StorageReference imageRef = mStorageRef.child("profileImages/" + userNum + ".jpeg");
            imageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //다운받은 파일(local) cache로 저장
                            makeCacheProfileImage(localFile);
                            setProfileImageCache(tempiv);
                            Log.d(TAG, "onSuccess: profile 다운로드 후 cache 저장");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, this.getClass().getEnclosingMethod().getName() + ":Fail");
                    // Handle failed download
                    // ...

                }
            });

        }
        catch (IOException e){
            e.printStackTrace();
        }

        if(cacheFile.exists())
            return true;
        else
            return false;
    }


    /**
     * local File로 cacheFile을 만듭니다.
     * @param localFile
     */
    public void makeCacheProfileImage(File localFile){
        Log.d(TAG, "makeCache localFile path: "+localFile.getAbsolutePath());
        try {
            cacheFile.delete();
            copyFile(localFile, cacheFile);
            Log.d(TAG, "makeCache: Success:" + cacheFile.getName());
        }
        catch(FileNotFoundException fnfe)
        {
            Log.d(TAG, "makeCache : FileNotFoundException");
            fnfe.printStackTrace();
        }
        catch(IOException ie)
        {
            Log.d(TAG, "makeCache : IOException");
            ie.printStackTrace();
        }
    }

    /**
     * src파일을 dst로 복사합니다.
     * @param src
     * @param dst
     * @throws IOException
     */
    public void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst,false);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    /**
     * iv에 ProFileImageCache를 setting합니다.
     * @param iv
     */
    public void setProfileImageCache(ImageView iv){
        if(this.cacheFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            iv.setImageBitmap(bitmap);
            Log.d(TAG, "setBitmapToImageView: Success");
        }
        else
        Log.d(TAG, "setBitmapToImageView: no cache File");

    }


}
