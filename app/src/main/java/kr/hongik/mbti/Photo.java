package kr.hongik.mbti;

import com.google.firebase.database.PropertyName;

public class Photo {

    public String imageurl;

    public Photo() {
    }

    public Photo(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
