package kr.hongik.mbti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kr.hongik.mbti.databinding.ActivitySubMiniGameBinding;

public class SubMiniGameActivity extends AppCompatActivity {

    private ActivitySubMiniGameBinding binding;
    private int[] RandomNum;
    private int count = 0;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubMiniGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        RandomNum = new int[3];
        while(true){
            for(int i=0;i<3;i++){
                RandomNum[i] = (int)(Math.random()*10);
            }
            if(RandomNum[0]!=RandomNum[1] && RandomNum[1]!=RandomNum[2] && RandomNum[0]!=RandomNum[2]) break;
        }
        Map<String, Integer> randomMap = new HashMap<>();
        randomMap.put("num1", RandomNum[0]);
        randomMap.put("num2", RandomNum[1]);
        randomMap.put("num3", RandomNum[2]);
        db.collection("miniGame/"+user.getUid()+"/miniGame").document(user.getUid()).set(randomMap);

        binding.miniTextView.setText("중복없이 3개 숫자 입력해주세요\n");

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.miniEditText.getText().length()>3){
                    startToast("3개 숫자만 입력하세요");
                }
                else{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    db.collection("miniGame/"+user.getUid()+"/miniGame").document(user.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        if(documentSnapshot.exists()){
                                            int strike = 0;
                                            int ball = 0;
                                            for(int i=0;i<3;i++){
                                                for(int j=0;j<3;j++){
                                                    if(Integer.parseInt(String.valueOf(binding.miniEditText.getText().charAt(i)))==RandomNum[j]){
                                                        if(i==j) strike++;
                                                        else ball++;
                                                    }
                                                }
                                            }
                                            if(strike==3) {
                                                binding.miniTextView.append("Perfect!");
                                                Map<String, Integer> countMap = new HashMap<>();
                                                countMap.put("count", count);
                                            }
                                            else{
                                                binding.miniTextView.append(binding.miniEditText.getText().toString() + " Strike : " + strike + " Ball : " + ball + "\n");
                                                count++;
                                            }
                                            inputMethodManager.hideSoftInputFromWindow(binding.miniEditText.getWindowToken(), 0);
                                            binding.miniEditText.setText("");
                                        }
                                    }
                                }
                            });
                }

            }
        });
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}