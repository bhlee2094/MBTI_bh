package kr.hongik.mbti.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.hongik.mbti.MainActivity;
import kr.hongik.mbti.R;
import kr.hongik.mbti.UpdateActivity;

import static android.app.Activity.RESULT_OK;


public class MyinfoFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_myprofile, container, false);
        Button btn_update = (Button)root.findViewById(R.id.btn_update);
        btn_update.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).Myinfo();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_update:
            {
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                startActivityForResult(intent, 1000);
            }
        }
    }
}
