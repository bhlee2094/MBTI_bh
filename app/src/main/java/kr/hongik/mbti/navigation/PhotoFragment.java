package kr.hongik.mbti.navigation;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kr.hongik.mbti.MainActivity;
import kr.hongik.mbti.Photo;
import kr.hongik.mbti.PhotoItemDecoration;
import kr.hongik.mbti.R;


public class PhotoFragment extends Fragment implements View.OnClickListener {

    private RecyclerView photo_recyclerview;
    private ArrayList<Photo> photoArrayList;
    private List<String> uidLists;
    private PhotoAdapter photoAdapter;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_photo, container, false);

        Context context = root.getContext();

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://mbti-bd577.appspot.com/");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }

        photo_recyclerview = (RecyclerView) root.findViewById(R.id.photo_recyclerview);
        photo_recyclerview.setHasFixedSize(true);
        photoArrayList= new ArrayList<>();
        uidLists = new ArrayList<>();
        photoAdapter = new PhotoAdapter(photoArrayList, context);
        photo_recyclerview.setAdapter(photoAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(context,3); // 리사이클러뷰 레이아웃 변경
        photo_recyclerview.setLayoutManager(layoutManager);
        photo_recyclerview.addItemDecoration(new PhotoItemDecoration(photo_recyclerview.getContext()));

        database.getReference().child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photoArrayList.clear();
                uidLists.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                        Photo photo = ds.getValue(Photo.class);
                        String uidKey = ds.getKey();
                        photoArrayList.add(photo);
                        uidLists.add(uidKey);
                }
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

        private ArrayList<Photo> mDataSet = new ArrayList<>();
        private Context context;

        public PhotoAdapter(ArrayList<Photo> mDataSet, Context context){
            this.context = context;
            this.mDataSet = mDataSet;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, parent, false);
            PhotoViewHolder holder = new PhotoViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
            Picasso.get().load(mDataSet.get(position).imageurl).placeholder(R.drawable.ic_image).into(holder.photo);
        }

        @Override
        public int getItemCount(){
            return mDataSet.size();
        }



        public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
            private ImageView photo;

            public PhotoViewHolder(View view){
                super(view);
                photo = (ImageView)view.findViewById(R.id.photo_imageView);

                view.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MenuItem Delete = menu.add(Menu.NONE, R.id.menu_delete, 1 , "삭제");
                Delete.setOnMenuItemClickListener(onMenuItemClickListener);
            }
            private final MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_delete:
                            database.getReference().child("images").child(uidLists.get(getAdapterPosition())).removeValue();
                            storageRef.child("images/"+photoArrayList.get(getAdapterPosition()).photo_id).delete();
                            photoArrayList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), photoArrayList.size());

                    }
                    return true;
                }
            };
        }
    }// 사진 어뎁터 끝
}
