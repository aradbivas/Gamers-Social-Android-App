package com.example.finallproject;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class ProfileData {
    String name;
    String uid;
    String uri;
    public ImageView Img;


    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }


    public void setImg(Bitmap img) {
        Img.setImageBitmap(img);
    }
}
