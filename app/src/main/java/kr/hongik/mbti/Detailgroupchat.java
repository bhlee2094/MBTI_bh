package kr.hongik.mbti;

public class Detailgroupchat {
    private String nickname;
    private String message;

    public static String dgc_message = "message";
    public static String dgc_nickname = "nickname";

    public Detailgroupchat() {
    }

    public Detailgroupchat(String nickname, String message) {
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
