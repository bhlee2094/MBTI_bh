package kr.hongik.mbti.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import kr.hongik.mbti.MatchingActivity;
import kr.hongik.mbti.MemberInfo;
import kr.hongik.mbti.R;
import kr.hongik.mbti.SearchingPersonActivity;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "SearchingActivity";
    String mp_nickname, mp_gender, mp_age, mp_mbti, mp_address, mp_stateMessage, ck_gender, mp_gender2, mp_userNum;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_searching, container, false);
        Button btn_searchingOption = (Button)root.findViewById(R.id.btn_searchingOption);
        Button btn3 = (Button)root.findViewById(R.id.btn_matchingfriend2);
        btn3.setOnClickListener(this);
        btn_searchingOption.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_searchingOption:
            {
                searchingAlgorithm();
                break;
            }
            case R.id.btn_matchingfriend2:
            {
                Intent intent = new Intent(getActivity(), MatchingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void searchingAlgorithm() {
        usersRef.document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ck_gender = documentSnapshot.getString(("gender"));

                if (ck_gender.equals("남자")) {
                    mp_gender2 = "여자";
                } else mp_gender2 = "남자";

            }
        });

        String m_mbti = ((EditText) getView().findViewById(R.id.m_mbti)).getText().toString();
        String m_minage = ((EditText) getView().findViewById(R.id.m_minage)).getText().toString();
        String m_maxage = ((EditText) getView().findViewById(R.id.m_maxage)).getText().toString();

        if (m_mbti.length() > 0 && m_minage.length() > 0 && m_maxage.length() > 0) {
            usersRef.whereEqualTo("gender", mp_gender2)
                    .whereEqualTo("mbti", m_mbti)
                    .whereGreaterThanOrEqualTo("age", m_minage)
                    .whereLessThanOrEqualTo("age", m_maxage)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    mp_nickname = document.getString("nickname");
                                    mp_gender = document.getString("gender");
                                    mp_age = document.getString("age");
                                    mp_mbti = document.getString("mbti");
                                    mp_address = document.getString("address");
                                    mp_stateMessage = document.getString("stateMessage");
                                    mp_userNum = document.getString("userNum");

                                    MemberInfo m = new MemberInfo(mp_nickname, mp_gender, mp_age, mp_mbti, mp_address, mp_stateMessage, mp_userNum);

                                    Intent intent = new Intent(getActivity(), SearchingPersonActivity.class);
                                    intent.putExtra("MemberInfo", m);
                                    intent.putExtra("otherUserNum", document.getId());
                                    startActivity(intent);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else {

        }
    }
}
