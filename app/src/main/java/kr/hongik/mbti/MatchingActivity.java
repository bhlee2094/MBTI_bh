package kr.hongik.mbti;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MatchingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
    }

    private void matchingAlgorithm(){
        int match[][] = new int[4][4];
        int[][] man = {{1,2,3,4}, {1,3,4,2}, {2,1,4,3}, {2,4,1,3}};
        int[][] woman = {{3,4,1,2}, {2,4,1,3}, {4,2,1,3}, {3,1,2,4}};

        //첫번째 라운드
        int round=0;
        for(int i=0;i<woman.length;i++){
            for(int j=0;j<woman[i].length;j++){
                if(man[woman[i][j]-1][round]==i+1){
                    match[woman[i][j]-1][i]=1;
                    break;
                }
            }
        }

        //두번째 라운드
        round=1;
        for(int i=0;i<match.length;i++){
            for(int j=0;j<match[i].length;j++){
                if(match[i][j]==1) break;

                if(j==match[i].length-1){
                    Loop1 : for(int k=0;k<match.length;k++){
                        if(match[k][man[i][round]-1]!=0){
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

        //세번째 라운드
        round=2;
        for(int i=0;i<match.length;i++){
            for(int j=0;j<match[i].length;j++){
                if(match[i][j]==1) break;

                if(j==match[i].length-1){
                    Loop1 : for(int k=0;k<match.length;k++){
                        if(match[k][man[i][round]-1]!=0){
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

        //네번째 라운드
        round=3;
        for(int i=0;i<match.length;i++){
            for(int j=0;j<match[i].length;j++){
                if(match[i][j]==1) break;

                if(j==match[i].length-1){
                    Loop1 : for(int k=0;k<match.length;k++){
                        if(match[k][man[i][round]-1]!=0){
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
                System.out.print("남자:"+(i+1)+",여자:"+(j+1)+" = "+match[i][j]+" /");
            }
            System.out.println();
        }
    }
}