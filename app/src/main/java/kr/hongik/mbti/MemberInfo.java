package kr.hongik.mbti;

import java.io.Serializable;

public class MemberInfo  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int profile_image;
    private String nickname;
    private String gender;
    private String age;
    private String address;
    private String mbti;
    private String stateMessage;

    public static String userNum;
    public static String post = "post";

    public static String title = "title";
    public static String contents = "contents";

    public MemberInfo(String nickname, String gender, String age, String address, String mbti, String stateMessage, String userNum){
        this.nickname=nickname;
        this.gender=gender;
        this.age=age;
        this.address=address;
        this.mbti=mbti;
        this.stateMessage=stateMessage;
        this.userNum=userNum;
    }

    public int getProfile_image() {
        return profile_image;
    }
    public void setProfile_image(int profile_image) {
        this.profile_image = profile_image;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setNickname(String nickname){
        this.nickname=nickname;
    }
    public String getGender(){
        return this.gender;
    }
    public void setGender(String gender){
        this.gender=gender;
    }
    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age=age;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public String getMbti(){
        return this.mbti;
    }
    public void setMbti(String mbti){
        this.mbti=mbti;
    }
    public String getStateMessage(){
        return this.stateMessage;
    }
    public void setStateMessage(String stateMessage){
        this.stateMessage=stateMessage;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }
}
