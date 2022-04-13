package com.acimsoft.lha.lirik_project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
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
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Adapter.AdapterLikedView;
import com.acimsoft.lha.lirik_project.Adapter.AdapterTopView;
import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

    RecyclerView rvTop, rvLike;
    RecyclerView.LayoutManager manager, lmanager;
    AdapterTopView apdater;
    AdapterLikedView ladapter;
    List<DataModel> mlist = new ArrayList<>();
    List<DataModel> listLiked = new ArrayList<>();
    String data;
    NestedScrollView neste;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(8);

        rvLike = view.findViewById(R.id.rvLike);
        rvTop = view.findViewById(R.id.rvTop);
        neste = view.findViewById(R.id.nested);
        progressBar = view.findViewById(R.id.progress);

        if (Pengaturan.CheckNetClass.checknetwork(getContext())) {
            progressBar.setVisibility(View.VISIBLE);
            loadTopViewLike();
        } else {
            Toast.makeText(getContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
        }

        return view;
    }


    private void loadTopViewLike() {

        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvTop.setLayoutManager(manager);

        lmanager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvLike.setLayoutManager(lmanager);

        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getTop = api.getTopViewerLike();
        getTop.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                // load top view
                mlist = response.body().getResult();
                apdater = new AdapterTopView(getActivity(), mlist);
                rvTop.setAdapter(apdater);
                apdater.notifyDataSetChanged();

                //load top like
                listLiked = response.body().getResult1();
                ladapter = new AdapterLikedView(getContext(), listLiked);
                rvLike.setAdapter(ladapter);
                ladapter.notifyDataSetChanged();


                progressBar.setVisibility(View.GONE);
                neste.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

}
