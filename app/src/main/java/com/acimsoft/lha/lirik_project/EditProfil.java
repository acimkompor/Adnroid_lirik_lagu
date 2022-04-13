package com.acimsoft.lha.lirik_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import de.hdodenhof.circleimageview.CircleImageView;
import maes.tech.intentanim.CustomIntent;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.bumptech.glide.Glide;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.HashMap;
import java.util.List;


public class EditProfil extends AppCompatActivity implements Validator.ValidationListener  {

    CircleImageView imgProfil;
    ImageView imgTake;
    File fileimage;
    String url, id, tgl, status;
    SessionManager sm;
    Button btnSave;

    @NotEmpty
    @Length(min = 6)
    EditText nama;

    EditText  email, tgl_gabung;
    MultipartBody.Part partImage;
    Call<ResponModel> data;

    private Validator validator;

    public static final int REQUEST_CODE_CAMERA = 100;
    public static final int REQUEST_CODE_GALLERY = 200;

    private static final int PERMISSION_REQUEST_STORAGE = 2;
    private static final int PERMISSION_REQUEST_CAMERA = 1;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        validator = new Validator(this);
        validator.setValidationListener(this);

        sm = new SessionManager(getApplicationContext());
        HashMap<String, String> dt = sm.getSess();
        tgl = dt.get(sm.TGL);
        status = dt.get(sm.STATUS);
        id = dt.get(sm.ID);


        btnSave = findViewById(R.id.btnSave);
        imgProfil = findViewById(R.id.imgProf);
        imgTake = findViewById(R.id.imgTake);
        email = findViewById(R.id.edEmail);
        nama = findViewById(R.id.edNama);
        tgl_gabung = findViewById(R.id.edDaftar);

        nama.setText(dt.get(sm.NAME));
        email.setText(dt.get(sm.EMAIL));
        tgl_gabung.setText(dt.get(sm.TGL));

        imgTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Ambil dari Kamera", "Ambil dari Galeri"};

                final AlertDialog.Builder builder = new AlertDialog.Builder(EditProfil.this);
                builder.setTitle("Cari Gambar");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("Ambil dari Kamera")){
                            showCameraPreview();
                        }
                        else if (items[which].equals("Ambil dari Galeri")){
                            checkExternal();
                        }
                    }
                });
                builder.show();
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...");

        url = getIntent().getStringExtra("url");
        Glide.with(this)
                .load(url)
                .centerCrop()
                .into(imgProfil);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
                fileimage = new File(imageUri.getPath());
                //Menampilkan Gambar pada ImageView
//                Picasso.get().load(imageUri).into(imgicon);
//                uploadImg(key, idbarang);
                Glide.with(this)
                        .load(imageUri)
                        .centerCrop()
                        .into(imgProfil);

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //Menangani Jika terjadi kesalahan
                String error = result.getError().toString();
                Log.d("Exception", error);
                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
            }
        }

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {

            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Error pada Image
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Method Ini Digunakan Untuk Menghandle Image
                switch (type){
                    case REQUEST_CODE_CAMERA:
                        CropImage.activity(Uri.fromFile(imageFile))
                                .setAspectRatio(3,3)
                                .setRequestedSize(300,300, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                                .start(EditProfil.this);
                        break;

                    case REQUEST_CODE_GALLERY:
                        CropImage.activity(Uri.fromFile(imageFile))
                                .setAspectRatio(3,3)
                                .setRequestedSize(300,300, CropImageView.RequestSizeOptions.RESIZE_EXACT)
                                .start(EditProfil.this);
                        Log.d("imageFile", imageFile.toString());
                        Log.d("source", source.toString());
                        break;
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Batalkan penanganan, Anda mungkin ingin menghapus foto yang diambil jika dibatalkan
            }
        });
    }

    private void uploadImg(final String nama, final String email){
        dialog.show();
        if (fileimage != null) {
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), fileimage);
            partImage = MultipartBody.Part.createFormData("image", fileimage.getName(), reqBody);
        }
        RequestBody rEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody rNama = RequestBody.create(MediaType.parse("text/plain"), nama);

        ApiRetro api = RetroClient.getApiRetro();
        if (fileimage!=null) {
            data = api.changeProfil(rEmail, rNama, partImage);
        } else {
            data = api.changeProfilNoImage(rEmail, rNama);
        }

        data.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel model = response.body();
                if (model.getKode()==200){
                    sm.setSess(id, nama, status, tgl, model.getFoto(), email);
                    SettingFragment.getInstance().loadEditProfil();
                    finish();
                    CustomIntent.customType(EditProfil.this, "fadein-to-fadeout");
                }
                Toast.makeText(EditProfil.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditProfil.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showCameraPreview() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EditProfil.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfil.this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(EditProfil.this,
                        new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(EditProfil.this,
                        new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        } else {
            EasyImage.openCamera(EditProfil.this, REQUEST_CODE_CAMERA);
        }
    }

    private void checkExternal(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EditProfil.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfil.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(EditProfil.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
//
            } else {
                ActivityCompat.requestPermissions(EditProfil.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
            }
        } else {
            EasyImage.openGallery(EditProfil.this, REQUEST_CODE_GALLERY);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // mendapatkan Izin
                    EasyImage.openGallery(EditProfil.this, REQUEST_CODE_GALLERY);
                } else {
                    // tidak mendapatkan izin
                    Toast.makeText(this, "Dibutuhkan izin untuk akses External Penyimpanan", Toast.LENGTH_LONG).show();
                }
                break;


            case PERMISSION_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // mendapatkan Izin
                    EasyImage.openCamera(EditProfil.this, REQUEST_CODE_CAMERA);
                } else {
                    // tidak mendapatkan izin
                     Toast.makeText(this, "Dibutuhkan izin untuk akses Camera", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void onValidationSucceeded() {
        dialog.show();
        uploadImg(nama.getText().toString(), email.getText().toString());
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
        CustomIntent.customType(EditProfil.this, "fadein-to-fadeout");
    }
}
