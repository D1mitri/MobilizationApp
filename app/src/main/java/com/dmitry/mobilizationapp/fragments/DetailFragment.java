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
import com.dmitry.mobilizationapp.utils.ArtistsHelper;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Represent an fragment, where user can see detail information about artist
 */

public class DetailFragment extends Fragment {

    private static final String ARG_ID = "id";

    private Artist mArtist;

    @Bind(R.id.ivBigCover) ImageView ivBigCover;
    @Bind(R.id.tvGenres) TextView tvGenres;
    @Bind(R.id.tvAlbumsWithTracks) TextView tvAlbumsWithTracks;
    @Bind(R.id.tvBiography) TextView tvBiography;

    public static DetailFragment newInstance(Artist artist) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, artist.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long mId = getArguments().getLong(ARG_ID);
            mArtist = ArtistsHelper.get(getActivity()).getArtist(mId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActivity().setTitle(mArtist.getName());

        Picasso.with(getActivity())
                .load(mArtist.getBigCover())
                .fit().centerCrop()
                .into(ivBigCover);

        String genres = Arrays.toString(mArtist.getGenres()).replaceAll("\\[|\\]", "");
        tvGenres.setText(genres);

        String albums = this.getResources().getQuantityString(R.plurals.albums, mArtist.getAlbums(), mArtist.getAlbums());
        String tracks = this.getResources().getQuantityString(R.plurals.tracks, mArtist.getTracks(), mArtist.getTracks());
        tvAlbumsWithTracks.setText(albums + " " + getActivity().getResources().getString(R.string.dot_operator) + " " + tracks);

        tvBiography.setText(mArtist.getDescription());

        return v;
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

}
