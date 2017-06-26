package com.coderschool.nytimes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.coderschool.nytimes.Utils.UiUtils;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 6/21/2017.
 */

public class Article implements Parcelable {
    private static final String BASE_IMG_URL = "http://www.nytimes.com/";
    @SerializedName("snippet")
    private String snippet;
    @SerializedName("web_url")
    private String webUrl;
    @SerializedName("multimedia")
    private List<Media> multimedia;

    protected Article(Parcel in) {
        snippet = in.readString();
        webUrl = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(snippet);
        dest.writeString(webUrl);
    }

    public class Media{
        @SerializedName("url")
        private String url;
        @SerializedName("height")
        private int height;
        @SerializedName("width")
        private int width;
        @SerializedName("type")
        private String type;

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return BASE_IMG_URL + url;
        }

        public int setUpLayoutParams(){
            int itemWidth = UiUtils.getScreenWidth() / 2;
            int itemHeight = itemWidth * height / width;

            return itemHeight;
        }
    }

    public String getSnippet() {
        return snippet;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Media> getMultimedia() {
        return multimedia;
    }
}
