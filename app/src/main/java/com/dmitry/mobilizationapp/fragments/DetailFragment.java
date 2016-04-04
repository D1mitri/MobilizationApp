package com.dmitry.mobilizationapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitry.mobilizationapp.R;
import com.dmitry.mobilizationapp.models.Artist;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Represent an fragment, where user can see detail information about artist
 */

public class DetailFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_COVER_URL = "coverUrl";
    private static final String ARG_GENRES = "genres";
    private static final String ARG_ALBUMS = "albums";
    private static final String ARG_TRACKS = "tracks";
    private static final String ARG_BIOGRAPHY = "biography";

    private String mName;
    private String mCoverUrl;
    private String[] mGenres;
    private int mAlbums;
    private int mTracks;
    private String mBiography;

    @Bind(R.id.ivBigCover) ImageView ivBigCover;
    @Bind(R.id.tvGenres) TextView tvGenres;
    @Bind(R.id.tvAlbumsWithTracks) TextView tvAlbumsWithTracks;
    @Bind(R.id.tvBiography) TextView tvBiography;

    public static DetailFragment newInstance(Artist artist) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, artist.getName());
        args.putString(ARG_COVER_URL, artist.getBigCover());
        args.putStringArray(ARG_GENRES, artist.getGenres());
        args.putInt(ARG_ALBUMS, artist.getAlbums());
        args.putInt(ARG_TRACKS, artist.getTracks());
        args.putString(ARG_BIOGRAPHY, artist.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mCoverUrl = getArguments().getString(ARG_COVER_URL);
            mGenres = getArguments().getStringArray(ARG_GENRES);
            mAlbums = getArguments().getInt(ARG_ALBUMS);
            mTracks = getArguments().getInt(ARG_TRACKS);
            mBiography = getArguments().getString(ARG_BIOGRAPHY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(mName);

        Picasso.with(getActivity())
                .load(mCoverUrl)
                .fit().centerCrop()
                .into(ivBigCover);

        String genres = Arrays.toString(mGenres).replaceAll("\\[|\\]", "");
        tvGenres.setText(genres);

        String albumsCount = String.valueOf(mAlbums);
        String tracksCount = String.valueOf(mTracks);
        tvAlbumsWithTracks.setText(albumsCount + " альбомов " + getActivity().getResources().getString(R.string.dot_operator) + " " + tracksCount + " песни");

        tvBiography.setText(mBiography);

        return v;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

}
