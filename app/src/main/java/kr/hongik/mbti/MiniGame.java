package kr.hongik.mbti;

public class MiniGame {
    private String myUid;
    private String friendUid;

    public MiniGame(String myUid, String friendUid) {
        this.myUid = myUid;
        this.friendUid = friendUid;
    }

    public String getMyUid() {
        return myUid;
    }

    public void setMyUid(String myUid) {
        this.myUid = myUid;
    }

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }
}
