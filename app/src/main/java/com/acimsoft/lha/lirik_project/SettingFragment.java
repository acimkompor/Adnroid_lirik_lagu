package com.acimsoft.lha.lirik_project;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Adapter.AdapterHistory;
import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;
import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class SettingFragment extends Fragment {

    private TextView  txtNama, txtTgl, txtVersi, txtFontSize, txtPersen, txtPreview;
    CircleImageView imgProf;
    SessionManager sm;
    RelativeLayout relativeLayout;
    RelativeLayout rlTop, rlComposer, rlCopy, rlPrivacy, rlLogout, rlList, rlFontSize;
    String nama, tgl, status, ukuran;
    SeekBar seekBar;
    Button btnOK;

    RecyclerView rv;
    RecyclerView.LayoutManager manager;
    AdapterHistory adapater;
    List<DataModel> list = new ArrayList<>();
    Dialog dialog;

    private static SettingFragment instance;
    public static SettingFragment getInstance(){
        return instance;
    }
    String url;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);

        imgProf = view.findViewById(R.id.imgProf);
        txtNama = view.findViewById(R.id.txtNama);
        txtTgl = view.findViewById(R.id.txtTgl);
        txtVersi = view.findViewById(R.id.txtVersi);
        relativeLayout = view.findViewById(R.id.rvSetting);
        txtFontSize = view.findViewById(R.id.txtFontSize);

        rlComposer = view.findViewById(R.id.rlComposer);
        rlCopy = view.findViewById(R.id.rlCopyright);
        rlPrivacy = view.findViewById(R.id.rlKebijakan);
        rlTop = view.findViewById(R.id.rlTop);
        rlList = view.findViewById(R.id.rlLis);
        rlFontSize = view.findViewById(R.id.rlSetSize);

        rlList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ListByUser.class));
            }
        });

        relativeLayout.setVisibility(View.GONE);
        rlTop.setVisibility(View.GONE);
        rv = view.findViewById(R.id.rvTop);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(manager);

        instance = this;

        rlLogout = view.findViewById(R.id.rlSignout);
        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Apa ingin logout...?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SessionManager sm = new SessionManager(getContext());
                                sm.clearAll();
                                Depan.getInstance().loadFragment(new HomeFragment());
                                Depan.getInstance().navigation.getMenu().findItem(R.id.home_menu).setChecked(true);
//                                startActivity(new Intent(getContext(), Depan.class));
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        sm = new SessionManager(getContext());
        final HashMap<String, String> fs = sm.getValueFont();
        ukuran = fs.get(sm.VALSIZE);
        txtFontSize.setText(ukuran + '%');
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.seting_fontsize);

        txtPersen = dialog.findViewById(R.id.txtPersen);
        txtPreview = dialog.findViewById(R.id.txtPreview);
        seekBar = dialog.findViewById(R.id.seekFont);
        btnOK = dialog.findViewById(R.id.btnOK);
        rlFontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HashMap<String, String> fs = sm.getValueFont();
                ukuran = fs.get(sm.VALSIZE);
                txtPersen.setText(ukuran + "%");
                seekBar.setProgress(Integer.parseInt(ukuran));
                txtPreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float)(18 * seekBar.getProgress())/100);
                dialog.show();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sm.setValueFont(String.valueOf(seekBar.getProgress()));
                txtFontSize.setText(String.valueOf(seekBar.getProgress()) + "%");
                dialog.dismiss();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtPersen.setText(String.valueOf(seekBar.getProgress()) + "%");
                txtPreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float)(18 * seekBar.getProgress())/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        if (Pengaturan.CheckNetClass.checknetwork(getContext())) {
            loadData();
        } else {
            Toast.makeText(getContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
        }

        // Tulis lirik baru
        rlComposer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TulisLirikBaru.class));
            }
        });

        // Copyright
        rlCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), About.class));
            }
        });

        //Privacy
        rlPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), KebijakanPrivasi.class));
            }
        });

        imgProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), EditProfil.class);
                i.putExtra("url", url);
                startActivity(i);
                CustomIntent.customType(getContext(), "fadein-to-fadeout");
            }
        });


        return view;
    }

    public void loadEditProfil() {
        HashMap<String, String> dt = sm.getSess();
        nama = dt.get(sm.NAME);
        tgl = dt.get(sm.TGL);
        status = dt.get(sm.STATUS);
        url = dt.get(sm.URL_PROF);

        relativeLayout.setVisibility(View.VISIBLE);;
        txtNama.setText(nama);
        Glide.with(getContext())
                .load(url)
                .centerCrop()
                .into(imgProf);
    }

    public void loadData() {
        HashMap<String, String> dt = sm.getSess();
        nama = dt.get(sm.NAME);
        tgl = dt.get(sm.TGL);
        status = dt.get(sm.STATUS);
        url = dt.get(sm.URL_PROF);

        relativeLayout.setVisibility(View.VISIBLE);;
        txtTgl.setText(tgl);
        txtNama.setText(nama);
        txtVersi.setText("Versi app " + BuildConfig.VERSION_NAME);
        Glide.with(getContext())
                .load(url)
                .centerCrop()
                .into(imgProf);

        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getHistory = api.getMyHistory(Build.ID);
        getHistory.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                list = response.body().getResult();
                if (list.size() > 0) {
                    adapater = new AdapterHistory(getActivity(), list);
                    rv.setAdapter(adapater);
                    adapater.notifyDataSetChanged();
                    rlTop.setVisibility(View.VISIBLE);
                } else {
                    rlTop.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
