package kr.hongik.mbti;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 친구 목록 클래스. 친구목록과 관련있는 함수 구현함.
 * @author 장혜리
 **/

// TODO: firebase의 데이터 업로드 다운로드 매우 느림. 추후 Cache 저장 구현할 것.
// TODO: 클릭시 유저 프로필 띄워지는 기능 추가할 것.

public class FriendList{

    final static String TAG = FriendList.class.getName();
    final static Map<String, Object> emptyObject = new HashMap<>();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String UserNum = user.getUid();

    public ArrayList<FriendVO> Friends = new ArrayList<FriendVO>();
    public ArrayList<FriendVO> friendRequets = new ArrayList<FriendVO>();
    public ArrayList<FriendVO> sentFriendRequests = new ArrayList<FriendVO>();

    public FriendList(){

    }

    /**
     * 파이어베이스에서 친구목록을 가져온다.
     * @param adapter
     */
    public void getFriends(FriendListAdapter adapter ){
        CollectionReference colRef = db.collection("friendList/"+user.getUid()+"/friends");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserNum =document.getId();
                        getUserinfo(friendUserNum, Friends,adapter);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    /**
     * 파이어베이스에서 내게 온 친구신청을 가져온다.
     * @param adapter
     */
    public void getFriendRequets(FriendListAdapter adapter ){
        CollectionReference colRef = db.collection("friendList/"+user.getUid()+"/friendRequets");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String friendUserNum =document.getId();
                        getUserinfo(friendUserNum, Friends,adapter);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        Log.d(TAG, "getFriendRequets: 친구 목록을 가져왔습니다.");
    }


    /**
     * 친구의 userNum으로 친구 정보를 가져오는 함수
     * @param userNum
     * @param myFriendList
     * @param adapter
     */
    public void getUserinfo(String userNum, ArrayList<FriendVO> myFriendList , FriendListAdapter adapter ){
        DocumentReference docRef = db.document("users/"+userNum);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        myFriendList.add(new FriendVO(userNum, document.getString("nickname"), document.getString("stateMessage"), document.getString("mbti")));
                        adapter.notifyDataSetChanged();
                        Log.d(TAG, "current Friendlist size : "+ myFriendList.size());
                    }
                }
            }
        });
    }


    /**
     * otherUserNum에게 친구신청한다.
     * @param otherUserNum
     */
    public void sendFriendRequest(String otherUserNum){

        if(otherUserNum == UserNum)
            return;

        //내가 친구 신청한 목록(sentFriendRequests)에 추가
        CollectionReference colRef = db.collection("friendList/"+ UserNum +"/sentFriendRequests");
        colRef.document(otherUserNum).set(emptyObject);

        //상대방 친구 요청 목록(friendingList)에 나 추가
        CollectionReference  colRef2 = db.collection("friendList/"+otherUserNum+"/friendRequets");
        colRef2.document(UserNum).set(emptyObject);
    }

    /**
     * otherUserNum의 친구신청 수락
     * @param otherUserNum
     */
    public void acceptFriendRequest(String otherUserNum){

        if(otherUserNum == UserNum)
            return;

        //내 친구  목록(friends)에 상대방 추가
        CollectionReference colRef = db.collection("friendList/"+ UserNum +"/friends");
        colRef.document(otherUserNum).set(emptyObject);

        colRef = db.collection("friendList/"+ UserNum +"/friendRequets");
        colRef.document(otherUserNum).delete();

        //상대방 친구 목록에 나 추가
        CollectionReference  colRef2 = db.collection("friendList/"+otherUserNum+"/friends");
        colRef2.document(UserNum).set(emptyObject);

        colRef2 = db.collection("friendList/"+otherUserNum+"/sentFriendRequests");
        colRef2.document(UserNum).delete();


    }

    //테스트용 함수
    public void ForTest(String OtherUserNum){
        CollectionReference colRef = db.collection("friendList/"+ UserNum +"/friendRequets");
        colRef.document(OtherUserNum).set(emptyObject);
    }

}
