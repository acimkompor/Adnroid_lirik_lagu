package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class NewPassword extends AppCompatActivity  implements Validator.ValidationListener {

    @Length(min = 6)
    @Password(scheme = Password.Scheme.ANY)
    private EditText editTextPassword;

    @ConfirmPassword
    private EditText editTextConfirmPassword;

    private Button buttonProses;

    private Validator validator;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        validator = new Validator(this);
        validator.setValidationListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...");

        editTextPassword = findViewById(R.id.txtpassword);
        editTextConfirmPassword = findViewById(R.id.txtpassword2);
        buttonProses = findViewById(R.id.btnChangPassword);

        buttonProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonProses_onclick(view);
            }
        });

    }

    private void buttonProses_onclick(View view) {
        validator.validate();
    }

    @Override
    public void onValidationSucceeded() {
        dialog.show();
        final String email = getIntent().getStringExtra("email");
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> reg = api.changePass(email, editTextPassword.getText().toString());
        reg.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel res = response.body();
                if (res.getKode() == 200){
                    Toast.makeText(NewPassword.this, res.getMessage() + ", silahkan login.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewPassword.this, res.getMessage(), Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(this)
                .setMessage("Apakah ingin dibatalin?...")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NewPassword.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
