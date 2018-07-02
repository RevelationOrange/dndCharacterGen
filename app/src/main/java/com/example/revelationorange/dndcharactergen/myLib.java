package com.example.revelationorange.dndcharactergen;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class myLib {
    private static int idStart = 100;
    private static int idCounter = idStart;

    static int getNextID() { return idCounter++; }
    static int getCurID() { return idCounter; }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static TextView makebox(Context c) {
        TextView retBox = new TextView(c);
        retBox.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
//        retBox.setTextColor(0xFFFFFFFF);
//        retBox.setBackgroundResource(R.drawable.black);
        retBox.setId(getNextID());
        return retBox;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static TextView makesmallbox(Context c) {
        TextView retBox = new TextView(c);
//        retBox.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
//        retBox.setTextColor(0xFFFFFFFF);
//        retBox.setBackgroundResource(R.drawable.black);
        retBox.setId(getNextID());
        return retBox;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    static TextView makewhitetextbox(Context c) {
        TextView retBox = new TextView(c);
        retBox.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        retBox.setTextColor(0xFFFFFFFF);
//        retBox.setBackgroundResource(R.drawable.black);
        retBox.setId(getNextID());
        return retBox;
    }

    static Spinner makeDropdown(Context c) {
        Spinner retSpin = new Spinner(c);
        retSpin.setBackgroundColor(0xFF3f48cc);
        retSpin.setId(getNextID());
        return retSpin;
    }

    static Spinner makeAndrDefDd(Context c) {
        Spinner retSpin = new Spinner(c);
//        retSpin.setBackgroundColor(0xFF3f48cc);
        retSpin.setId(getNextID());
        return retSpin;
    }

    static ImageView makeddarrow(Context c) {
        ImageView retImg = new ImageView(c);
        retImg.setImageResource(android.R.drawable.arrow_down_float);
        retImg.setId(getNextID());
        return retImg;
    }

    static Button makePMbutton(Context c, boolean p) {
        Button retButton = new Button(c);
        if (p) { retButton.setText("+"); }
        else { retButton.setText("-"); }
//        retButton.setBackground(R.drawable.);
        retButton.setId(getNextID());
        return retButton;
    }

    static Button makebutton(Context c) {
        Button retButton = new Button(c);
//        retButton.setBackground(R.drawable.);
        retButton.setId(getNextID());
        return retButton;
    }

    public static String loadJSONFromAsset(InputStream is) {
        String json = null;
        try {
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
