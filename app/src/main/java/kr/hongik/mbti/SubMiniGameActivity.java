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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SubMiniGameActivity extends AppCompatActivity {

    private TextView mini_TextView;
    private EditText editText;
    private Button button;
    private String myUid, friendUid;
    private int[] RandomNum;
    private int count = 0;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_mini_game);

        Intent intent = getIntent();
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendUid = intent.getStringExtra("friendUid");

        db = FirebaseFirestore.getInstance();
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
        db.collection("miniGame/"+myUid+"/miniGame").document(friendUid).set(randomMap);

        editText = (EditText) findViewById(R.id.mini_EditText);
        mini_TextView = (TextView) findViewById(R.id.mini_TextView);
        mini_TextView.setText("");
        button = (Button) findViewById(R.id.btnSubmit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().length()>3){
                    startToast("3개 숫자만 입력하세요");
                }
                else{
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    db.collection("miniGame/"+myUid+"/miniGame").document(friendUid).get()
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
                                                    if(Integer.parseInt(String.valueOf(editText.getText().charAt(i)))==RandomNum[j]){
                                                        if(i==j) strike++;
                                                        else ball++;
                                                    }
                                                }
                                            }
                                            if(strike==3) {
                                                mini_TextView.append("Perfect!");
                                                Map<String, Integer> countMap = new HashMap<>();
                                                countMap.put("count", count);
                                                db.collection("miniGame/"+myUid+"/miniGame/"+friendUid+"/count").document().set(countMap);
                                            }
                                            else{
                                                mini_TextView.append(editText.getText().toString() + " Strike : " + strike + " Ball : " + ball + "\n");
                                                count++;
                                            }
                                            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                            editText.setText("");
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