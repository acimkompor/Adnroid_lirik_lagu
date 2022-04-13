package com.acimsoft.lha.lirik_project;

import android.content.Intent;
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

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Adapter.AdapterSearch;
import com.acimsoft.lha.lirik_project.Adapter.AdapterSearchNew;
import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.PaginationScrollListener;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements AdapterSearchNew.SelectedItem {

    private RecyclerView rv;
    private LinearLayoutManager manager;
    private AdapterSearchNew adapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private int perpage = 55;
    private int totalrow;

    private TextView txtKeyword;
    private ImageView imgClear;

    private RelativeLayout rl;
    private ProgressBar progressBar;
    private LinearLayout llayout;

    private static SearchFragment intance;
    public static SearchFragment getInstance(){
        return intance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        intance = this;
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setElevation(8);

        imgClear = view.findViewById(R.id.btnClear);
        txtKeyword = view.findViewById(R.id.edSrc);

        adapter = new AdapterSearchNew(getContext(), this);
        rl = view.findViewById(R.id.rlnotfound);
        rv = view.findViewById(R.id.rvSearch);
        progressBar = view.findViewById(R.id.progress);
        manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
//        Toast.makeText(getContext(), "page: " + currentPage, Toast.LENGTH_SHORT).show();

        rv.addOnScrollListener(new PaginationScrollListener(manager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
//                Toast.makeText(getContext(), "page: " + currentPage, Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastPage();
                    }
                }, 1000);

            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        if (Pengaturan.CheckNetClass.checknetwork(getContext())) {
            progressBar.setVisibility(View.VISIBLE);
            firstPage();
        } else {
            Toast.makeText(getContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
        }


        imgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtKeyword.setText("");
            }
        });
        txtKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    showAfterView();
                    return true;
                }
                return false;
            }
        });

        txtKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                    adapter.clear();
                    isLastPage = false;
                    currentPage = PAGE_START;
                    firstPage();
                }
            }
        });

        llayout = view.findViewById(R.id.llayout);
        createLayoutDynamically(7);

        return view;
    }

    public void showAfterView() {
        adapter.clear();
        isLastPage = false;
        currentPage = PAGE_START;
        firstPage();
    }

    private void createLayoutDynamically(int n) {

        for (int i = 0; i < n; i++) {
            Button myButton = new Button(getContext());
            myButton.setText("Button :" + i);
            myButton.setId(i);
            final int id_ = myButton.getId();

            llayout.addView(myButton);

            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Toast.makeText(getContext(),
                            "Button clicked index = " + id_, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    private void firstPage() {
        callAllData().enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                List<DataModel> result = fetchResults(response);
                adapter.addAll(result);
                progressBar.setVisibility(View.GONE);
                if (result.size() > 0) {
                    rl.setVisibility(View.GONE);
                } else {
                    rl.setVisibility(View.VISIBLE);
                }

                totalrow = response.body().getTotal_row();
                if (totalrow > perpage){
                    TOTAL_PAGES = (int) Math.ceil(totalrow/perpage);
                    if (currentPage <= TOTAL_PAGES)
                        adapter.addLoadingFooter();
                    else
                        isLastPage = true;
                } else {
                    isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void lastPage(){
        callAllData().enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<DataModel> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) {
                    adapter.addLoadingFooter();
                } else {
                    isLastPage = true;
//                    Toast.makeText(getContext(), "Cuma 5 Page yang bisa ditampilkan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private List<DataModel> fetchResults(Response<ResponModel> response){
        ResponModel res = response.body();
        return res.getResult();
    }


    private Call<ResponModel> callAllData(){
        return RetroClient.getApiRetro().getLirik(
                Build.ID, txtKeyword.getText().toString(), currentPage, perpage
        );
    }

    @Override
    public void selectedItem(DataModel dataModel) {
//        Toast.makeText(getContext(), "judul : " + dataModel.getJudul(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getContext(), ViewLirik.class).putExtra("data", dataModel));
        addHistory(dataModel.getId(), Build.ID);
    }

    private void addHistory(String idlirik, String iduniq){
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> addhistori = api.addHistory(iduniq, idlirik);
        addhistori.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {

            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
