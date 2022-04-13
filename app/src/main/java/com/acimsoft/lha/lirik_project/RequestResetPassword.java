package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AppCompatActivity;
import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

public class RequestResetPassword extends AppCompatActivity  implements Validator.ValidationListener {

    @NotEmpty
    @Email
    private EditText edtEmail;
    Button btnSend;
    ProgressDialog dialog;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_reset_password);

        validator = new Validator(this);
        validator.setValidationListener(this);

        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...!");

        btnSend = findViewById(R.id.btnSend);
        edtEmail = findViewById(R.id.txtusername);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend_onclick(view);
            }
        });
    }

    private void btnSend_onclick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        dialog.show();
        final String email = edtEmail.getText().toString();
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> req = api.reqLupapass(email);
        req.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel res = response.body();
                if (res.getKode() == 200){
                    Intent intent = new Intent(RequestResetPassword.this, AktivasiKode.class);
                    intent.putExtra("kode", "reset");
                    intent.putExtra("email", email);
                    startActivity(intent);
                    Toast.makeText(RequestResetPassword.this, res.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                    CustomIntent.customType(RequestResetPassword.this, "fadein-to-fadeout");
                } else {
                    Toast.makeText(RequestResetPassword.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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
        CustomIntent.customType(RequestResetPassword.this, "fadein-to-fadeout");
    }
}
