package com.acimsoft.lha.lirik_project.Api;

import com.acimsoft.lha.lirik_project.Models.ResponModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiRetro {

    @GET("lirik/getTopViewer")
    Call<ResponModel> getTopViewerLike();

    @GET("lirik/getTopLiked")
    Call<ResponModel> getTopLiked();

    @GET("lirik/getMyFavorite")
    Call<ResponModel> getMyFavorite(@Query("iduniq") String iduniq);


    @GET("lirik/listLirik")
    Call<ResponModel> getLirik(@Query("iduniq") String iduniq,
                               @Query("q") String keyword,
                               @Query("page") int page,
                               @Query("perpage") int perpage);

    @GET("lirik/updateView")
    Call<ResponModel> getdata(@Query("id") String id);

    @GET("lirik/getDataFavLike")
    Call<ResponModel> getdataFavLike(@Query("iduniq") String iduniq,
                                     @Query("id") String id);


    @FormUrlEncoded
    @POST("lirik/DeleteMyFavoriteById")
    Call<ResponModel> delfavorite (@Field("iduniq") String iduniq,
                                   @Field("id_lirik") String idlirik);

    @FormUrlEncoded
    @POST("lirik/addMyFavorite")
    Call<ResponModel> addfavorite (@Field("iduniq") String iduniq,
                                   @Field("id_lirik") String idlirik);

    @FormUrlEncoded
    @POST("lirik/DeleteMyLike")
    Call<ResponModel> delLike (@Field("iduniq") String iduniq,
                               @Field("id_lirik") String idlirik);

    @FormUrlEncoded
    @POST("lirik/addLike")
    Call<ResponModel> addLike (@Field("iduniq") String iduniq,
                               @Field("id_lirik") String idlirik);

    @FormUrlEncoded
    @POST("login/auth")
    Call<ResponModel> getLogin (@Field("user") String username,
                                @Field("pass") String password);

    @FormUrlEncoded
    @POST("lirik/addToHistory")
    Call<ResponModel> addHistory (@Field("iduniq") String iduniq,
                                  @Field("id_lirik") String idlirik);

    @GET("lirik/tampilHistory")
    Call<ResponModel> getMyHistory (@Query("iduniq") String iduniq);

    @FormUrlEncoded
    @POST("login/registerUser")
    Call<ResponModel> register (@Field("iduniq") String iduniq,
                                @Field("email") String email,
                                @Field("passwords") String password,
                                @Field("nama") String nama);

    @GET("login/verify")
    Call<ResponModel> toVerifyRegister (@Query("token") String token,
                                        @Query("email") String email);

    @GET("login/verifyReset")
    Call<ResponModel> toVerifyReset (@Query("token") String token,
                                     @Query("email") String email);

    @FormUrlEncoded
    @POST("login/batalAktivasi")
    Call<ResponModel> cancelRegister (@Field("email") String email);

    @FormUrlEncoded
    @POST("login/changePasswordReset")
    Call<ResponModel> changePass (@Field("email") String email,
                                  @Field("pass") String password);


    @GET("login/requestLupaPassword")
    Call<ResponModel> reqLupapass (@Query("email") String email);

    @Multipart
    @POST("lirik/changePhoto")
    Call<ResponModel> changeProfil (@Part("email") RequestBody email,
                                    @Part("nama") RequestBody nama,
                                    @Part MultipartBody.Part image);

    @Multipart
    @POST("lirik/changePhoto")
    Call<ResponModel> changeProfilNoImage (@Part("email") RequestBody email,
                                           @Part("nama") RequestBody nama);

    @GET("lirik/getJenislaguSpinner")
    Call<ResponModel> getAllJenis ();

    @GET("lirik/getJenislaguSpinner")
    Call<ResponModel> getIdjenis (@Query("jenis_lagu") String jenis);

    @Multipart
    @POST("lirik/tulisLirik")
    Call<ResponModel> writeLirik (@Part("judul") RequestBody judul,
                                  @Part("id_jenis") RequestBody idjenis,
                                  @Part("vocal") RequestBody penyanyi,
                                  @Part("lirik") RequestBody lirik,
                                  @Part("id_employee") RequestBody idpenulis,
                                  @Part MultipartBody.Part image);

    @Multipart
    @POST("lirik/tulisLirik")
    Call<ResponModel> writeLirikNoImage (@Part("judul") RequestBody judul,
                                          @Part("id_jenis") RequestBody idjenis,
                                          @Part("vocal") RequestBody penyanyi,
                                          @Part("lirik") RequestBody lirik,
                                          @Part("id_employee") RequestBody idpenulis);


    @GET("lirik/listLirikByUser")
    Call<ResponModel> getLirikbyId (@Query("id_employee") String id_user,
                                    @Query("q") String keyword,
                                    @Query("page") int page,
                                    @Query("perpage") int perpage);

    @Multipart
    @POST("lirik/editLirik")
    Call<ResponModel> editLirik  (@Part("id_lirik") RequestBody idlirik,
                                  @Part("judul") RequestBody judul,
                                  @Part("id_jenis") RequestBody idjenis,
                                  @Part("vocal") RequestBody penyanyi,
                                  @Part("lirik") RequestBody lirik,
                                  @Part("id_employee") RequestBody idpenulis,
                                  @Part MultipartBody.Part image);

    @Multipart
    @POST("lirik/editLirik")
    Call<ResponModel> editLirikNoImage  (@Part("id_lirik") RequestBody idlirik,
                                         @Part("judul") RequestBody judul,
                                         @Part("id_jenis") RequestBody idjenis,
                                         @Part("vocal") RequestBody penyanyi,
                                         @Part("lirik") RequestBody lirik,
                                         @Part("id_employee") RequestBody idpenulis);


    @FormUrlEncoded
    @POST("lirik/addReport")
    Call<ResponModel> sendReport (
            @Field("id_lirik") String idlirik,
            @Field("iduniq") String iduniq,
            @Field("keterangan") String keterangan
            );
}
