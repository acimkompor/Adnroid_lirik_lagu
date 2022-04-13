package com.acimsoft.lha.lirik_project;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.acimsoft.lha.lirik_project.Utils.Pengaturan;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements Validator.ValidationListener {

    TextView backLogin;

    @NotEmpty
    @Length(min = 3, max = 20)
    private EditText editTextUsername;

    @NotEmpty
//    @Pattern(regex = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})")
//    @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC)
    @Length(min = 6)
    @Password(scheme = Password.Scheme.ANY)
    private EditText editTextPassword;

    @ConfirmPassword
    private EditText editTextConfirmPassword;

    @NotEmpty
    @Email
    private EditText editTextEmail;

    @Checked
    private CheckBox checkBoxAgree;

    private Button buttonRegister;

    private Validator validator;
    ProgressDialog dialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        validator = new Validator(this);
        validator.setValidationListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...");

        editTextUsername = findViewById(R.id.txtnama);
        editTextEmail = findViewById(R.id.txtusername);
        editTextPassword = findViewById(R.id.txtpassword);
        editTextConfirmPassword = findViewById(R.id.txtpassword2);
        checkBoxAgree = findViewById(R.id.cekBox);
        buttonRegister = findViewById(R.id.btnRegister);
        backLogin = findViewById(R.id.txtBackLogin);
        TextView terms = findViewById(R.id.termOfUse);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this, KebijakanPrivasi.class));
            }
        });

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                CustomIntent.customType(Register.this, "fadein-to-fadeout");
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonRegister_onclick(view);
            }
        });

    }

    private void buttonRegister_onclick(View view) {
        validator.validate();
        String username = editTextUsername.getText().toString();
        if (username.equalsIgnoreCase("admin")) {
            editTextUsername.setError(getText(R.string.username_already_exists));
        }
    }


    @Override
    public void onValidationSucceeded() {
        dialog.show();
        String iduniq = Build.ID;
        String nama = editTextUsername.getText().toString();
        final String email = editTextEmail.getText().toString();
        String pass = editTextPassword.getText().toString();
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> reg = api.register(iduniq, email, pass, nama);
        reg.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel res = response.body();
                if (res.getKode() == 200){
                    Intent intent = new Intent(Register.this, AktivasiKode.class);
                    intent.putExtra("kode", "register");
                    intent.putExtra("email", email);
                    startActivity(intent);
                    Toast.makeText(Register.this, "cek email, dan masukkan kode aktivasinya.", Toast.LENGTH_SHORT).show();
                    finish();
                    CustomIntent.customType(Register.this, "fadein-to-fadeout");
                } else {
                    Toast.makeText(Register.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        CustomIntent.customType(Register.this, "fadein-to-fadeout");
    }
}
