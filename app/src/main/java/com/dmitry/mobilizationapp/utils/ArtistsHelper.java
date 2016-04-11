package com.dmitry.mobilizationapp.utils;

import android.content.Context;

import com.dmitry.mobilizationapp.models.Artist;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Represent an helper for reading data from cache
 */

public class ArtistsHelper {

    public static final String FILE_NAME = "artists";
    private static ArtistsHelper sArtistsHelper;
    private ArrayList<Artist> mArtists;

    public static ArtistsHelper get(Context context) {
        if(sArtistsHelper == null) {
            sArtistsHelper = new ArtistsHelper(context);
        }
        return sArtistsHelper;
    }

    private ArtistsHelper(Context context){
        mArtists = new ArrayList<>();
        try {
            mArtists.addAll((ArrayList<Artist>) CacheHelper.readArtistsList(context, FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Artist> getArtists() {
        return mArtists;
    }

    public Artist getArtist(long id) {
        for (Artist artist : mArtists) {
            if (artist.getId() == id) {
                return artist;
            }
        }
        return null;
    }

}
