package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;

public class ViewLirik extends AppCompatActivity {

    TextView txtJudul, txtVocal, txtLirik, txtJenis, txtFav, txtLike, txtView;
    ImageView imgArtis, imglogoLike, imgLogoFavorite;
    EditText edJudul, edKet;
    Button btnKirimReport, btnReport;
    String idlirik, iduniq, judul;
    int like, fav, tx;
    SessionManager sm;
    BottomSheetDialog bsd;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_lirik);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        transparentStatusAndNavigation();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        AdSize adSize = new AdSize(300, 50);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdClicked() {
                transparentStatusAndNavigation();
            }

            @Override
            public void onAdClosed() {
                transparentStatusAndNavigation();
            }

        });


        CollapsingToolbarLayout mCollapsingToolbar = findViewById(R.id.colapse);

        txtJudul = findViewById(R.id.toolbar_title);
        txtVocal = findViewById(R.id.txtArtis);
        txtLirik = findViewById(R.id.txtLirik);
        imgArtis = findViewById(R.id.imgArtis);
        txtJenis = findViewById(R.id.txtJenis);
        txtFav = findViewById(R.id.txtFav);
        txtLike = findViewById(R.id.txtLike);
        txtView = findViewById(R.id.txtView);
        imgLogoFavorite = findViewById(R.id.imgFav);
        imglogoLike = findViewById(R.id.imgLike);
        btnReport = findViewById(R.id.btnReport);

        txtFav.setText("");
        txtLike.setText("");
        txtView.setText("");

        sm = new SessionManager(this);
        final HashMap<String, String> fs = sm.getValueFont();
        txtLirik.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (18 * Integer.parseInt(fs.get(sm.VALSIZE))) / 100);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intent = getIntent();


        if(intent.getExtras() != null){
            DataModel userModel = (DataModel) intent.getSerializableExtra("data");

            judul = userModel.getJudul();
            txtVocal.setText(judul);
            txtLirik.setText(userModel.getLirik());
            txtJenis.setText("#"+userModel.getJenis()+"#");
            idlirik = userModel.getId();
            iduniq = Build.ID;
            Glide.with(ViewLirik.this)
                    .load(userModel.getProfil())
                    .centerCrop()
                    .into(imgArtis);

            mCollapsingToolbar.setTitle(userModel.getVocal());
        }

        mCollapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        getDataView();
        getDataFavLike();

        imgLogoFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tx = Integer.parseInt(txtFav.getText().toString());
                Log.d("tx", String.valueOf(tx));
                if (fav > 0){
                    deleteFavorite(iduniq, idlirik);
                } else {
                    addFavorite(iduniq, idlirik);
                }
            }
        });

        imglogoLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (like > 0){
                    deleteLike(iduniq, idlirik);
                } else {
                    addLike(iduniq, idlirik);
                }
            }
        });

        bsd = new BottomSheetDialog(this);
        View v = getLayoutInflater().inflate(R.layout.bottom_report, null);
        bsd.setContentView(v);

        edJudul = bsd.findViewById(R.id.edtLirik);
        edKet = bsd.findViewById(R.id.edtKeterangan);
        btnKirimReport = bsd.findViewById(R.id.btnKirimReport);

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edJudul.setText(judul);
                bsd.show();
            }
        });
        
        btnKirimReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edKet.length() <= 10) {
                    Toast.makeText(ViewLirik.this, "Keterangan terlalu pendek", Toast.LENGTH_SHORT).show();
                } else {
                    ApiRetro api = RetroClient.getApiRetro();
                    Call<ResponModel> sendReport = api.sendReport(idlirik, iduniq, edKet.getText().toString());
                    sendReport.enqueue(new Callback<ResponModel>() {
                        @Override
                        public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                            if (response.body().getKode() == 200) {
                                Toast.makeText(ViewLirik.this, "Terima kasih atas supportnya.", Toast.LENGTH_SHORT).show();
                                edKet.setText("");
                                bsd.dismiss();
                            } else {
                                Toast.makeText(ViewLirik.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponModel> call, Throwable t) {
                            Toast.makeText(ViewLirik.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    private void getDataFavLike() {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getF = api.getdataFavLike(iduniq, idlirik);
        getF.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                ResponModel res = response.body();
                txtFav.setText(res.getFavorite());
                txtLike.setText(res.getLiked());
                txtView.setText(res.getViewer());
                like = res.getFound_like();
                fav  = res.getFound_favorite();
                if (fav > 0 )
                    imgLogoFavorite.setImageResource(R.drawable.ic_favorite_select);
                else
                    imgLogoFavorite.setImageResource(R.drawable.ic_favorite_pull);

                if (like > 0)
                    imglogoLike.setImageResource(R.drawable.ic_thumbpx);
                else
                    imglogoLike.setImageResource(R.drawable.ic_thumb_orange);
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addFavorite(String iduniq, String idlirik) {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> add = api.addfavorite(iduniq, idlirik);
        add.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                if (response.body().getKode() == 200) {
                    getDataFavLike();
                    Toast.makeText(ViewLirik.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteFavorite(String iduniq, String idlirik) {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> del = api.delfavorite(iduniq, idlirik);
        del.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                Log.d("kode", String.valueOf(response.body().getKode()));
                if (response.body().getKode() == 200) {
                    getDataFavLike();
                    Toast.makeText(ViewLirik.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addLike(String iduniq, String idlirik) {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> add = api.addLike(iduniq, idlirik);
        add.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                if (response.body().getKode() == 200) {
                    getDataFavLike();
                    Toast.makeText(ViewLirik.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deleteLike(String iduniq, String idlirik) {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> del = api.delLike(iduniq, idlirik);
        del.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                Log.d("kode", String.valueOf(response.body().getKode()));
                if (response.body().getKode() == 200) {
                    getDataFavLike();
                    Toast.makeText(ViewLirik.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void getDataView() {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getdata = api.getdata(idlirik);
        getdata.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {

            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
//            Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            );
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {
//        SearchFragment.getInstance().showAfterView();
        finish();
    }
}
