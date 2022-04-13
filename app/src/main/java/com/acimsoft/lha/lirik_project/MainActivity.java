package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.acimsoft.lha.lirik_project.Adapter.AdapterLikedView;
import com.acimsoft.lha.lirik_project.Adapter.AdapterTopView;
import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvTop, rvLike;
    RecyclerView.LayoutManager manager, lmanager;
    AdapterTopView apdater;
    AdapterLikedView ladapter;
    List<DataModel> mlist = new ArrayList<>();
    List<DataModel> listLiked = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        loadTopView();
        loadLiked();

        TextView dt = findViewById(R.id.toolbar_title);
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Search.class));
            }
        });
    }

    private void loadLiked() {
//        rvLike = findViewById(R.id.rvLike);
//        lmanager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rvLike.setLayoutManager(lmanager);
//
//        ApiRetro api = RetroClient.getApiRetro();
//        Call<ResponModel> getLike = api.getTopLiked();
//        getLike.enqueue(new Callback<ResponModel>() {
//            @Override
//            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
//                listLiked = response.body().getResult();
//                ladapter = new AdapterLikedView(MainActivity.this, listLiked);
//                rvLike.setAdapter(ladapter);
//                ladapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<ResponModel> call, Throwable t) {
//
//            }
//        });
    }

    private void loadTopView() {

//        rvTop = findViewById(R.id.rvTop);
//        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        rvTop.setLayoutManager(manager);
//
//        ApiRetro api = RetroClient.getApiRetro();
//        Call<ResponModel> getTop = api.getTopViewer();
//        getTop.enqueue(new Callback<ResponModel>() {
//            @Override
//            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
//                mlist = response.body().getResult();
//                apdater = new AdapterTopView(MainActivity.this, mlist);
//                rvTop.setAdapter(apdater);
//                apdater.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(Call<ResponModel> call, Throwable t) {
//
//            }
//        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.favorite) {

        }

        return super.onOptionsItemSelected(item);
    }
}
