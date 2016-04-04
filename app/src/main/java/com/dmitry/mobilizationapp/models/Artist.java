package com.dmitry.mobilizationapp.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Required model
 */

public class Artist implements Serializable {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("genres")
    private String [] genres;

    @SerializedName("tracks")
    private int tracks;

    @SerializedName("albums")
    private int albums;

    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @SerializedName("cover")
    private Cover cover;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getSmallCover() {
        return cover.small;
    }

    public String getBigCover() {
        return cover.big;
    }

    private class Cover implements Serializable {
        private String small;
        private String big;
    }
}
