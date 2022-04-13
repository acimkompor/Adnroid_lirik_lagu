package com.acimsoft.lha.lirik_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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


public class Login extends AppCompatActivity {

    Button loginButton;
    ProgressDialog dialog;
    TextView txtRegister, txtForgot;
    EditText txtUser, txtPassword;
    SessionManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sm = new SessionManager(this);
        loginButton = findViewById(R.id.btnlogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtUser = findViewById(R.id.txtusername);
        txtPassword = findViewById(R.id.txtpassword);
        txtForgot = findViewById(R.id.txtForgot);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Tunggu ya....");

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                CustomIntent.customType(Login.this, "fadein-to-fadeout");
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, RequestResetPassword.class));
                CustomIntent.customType(Login.this, "fadein-to-fadeout");
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                ApiRetro api = RetroClient.getApiRetro();
                Call<ResponModel> getLog = api.getLogin(txtUser.getText().toString(), txtPassword.getText().toString());
                getLog.enqueue(new Callback<ResponModel>() {
                    @Override
                    public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                        ResponModel res = response.body();
                        dialog.dismiss();
                        if (res.getKode() == 200){
                            String id = res.getResult().get(0).getId();
                            String nama = res.getResult().get(0).getNama();
                            String tgl = res.getResult().get(0).getCreate();
                            String url_prof = res.getResult().get(0).getProfil();
                            sm.setSess(id, nama, "login", tgl, url_prof, txtUser.getText().toString());
                            Toast.makeText(Login.this, res.getMessage(), Toast.LENGTH_SHORT).show();
//                            SettingFragment.getInstance().loadData();
                            finish();
                            CustomIntent.customType(Login.this, "fadein-to-fadeout");
                        } else {
                            Toast.makeText(Login.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponModel> call, Throwable t) {
                        dialog.dismiss();
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CustomIntent.customType(Login.this, "up-to-bottom");
    }

    //    @Override
//    public void finish() {
//        super.finish();
//        CustomIntent.customType(Login.this, "up-to-bottom");
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            CustomIntent.customType(Login.this, "up-to-bottom");
//            startActivity(new Intent(getApplicationContext(), Depan.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
