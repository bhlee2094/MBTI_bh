package kr.hongik.mbti;

public class VODetailgroupchat {
    private String nickname;
    private String message;

    public static String dgc_message = "message";
    public static String dgc_nickname = "nickname";

    public VODetailgroupchat() {
    }

    public VODetailgroupchat(String nickname, String message) {
        this.nickname = nickname;
        this.message = message;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
