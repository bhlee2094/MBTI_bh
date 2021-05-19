package kr.hongik.mbti;


/**
 * Freind value object
 * @author 장혜리
 **/
public class Friend {

    private String userNum;
    private String name; //name
    private String stateMessage; //statemessage
    private String mbti; //mbti

    public Friend() {
    }

    public Friend(String userNum, String name, String stateMessage, String mbti) {
        this.userNum = userNum;
        this.name = name;
        this.stateMessage = stateMessage;
        this.mbti = mbti;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public String getMbti() {
        return mbti;
    }

    public void setMbti(String mbti) {
        this.mbti = mbti;
    }
}