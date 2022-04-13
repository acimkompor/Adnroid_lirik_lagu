package com.acimsoft.lha.lirik_project;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Adapter.AdapterFavorite;
import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment{

    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    AdapterFavorite adapter;
    List<DataModel> mlist = new ArrayList<>();
    String id;
    RelativeLayout rl;
    ProgressBar progressBar;

    private static FavoriteFragment instance;
    public static FavoriteFragment getInstance(){
        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(8);

        rl = view.findViewById(R.id.rlnotfound);
        rv = view.findViewById(R.id.rvFavorite);
        progressBar = view.findViewById(R.id.progress);

        manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(manager);
        id = Build.ID;
        instance = this;

        if (Pengaturan.CheckNetClass.checknetwork(getContext())) {
            progressBar.setVisibility(View.VISIBLE);
            fetching();
        } else {
            Toast.makeText(getContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
        }

        return view;
    }

    public void fetching() {
        rl.setVisibility(View.GONE);
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getF = api.getMyFavorite(id);
        getF.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                mlist = response.body().getResult();
                adapter = new AdapterFavorite(getContext(), mlist);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                if (mlist.size() > 0) {
                    rl.setVisibility(View.GONE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
