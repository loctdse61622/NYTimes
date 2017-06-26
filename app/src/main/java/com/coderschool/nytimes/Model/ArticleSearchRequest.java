package com.coderschool.nytimes.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 6/21/2017.
 */

public class ArticleSearchRequest implements Parcelable {
    private int page = 0;
    private String query;
    private String beginDate;
    private String order = "Newest";
    public boolean hasArts = false;
    public boolean hasFashionStyle = false;
    public boolean hasSports = false;

    public ArticleSearchRequest(){}


    protected ArticleSearchRequest(Parcel in) {
        page = in.readInt();
        query = in.readString();
        beginDate = in.readString();
        order = in.readString();
        hasArts = in.readByte() != 0;
        hasFashionStyle = in.readByte() != 0;
        hasSports = in.readByte() != 0;
    }

    public static final Creator<ArticleSearchRequest> CREATOR = new Creator<ArticleSearchRequest>() {
        @Override
        public ArticleSearchRequest createFromParcel(Parcel in) {
            return new ArticleSearchRequest(in);
        }

        @Override
        public ArticleSearchRequest[] newArray(int size) {
            return new ArticleSearchRequest[size];
        }
    };

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean hasArts() {
        return hasArts;
    }

    public void setHasArts(boolean hasArts) {
        this.hasArts = hasArts;
    }

    public boolean hasFashionStyle() {
        return hasFashionStyle;
    }

    public void setHasFashionStyle(boolean hasFashionStyle) {
        this.hasFashionStyle = hasFashionStyle;
    }

    public boolean hasSports() {
        return hasSports;
    }

    public void setHasSports(boolean hasSports) {
        this.hasSports = hasSports;
    }

    public void resetPage() { page = 0; }

    public void nextPage() { page += 1; }

    public Map<String, String> toQueryMap(){
        Map<String, String> options = new HashMap<>();
        if (query != null) options.put("q", query);
        if (beginDate != null) options.put("begin_date", beginDate);
        if (order != null) options.put("sort", order.toLowerCase());
        if (getNewDesk() != null) options.put("fq", "news_desk:(" + getNewDesk() + ")");
        options.put("page", String.valueOf(page));
        return options;
    }

    private String getNewDesk(){
        if (!hasSports && !hasArts && !hasFashionStyle) return null;
        String desk = "";
        if (hasArts) desk += "\"Arts\" ";
        if (hasSports) desk += " \"Sports\" ";
        if (hasFashionStyle) desk += " \"Fashion & Style\"";
        return desk.trim();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(query);
        dest.writeString(beginDate);
        dest.writeString(order);
        dest.writeByte((byte) (hasArts ? 1 : 0));
        dest.writeByte((byte) (hasFashionStyle ? 1 : 0));
        dest.writeByte((byte) (hasSports ? 1 : 0));
    }
}
