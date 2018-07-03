package com.example.revelationorange.dndcharactergen;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.revelationorange.dndcharactergen.MainActivity.MARGIN_FACTOR;

public class featsPage extends AppCompatActivity {
    List<Spinner> selectedFeats = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feats_page);

        Integer nFeats = 1;
        if (MainActivity.globalChar.getRace().equals("Human")) { nFeats++; }
        if (MainActivity.globalChar.getChClass().equals("Fighter")) { nFeats++; }

        ConstraintSet set = new ConstraintSet();
        ConstraintLayout featsConstraint = findViewById(R.id.featsConstraint);
        Spinner featList;
        ArrayAdapter<String> feats;
        ImageView ddArrow;

        for (int i = 0; i < nFeats; i++) {
            featList = myLib.makeDropdown(this);
            feats = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DndChar.featNames);
            featList.setAdapter(feats);
            ddArrow = myLib.makeddarrow(this);
            // add
            featsConstraint.addView(featList);
            featsConstraint.addView(ddArrow);

            // clone
            set.clone(featsConstraint);
            // connect
            set.connect(featList.getId(), ConstraintSet.TOP, featsConstraint.getId(), ConstraintSet.TOP, (1+i*4)*(16)*MARGIN_FACTOR);
            set.connect(featList.getId(), ConstraintSet.LEFT, featsConstraint.getId(), ConstraintSet.LEFT, (16)*MARGIN_FACTOR);
            set.connect(ddArrow.getId(), ConstraintSet.TOP, featList.getId(), ConstraintSet.TOP, 4*MARGIN_FACTOR);
            set.connect(ddArrow.getId(), ConstraintSet.BOTTOM, featList.getId(), ConstraintSet.BOTTOM, 4*MARGIN_FACTOR);
            set.connect(ddArrow.getId(), ConstraintSet.RIGHT, featList.getId(), ConstraintSet.RIGHT, 8*MARGIN_FACTOR);
            // applyto
            set.applyTo(featsConstraint);

            selectedFeats.add(featList);
        }
    }

    public void saveFeats(View v) {
        String ftName;
        MainActivity.globalChar.clearFeats();
        for (Spinner f: selectedFeats) {
            ftName = f.getSelectedItem().toString();
            if (!MainActivity.globalChar.getFeatsList().contains(ftName)) {
                MainActivity.globalChar.addFeat(ftName);
            }
        }
    }

    public void backToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
