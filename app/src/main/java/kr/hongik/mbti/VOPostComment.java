package kr.hongik.mbti;

public class VOPostComment {
    private String nickname;
    private String comment;
    private String commentId;

    public VOPostComment() {
    }

    public VOPostComment(String nickname, String comment, String commentId) {
        this.nickname = nickname;
        this.comment = comment;
        this.commentId = commentId;
    }

    public VOPostComment(String nickname, String comment) {
        this.nickname = nickname;
        this.comment = comment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
