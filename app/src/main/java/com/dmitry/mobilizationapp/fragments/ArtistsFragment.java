package com.dmitry.mobilizationapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitry.mobilizationapp.R;
import com.dmitry.mobilizationapp.api.ArtistsAPI;
import com.dmitry.mobilizationapp.models.Artist;
import com.dmitry.mobilizationapp.utils.ArtistsHelper;
import com.dmitry.mobilizationapp.utils.CacheHelper;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Represent an fragment, where user can see all list of artists
 */

public class ArtistsFragment extends Fragment {

    @Bind(R.id.rvArtists)
    RecyclerView rvArtists;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    private static final String URL = "http://cache-kiev01.cdn.yandex.net/";
    private ArrayList<Artist> mArtistsList;
    private ArtistAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, v);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActivity().setTitle(R.string.artists);
        rvArtists.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                File file = getContext().getFileStreamPath(ArtistsHelper.FILE_NAME);
                boolean haveCache = file.exists();
                if (haveCache) {
                    updateUI();
                } else {
                    downloadArtistsList();
                }
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadArtistsList();
            }
        });

        return v;
    }

    private void downloadArtistsList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArtistsAPI api = retrofit.create(ArtistsAPI.class);

        Call<ArrayList<Artist>> call = api.getArtists();
        call.enqueue(new Callback<ArrayList<Artist>>() {
            @Override
            public void onResponse(Call<ArrayList<Artist>> call, Response<ArrayList<Artist>> response) {
                mArtistsList = response.body();
                setupRecyclerView();
                swipeContainer.setRefreshing(false);
                writeArtistsListToCache();
            }

            @Override
            public void onFailure(Call<ArrayList<Artist>> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    private void writeArtistsListToCache() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    CacheHelper.writeArtistsList(getActivity(), ArtistsHelper.FILE_NAME, mArtistsList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }

    private void updateUI() {
        ArtistsHelper artistsHelper = ArtistsHelper.get(getActivity());
        mArtistsList = artistsHelper.getArtists();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        mAdapter = new ArtistAdapter(mArtistsList);
        rvArtists.setAdapter(mAdapter);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private class ArtistHolder extends RecyclerView.ViewHolder {

        private ImageView ivCover;
        private TextView tvArtistName;
        private TextView tvGenre;
        private TextView tvAlbumsAndTracks;

        private Artist mArtist;

        public ArtistHolder(View itemView) {
            super(itemView);
            ivCover = (ImageView) itemView.findViewById(R.id.ivCover);
            tvArtistName = (TextView) itemView.findViewById(R.id.tvArtistName);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenre);
            tvAlbumsAndTracks = (TextView) itemView.findViewById(R.id.tvAlbumsAndTracks);
        }

        public void bindArtist(Artist artist) {
            mArtist = artist;

            Picasso.with(getActivity())
                    .load(mArtist.getSmallCover())
                    .fit().centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivCover);

            tvArtistName.setText(mArtist.getName());

            String genres = Arrays.toString(mArtist.getGenres()).replaceAll("\\[|\\]", "");
            tvGenre.setText(genres);

            String albumsCount = String.valueOf(mArtist.getAlbums());
            String tracksCount = String.valueOf(mArtist.getTracks());
            tvAlbumsAndTracks.setText(albumsCount + " альбомов, " + tracksCount + " песен");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragment detailFragment = DetailFragment.newInstance(mArtist);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

    }

    private class ArtistAdapter extends RecyclerView.Adapter<ArtistHolder> {

        private List<Artist> mArtists;

        public ArtistAdapter(List<Artist> artists) {
            mArtists = artists;
        }

        @Override
        public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_artist, parent, false);
            return new ArtistHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistHolder holder, int position) {
            Artist artist = mArtists.get(position);
            holder.bindArtist(artist);
        }

        @Override
        public int getItemCount() {
            return mArtists.size();
        }
    }

}
