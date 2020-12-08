package kr.hongik.mbti;


/**
 * Freind value object
 * @author 장혜리
 **/
public class FriendVO {

    private String userNum;
    private String name; //name
    private String stateMessage; //statemessage
    private String mbti; //mbti

    public FriendVO() {
    }

    public FriendVO(String userNum, String name, String stateMessage, String mbti) {
        this.userNum = userNum;
        this.name = name;
        this.stateMessage = stateMessage;
        this.mbti = mbti;
    }

    public String getUserNum() {
        return userNum;
    }

    public String getName() {
        return name;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public String getMbti() {
        return mbti;
    }
}