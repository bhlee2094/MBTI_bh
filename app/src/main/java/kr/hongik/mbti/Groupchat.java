package kr.hongik.mbti;

public class Groupchat {
    private int gchat_image;
    private String gchat_title;
    private String gchatId;

    public Groupchat() {
    }

    public Groupchat(int gchat_image, String gchat_title, String gchatId) {
        this.gchat_image = gchat_image;
        this.gchat_title = gchat_title;
        this.gchatId = gchatId;
    }

    public int getGchat_image() {
        return gchat_image;
    }

    public void setGchat_image(int gchat_image) {
        this.gchat_image = gchat_image;
    }

    public String getGchat_title() {
        return gchat_title;
    }

    public void setGchat_title(String gchat_title) {
        this.gchat_title = gchat_title;
    }

    public String getGchatId() {
        return gchatId;
    }

    public void setGchatId(String gchatId) {
        this.gchatId = gchatId;
    }
}
