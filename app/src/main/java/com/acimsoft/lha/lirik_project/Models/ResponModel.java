package com.acimsoft.lha.lirik_project.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponModel {

    @SerializedName("result")
    List<DataModel> result;

    @SerializedName("result1")
    List<DataModel> result1;

    @SerializedName("kode")
    int kode;

    @SerializedName("message")
    String message;

    @SerializedName("status")
    String status;

    @SerializedName("total_row")
    int total_row;

    @SerializedName("found_like")
    int found_like;

    @SerializedName("found_favorite")
    int found_favorite;
    @SerializedName("viewer")
    String viewer;

    @SerializedName("liked")
    String liked;
    @SerializedName("favorite")
    String favorite;

    @SerializedName("foto")
    String foto;

    @SerializedName("id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<DataModel> getResult1() {
        return result1;
    }

    public void setResult1(List<DataModel> result1) {
        this.result1 = result1;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public int getFound_like() {
        return found_like;
    }

    public void setFound_like(int found_like) {
        this.found_like = found_like;
    }

    public int getFound_favorite() {
        return found_favorite;
    }

    public void setFound_favorite(int found_favorite) {
        this.found_favorite = found_favorite;
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public int getTotal_row() {
        return total_row;
    }

    public void setTotal_row(int total_row) {
        this.total_row = total_row;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataModel> getResult() {
        return result;
    }

    public void setResult(List<DataModel> result) {
        this.result = result;
    }
}
