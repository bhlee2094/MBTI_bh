package kr.hongik.mbti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Board implements Serializable {
    private String title;
    private String content;
    private String nickname;
    private String up;
    private String comment;
    private String boardId;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public static String p_title = "title";
    public static String p_content = "content";
    public static String p_nickname = "nickname";
    public static String p_up = "up";
    public static String p_comment = "comment";
    public static String p_boardId = "boardId";
    public static String p_uId = "uId";


    public Board() {
    }

    public Board(String title, String content, String nickname, String up, String comment, String boardId) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.up = up;
        this.comment = comment;
        this.boardId = boardId;
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

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

}
