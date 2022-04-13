package com.acimsoft.lha.lirik_project.Config;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private final int MODE_PRIVATE = 0;
    public final static String ID = "id";
    public final static String STATUS = "status";
    public final static String NAME = "nama";
    public final static String TGL = "tgl";
    public final static String URL_PROF = "url";
    public final static String EMAIL = "email";
    public final static String VALSIZE = "valZize";

    public final static String SHARED_NAME = "login";


    public SessionManager(Context context) {
        this.context = context;
        sp = context.getSharedPreferences(SHARED_NAME, MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setSess(String id, String nama, String status, String tgl, String url, String email){
        editor.putString(ID, id);
        editor.putString(NAME, nama);
        editor.putString(STATUS, status);
        editor.putString(TGL, tgl);
        editor.putString(URL_PROF, url);
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public HashMap<String, String> getSess(){
        HashMap<String, String> maps = new HashMap<>();
        maps.put(ID, sp.getString(ID, null));
        maps.put(NAME, sp.getString(NAME, null));
        maps.put(STATUS, sp.getString(STATUS, null));
        maps.put(TGL, sp.getString(TGL, null));
        maps.put(URL_PROF, sp.getString(URL_PROF, null));
        maps.put(EMAIL, sp.getString(EMAIL, null));
        return maps;
    }

    public void clearAll(){
        editor.clear().commit();
        editor.remove(SHARED_NAME).commit();
    }


    public void setValueFont(String size){
        editor.putString(VALSIZE, size);
        editor.commit();
    }

    public HashMap<String, String> getValueFont(){
        HashMap<String, String> maps = new HashMap<>();
        maps.put(VALSIZE, sp.getString(VALSIZE, "90"));
        return maps;
    }

}
