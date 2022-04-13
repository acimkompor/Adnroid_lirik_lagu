package com.acimsoft.lha.lirik_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acimsoft.lha.lirik_project.Api.ApiRetro;
import com.acimsoft.lha.lirik_project.Api.RetroClient;
import com.acimsoft.lha.lirik_project.Config.SessionManager;
import com.acimsoft.lha.lirik_project.Models.DataModel;
import com.acimsoft.lha.lirik_project.Models.ResponModel;
import com.bumptech.glide.Glide;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditLirikByUser extends AppCompatActivity implements Validator.ValidationListener{

//    ImageView imgAdd, imgPref;

    @NotEmpty
    @Length(min = 3)
    TextView judul;

    @NotEmpty
    @Length(min = 3)
    TextView penyanyi;

    @NotEmpty
    @Length(min = 50)
    TextView lirik;

    private Validator validator;

    String idJenis, id, idlirik;
    Spinner spJenis;
    Button btnSimpan;
    private List<DataModel> mItems = new ArrayList<>();

    MultipartBody.Part partImage;
    Call<ResponModel> data;
//
//    public static final int REQUEST_CODE_CAMERA = 100;
//    public static final int REQUEST_CODE_GALLERY = 200;
//
//    private static final int PERMISSION_REQUEST_STORAGE = 2;
//    private static final int PERMISSION_REQUEST_CAMERA = 1;

    ProgressDialog dialog;
    File fileimage;
    SessionManager sm;
    DataModel dm;
    String Sjudul, Spenyanyi, Slirik, Sjenis, Surl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lirik_by_user);

        validator = new Validator(this);
        validator.setValidationListener(this);
        sm = new SessionManager(this);
        HashMap<String, String> dt = sm.getSess();
        id = dt.get(sm.ID);

        Intent intent = getIntent();
        if (intent.getExtras()!=null){
            dm = (DataModel) intent.getSerializableExtra("data");
            Sjudul = dm.getJudul();
            Spenyanyi = dm.getVocal();
            Slirik = dm.getLirik();
            Sjenis = dm.getJenis();
            Surl = dm.getImage();
            idlirik = dm.getId();
        }

//        imgAdd = findViewById(R.id.imgTake);
//        imgPref = findViewById(R.id.imgPreview);
        judul = findViewById(R.id.edJudul);
        penyanyi = findViewById(R.id.edVocal);
        lirik = findViewById(R.id.edLirik);
        spJenis = findViewById(R.id.spinnerJenis);
        btnSimpan = findViewById(R.id.btnUpdate);
        dialog = new ProgressDialog(this);
        dialog.setMessage("silahkan tunggu...");
        isiSpinnerProduk();
        spJenis.setOnItemSelectedListener(new EditLirikByUser.MyOnItemSelectedListener());

        isiForm();
//        imgAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final CharSequence[] items = {"Ambil dari Kamera", "Ambil dari Galeri"};
//
//                final AlertDialog.Builder builder = new AlertDialog.Builder(EditLirikByUser.this);
//                builder.setTitle("Cari Gambar");
//                builder.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (items[which].equals("Ambil dari Kamera")){
//                            showCameraPreview();
//                        }
//                        else if (items[which].equals("Ambil dari Galeri")){
//                            checkExternal();
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    private void isiForm(){
        judul.setText(Sjudul);
        penyanyi.setText(Spenyanyi);
        lirik.setText(Slirik);
//        Glide.with(this)
//                .load(Surl)
//                .centerCrop()
//                .into(imgPref);
    }

    private void isiSpinnerProduk() {
        ApiRetro api = RetroClient.getApiRetro();
        Call<ResponModel> getdata = api.getAllJenis();
        getdata.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                mItems = response.body().getResult();
                List<String> listSpinner = new ArrayList<String>();
                listSpinner.add("-- Pilih Jenis Lagu --");
                for (int i = 0; i < mItems.size(); i++){
                    listSpinner.add(mItems.get(i).getJenis());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditLirikByUser.this, android.R.layout.simple_spinner_item, listSpinner);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spJenis.setAdapter(adapter);
                }
                spJenis.setSelection(((ArrayAdapter<String>)spJenis.getAdapter()).getPosition(Sjenis));
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri imageUri = result.getUri(); //Mengubah data image kedalam Uri
//                fileimage = new File(imageUri.getPath());
//                //Menampilkan Gambar pada ImageView
//                Glide.with(this)
//                        .load(imageUri)
//                        .centerCrop()
//                        .into(imgPref);
//
//            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
//                //Menangani Jika terjadi kesalahan
//                String error = result.getError().toString();
//                Log.d("Exception", error);
//                Toast.makeText(getApplicationContext(), "Crop Image Error", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
//
//            @Override
//            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
//                //Method Ini Digunakan Untuk Menghandle Error pada Image
//            }
//
//            @Override
//            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
//                //Method Ini Digunakan Untuk Menghandle Image
//                switch (type){
//                    case REQUEST_CODE_CAMERA:
//                        CropImage.activity(Uri.fromFile(imageFile))
//                                .setAspectRatio(4,3)
//                                .setRequestedSize(400,300, CropImageView.RequestSizeOptions.RESIZE_EXACT)
//                                .start(EditLirikByUser.this);
//                        break;
//
//                    case REQUEST_CODE_GALLERY:
//                        CropImage.activity(Uri.fromFile(imageFile))
//                                .setAspectRatio(4,3)
//                                .setRequestedSize(400,300, CropImageView.RequestSizeOptions.RESIZE_EXACT)
//                                .start(EditLirikByUser.this);
//                        Log.d("imageFile", imageFile.toString());
//                        Log.d("source", source.toString());
//                        break;
//                }
//            }
//
//            @Override
//            public void onCanceled(EasyImage.ImageSource source, int type) {
//                //Batalkan penanganan, Anda mungkin ingin menghapus foto yang diambil jika dibatalkan
//            }
//        });
//    }
//
//    private void showCameraPreview() {
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(EditLirikByUser.this,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(EditLirikByUser.this,
//                    Manifest.permission.CAMERA)) {
//                Toast.makeText(this,
//                        "Silahkan Buka pengaturan untuk memberi akses Aplikasi Menggunakan Kamera", Toast.LENGTH_SHORT).show();
//            } else {
//                ActivityCompat.requestPermissions(EditLirikByUser.this,
//                        new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
//            }
//        } else {
//            EasyImage.openCamera(EditLirikByUser.this, REQUEST_CODE_CAMERA);
//        }
//    }
//
//    private void checkExternal(){
//        // Here, thisActivity is the current activity
//        if (ContextCompat.checkSelfPermission(EditLirikByUser.this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(EditLirikByUser.this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(this,
//                        "Silahkan Buka pengaturan untuk memberi akses Aplikasi Membaca External penyimpanan", Toast.LENGTH_LONG).show();
//            } else {
//                ActivityCompat.requestPermissions(EditLirikByUser.this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_STORAGE);
//            }
//        } else {
//            EasyImage.openGallery(EditLirikByUser.this, REQUEST_CODE_GALLERY);
//        }
//
//    }

    private void uploadImg(final String judul, final String idjenis, final String penyanyi, final String lirik){
        dialog.show();
        if (fileimage != null) {
            RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), fileimage);
            partImage = MultipartBody.Part.createFormData("image", fileimage.getName(), reqBody);
        }
        RequestBody rJudul = RequestBody.create(MediaType.parse("text/plain"), judul);
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), idjenis);
        RequestBody rPenyanyi = RequestBody.create(MediaType.parse("text/plain"), penyanyi);
        RequestBody rLirik = RequestBody.create(MediaType.parse("text/plain"), lirik);
        RequestBody rIdEmployee = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody rIdLirik = RequestBody.create(MediaType.parse("text/plain"), idlirik);

        ApiRetro api = RetroClient.getApiRetro();
        if (fileimage!=null) {
            data = api.editLirik(rIdLirik, rJudul, rId, rPenyanyi, rLirik, rIdEmployee, partImage);
        } else {
            data = api.editLirikNoImage(rIdLirik, rJudul, rId, rPenyanyi, rLirik, rIdEmployee);
        }

        data.enqueue(new Callback<ResponModel>() {
            @Override
            public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                dialog.dismiss();
                ResponModel model = response.body();
                if (model.getKode()==200){
//                    SettingFragment.getInstance().loadEditProfil();
                    Toast.makeText(EditLirikByUser.this, "Berhasil edit lirik", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditLirikByUser.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(EditLirikByUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        if (!idJenis.equals("0")) {
            uploadImg(judul.getText().toString(), idJenis, penyanyi.getText().toString(), lirik.getText().toString());
        } else {
            Toast.makeText(this, "jenis lagu belum dipilih", Toast.LENGTH_SHORT).show();
        }
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

    private class MyOnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, final long id) {
            String jenis = spJenis.getSelectedItem().toString().trim();
            ApiRetro api = RetroClient.getApiRetro();
            Call<ResponModel> res = api.getIdjenis(jenis);
            res.enqueue(new Callback<ResponModel>() {
                @Override
                public void onResponse(Call<ResponModel> call, Response<ResponModel> response) {
                    idJenis = response.body().getId();
                    Log.d("idlirik", idJenis);
                }

                @Override
                public void onFailure(Call<ResponModel> call, Throwable t) {
                    idJenis = "0";
                    Log.d("idlirik", idJenis);
                }
            });

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}
