package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Adapter.AdapterListByUser;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.PaginationScrollListener;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;

import java.util.HashMap;
import java.util.List;

public class ListByUser extends AppCompatActivity implements AdapterListByUser.SelectedItem {

    RecyclerView rv;
    LinearLayoutManager manager;
    AdapterListByUser adapter;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;
    private int perpage = 70;
    private int totalrow;

    TextView txtKeyword;
    ImageView imgClear;

    RelativeLayout rl;
    ProgressBar progressBar;
    String idemployee;
    SessionManager sm;

   private static ListByUser instance;
   public static ListByUser getInstance() {
       return instance;
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_by_user);

        instance = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(8);

        adapter = new AdapterListByUser(ListByUser.this, this);
        rl = findViewById(R.id.rlnotfound);
        rv = findViewById(R.id.rvSearch);
        progressBar = findViewById(R.id.progress);
        manager = new LinearLayoutManager(ListByUser.this, RecyclerView.VERTICAL, false);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        sm = new SessionManager(this);
        HashMap<String, String> dt = sm.getSess();
        idemployee = dt.get(sm.ID);

        imgClear = findViewById(R.id.btnClear);
        txtKeyword = findViewById(R.id.edSrc);

        //Menyembunyikan Keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txtKeyword.getWindowToken(), 0);

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

        if (Pengaturan.CheckNetClass.checknetwork(getApplicationContext())) {
            progressBar.setVisibility(View.VISIBLE);
            firstPage();
        } else {
            Toast.makeText(getApplicationContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
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
                    loadAfterSearch();
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
    }

    public void loadAfterSearch(){
        adapter.clear();
        isLastPage = false;
        currentPage = PAGE_START;
        firstPage();
    }

    @Override
    public void selectedItem(DataModel dataModel) {
        String image = getJPG(dataModel.getImage());
        Intent i = new Intent(ListByUser.this, EditLirikByUser.class);
        i.putExtra("image_old", image);
        i.putExtra("data", dataModel);
        startActivity(i);
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
        return RetroClient.getApiRetro().getLirikbyId(
                idemployee, txtKeyword.getText().toString(), currentPage, perpage
        );
    }

    private String getJPG(String url){
        String[] name = url.split("/");
        return name[6];
    }
}
