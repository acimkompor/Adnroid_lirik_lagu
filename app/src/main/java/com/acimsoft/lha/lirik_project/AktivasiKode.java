package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.ResponModel;

public class AktivasiKode extends AppCompatActivity {

    String kode, email;
    TextView txtKet;
    EditText edtKode;
    Button btnAktivasi;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivasi_kode);

        txtKet = findViewById(R.id.txtKet);
        edtKode = findViewById(R.id.editAktivasi);
        btnAktivasi = findViewById(R.id.btnAktivasi);
        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...");

        kode = getIntent().getStringExtra("kode");
        email = getIntent().getStringExtra("email");

        btnAktivasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kode.equals("register")){
                    loadRegister();
                } else if (kode.equals("reset")){
                    loadReset();
                }
            }
        });

    }

    private void loadReset() {
        dialog.show();
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getReset = api.toVerifyReset(edtKode.getText().toString(), email);
        getReset.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel res = response.body();
                if (res.getKode() == 200){
                    startActivity(new Intent(AktivasiKode.this, NewPassword.class).putExtra("email", email));
                    Toast.makeText(AktivasiKode.this, res.getMessage() + ", silahkan masukkan password baru anda.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AktivasiKode.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });


    }

    private void loadRegister() {
        dialog.show();
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getAktivasi = api.toVerifyRegister(edtKode.getText().toString(), email);
        getAktivasi.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel res = response.body();
                if (res.getKode() == 200){
                    Toast.makeText(AktivasiKode.this, res.getMessage() + ", Silahkan login", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AktivasiKode.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });

    }

    private void batalRegister() {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> batalAktivasi = api.cancelRegister(email);
        batalAktivasi.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Apakah ingin dibatalin?...")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (kode == "register"){
                            batalRegister();
                        }
                        AktivasiKode.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
