package kr.hongik.mbti.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.hongik.mbti.MainActivity;
import kr.hongik.mbti.R;
import kr.hongik.mbti.UpdateActivity;
import kr.hongik.mbti.databinding.FragmentMyprofileBinding;

public class MyinfoFragment extends Fragment implements View.OnClickListener {

    public FragmentMyprofileBinding fragmentMyprofileBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        fragmentMyprofileBinding = FragmentMyprofileBinding.inflate(inflater, container, false);
        fragmentMyprofileBinding.btnUpdate.setOnClickListener(this);
        View view = fragmentMyprofileBinding.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).Myinfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentMyprofileBinding = null;
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
