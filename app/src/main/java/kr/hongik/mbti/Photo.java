package kr.hongik.mbti;

public class Photo {

    public String imageurl;
    public String photo_id;
    public String photo_key;

    public Photo() {
    }

    public Photo(String imageurl, String photo_id, String photo_key) {
        this.imageurl = imageurl;
        this.photo_id = photo_id;
        this.photo_key = photo_key;
    }

    public Photo(String imageurl, String photo_id) {
        this.imageurl = imageurl;
        this.photo_id = photo_id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getPhoto_key() {
        return photo_key;
    }

    public void setPhoto_key(String photo_key) {
        this.photo_key = photo_key;
    }
}
