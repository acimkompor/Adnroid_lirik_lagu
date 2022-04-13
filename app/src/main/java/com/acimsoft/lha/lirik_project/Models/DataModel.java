package com.acimsoft.lha.lirik_project.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataModel implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("judul")
    String judul;

    @SerializedName("vocal")
    String vocal;
    @SerializedName("image")
    String image;
    @SerializedName("jenis")
    String jenis;
    @SerializedName("liked")
    String liked;
    @SerializedName("profil")
    String profil;

    @SerializedName("nama")
    String nama;
    @SerializedName("create")
    String create;

    @SerializedName("found_like")
    String found_like;

    @SerializedName("found_favorite")
    String found_favorite;
    @SerializedName("viewer")
    String viewer;

    @SerializedName("lirik")
    String lirik;

    public String getProfil() {
        return profil;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public String getLirik() {
        return lirik;
    }

    public void setLirik(String lirik) {
        this.lirik = lirik;
    }

    public String getFound_like() {
        return found_like;
    }

    public void setFound_like(String found_like) {
        this.found_like = found_like;
    }

    public String getFound_favorite() {
        return found_favorite;
    }

    public void setFound_favorite(String found_favorite) {
        this.found_favorite = found_favorite;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getVocal() {
        return vocal;
    }

    public void setVocal(String vocal) {
        this.vocal = vocal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }
}
