package com.dmitry.mobilizationapp.api;

import com.dmitry.mobilizationapp.models.Artist;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface for request to server
 */

public interface ArtistsAPI {

    @GET("/download.cdn.yandex.net/mobilization-2016/artists.json")
    Call<ArrayList<Artist>> getArtists();

}
