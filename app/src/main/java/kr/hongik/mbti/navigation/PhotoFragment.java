package kr.hongik.mbti.navigation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import kr.hongik.mbti.Board;
import kr.hongik.mbti.MainActivity;
import kr.hongik.mbti.Photo;
import kr.hongik.mbti.PostActivity;
import kr.hongik.mbti.R;

import static android.app.Activity.RESULT_OK;

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private RecyclerView photo_recyclerview;
    private ArrayList<Photo> list;
    private PhotoAdapter photoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_photo, container, false);

        Context context = root.getContext();

        list = new ArrayList<>();

        photo_recyclerview = (RecyclerView) root.findViewById(R.id.photo_recyclerview);
        photo_recyclerview.setHasFixedSize(true);
        photo_recyclerview.setLayoutManager(new LinearLayoutManager(context));
        photo_recyclerview.addItemDecoration(new DividerItemDecoration(photo_recyclerview.getContext(),1));

        photoAdapter = new PhotoAdapter(getActivity(), list);
        photo_recyclerview.setAdapter(photoAdapter);

        Button btnPhoto = (Button)root.findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPhoto :
            {
                ((MainActivity) getActivity()).selectPhoto();

                break;
            }
        }
    }

    public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {// 사진 어뎁터

        private ArrayList<Photo> arrayList;
        private Context context;

        public PhotoAdapter(Context context, ArrayList<Photo> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            PhotoViewHolder holder = new PhotoViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            Photo photo = arrayList.get(position);
            holder.photo.setImageResource(photo.getPhoto_image());
        }

        @Override
        public int getItemCount(){
            return arrayList.size();
        }

        public class PhotoViewHolder extends RecyclerView.ViewHolder{
            private ImageView photo;

            public PhotoViewHolder(View view){
                super(view);
                photo = view.findViewById(R.id.photo_imageView);
            }
        }
    }// 사진 어뎁터 끝
}
