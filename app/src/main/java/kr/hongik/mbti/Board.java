package kr.hongik.mbti;

public class Board {
    private String title;
    private String content;
    private String nickname;
    private String up;
    private String comment;

    public Board() {
    }

    public Board(String title, String content, String nickname, String up, String comment) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.up = up;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
