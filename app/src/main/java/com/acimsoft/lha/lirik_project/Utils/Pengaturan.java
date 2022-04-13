package com.acimsoft.lha.lirik_project.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class Pengaturan {

    Random random;
    public static String URL_PRIVASI = "https://web.facebook.com/?_rdc=1&_rdr";
    public static String URL_TOU = "https://web.facebook.com/?_rdc=1&_rdr";

    public static TextWatcher onTextChangedListener(final EditText ed){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ed.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    ed.setText(formattedString);
                    ed.setSelection(ed.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                ed.addTextChangedListener(this);
            }
        };
    }

    public static String ubahFormatTgl(String _tgl){
        String[] tgl = _tgl.split("-");
        return tgl[2] + "-" + tgl[1] + "-" +tgl[0];
    }

    public static String ubahFormatLiter(Float liter){
        DecimalFormat df = new DecimalFormat("#.000");
        String vol = df.format(liter).replace(",", ".");
        return vol;
    }

    public static String Ubah_FormatRupiah(String nilai){
        return nilai.replace(",", "");
    }

    public static String hitung_Total(Float liter, int harga){

        DecimalFormat tot = new DecimalFormat("#,###");

        String total_harga = String.valueOf(tot.format(harga*liter).replace(".",","));
        return  total_harga;
    }

    public static String hitung_Liter(String total, int harga){

        int tot =  Integer.parseInt(Ubah_FormatRupiah(total));

        float liter = (float) tot / harga;

        DecimalFormat ltr = new DecimalFormat("#.000");

        String tot_ltr = String.valueOf(ltr.format(liter).replace(",","."));
        return tot_ltr;
    }

    public static String isiShift(String jam){
        String[] _jam = jam.split(":");

        int hours = Integer.parseInt(_jam[0]);
        if (hours >= 6 && hours < 14 ){
            return  "1";
        } else if (hours >= 14 && hours < 22){
            return  "2";
        } else {
            return  "3";
        }
    }

    public static String[] jams(String jam){
        String[] jams = jam.split(":");
        return jams;
    }

    public static String formatRupiah(int angka)
    {
        DecimalFormat tot = new DecimalFormat("#,###");
        return  String.valueOf(tot.format(angka)).replace(".", ",");
    }

    public static String formatLiter(Float liter)
    {
        DecimalFormat ltr = new DecimalFormat("#.000");
        return String.valueOf(ltr.format(liter)).replace(",", ".");
    }

    public static int getRundom(int selisihhari, int selisihmenit) {
        int min;
        int max;
        final int random;

        if (selisihhari > 0) {
            min = 50;
            max = 120;
            random = new Random().nextInt((max - min) + 1) + min;
            return random;

        } else if (selisihhari == 0 && selisihmenit > 4 && selisihmenit < 10){
            min = 3;
            max = 5;
            random = new Random().nextInt((max - min) + 1) + min;
            return random;

        }else if (selisihhari == 0 && selisihmenit > 9 && selisihmenit < 15){
            min = 5;
            max = 10;
            random = new Random().nextInt((max - min) + 1) + min;
            return random;

        } else if (selisihhari == 0 && selisihmenit <= 4){
            return 1;

        } else if (selisihhari == 0 && selisihmenit > 15){
            min = 11;
            max = 25;
            random = new Random().nextInt((max - min) + 1) + min;
            return random;
        }
        return 1;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }


    public static class CheckNetClass {

        public static Boolean checknetwork(Context mContext) {

            NetworkInfo info = ((ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return false;
            }
            if (info.isRoaming()) {
                // here is the roaming option, you can change it if you want to
                // disable internet while roaming, just return false
                return true;
            }

            return true;

        }
    }
}
