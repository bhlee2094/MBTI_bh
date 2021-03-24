package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MatchingActivity extends AppCompatActivity {

    private TextView roundTextView;
    TextView[] matchTV = new TextView[4];
    private Button button;
    static int round = 0;
    static int match[][] = new int[4][4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        matchTV[0] = (TextView) findViewById(R.id.match1);
        matchTV[1] = (TextView) findViewById(R.id.match2);
        matchTV[2] = (TextView) findViewById(R.id.match3);
        matchTV[3] = (TextView) findViewById(R.id.match4);
        roundTextView = (TextView) findViewById(R.id.roundTextView);
        button = (Button) findViewById(R.id.btn_round);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(round%4==0){// 라운드 0 -> 1
                    matchingAlgorithm();
                    round++;
                    roundTextView.setText("라운드"+round);
                }else if(round%4==1){// 라운드 1 -> 2
                    matchingAlgorithm();
                    round++;
                    roundTextView.setText("라운드"+round);
                }else if(round%4==2){// 라운드 2 -> 3
                    matchingAlgorithm();
                    round++;
                    roundTextView.setText("라운드"+round);
                }else{// 라운드 3 -> 4
                    matchingAlgorithm();
                    round++;
                    roundTextView.setText("라운드"+round);
                    button.setEnabled(false);
                }
            }
        });
    }

    private void matchingAlgorithm(){ // 게일-섀플리 알고리즘 나만의 방식으로 구현
        int[][] man = {{1,2,3,4}, {1,3,4,2}, {2,1,4,3}, {2,4,1,3}};
        int[][] woman = {{3,4,1,2}, {2,4,1,3}, {4,2,1,3}, {3,1,2,4}};

        //첫번째 라운드
        if(round==0){
            for(int i=0;i<woman.length;i++){
                for(int j=0;j<woman[i].length;j++){
                    if(man[woman[i][j]-1][round]==i+1){
                        match[woman[i][j]-1][i]=1;
                        break;
                    }
                }
            }
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(match[i][j]==1) {
                        matchTV[i].setText("여자" + (char)(j + 65));// 아스키코드 덧셈
                    }
                }
            }
        }
        //나머지 라운드
        else{
            for(int i=0;i<match.length;i++){
                for(int j=0;j<match[i].length;j++){
                    if(match[i][j]==1) break;

                    if(j==match[i].length-1){
                        Loop1 : for(int k=0;k<match.length;k++){
                            if(match[k][man[i][round]-1]!=0){// 다른 남자와 매칭이 되있는지 체크
                                for(int l=0;l< woman.length;l++){
                                    if(woman[man[i][round]-1][l]==k+1) break Loop1;
                                    if(woman[man[i][round]-1][l]==i+1){
                                        match[k][man[i][round]-1]=0;
                                        match[i][man[i][round]-1]=1;
                                        break Loop1;
                                    }
                                }
                            }
                            if(k==match.length-1){
                                match[i][man[i][round]-1]=1;
                            }
                        }
                    }
                }
            }
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(match[i][j]==1) {
                        matchTV[i].setText("여자" + (char)(j + 65));// 아스키코드 덧셈
                        break;
                    }else{
                        matchTV[i].setText("없음"+(i+1));
                    }
                }
            }
        }

    }
}