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
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.example.revelationorange.dndcharactergen.MainActivity.MARGIN_FACTOR;

public class bardSpells extends AppCompatActivity {
    List<Spinner> selectedSpellsLvl0 = new ArrayList<>();
    List<Spinner> selectedSpellsLvl1 = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bard_spells);
        ConstraintSet cset = new ConstraintSet();
        ConstraintLayout spellsConstraint = findViewById(R.id.bardSpellsConstraint);
        spellsConstraint.setPadding(0, 0, 0, 64*MARGIN_FACTOR);

        int n0SpellsKnown = 4;
        int n1SpellsKnown = 2;
        String lvl0Text = getResources().getString(R.string.level0Text);
        String lvl1Text = getResources().getString(R.string.level1Text);

        List<String> lvl0SpellList = new ArrayList<>();
        List<String> lvl1SpellList = new ArrayList<>();
        ArrayAdapter<String> spells0adapter, spells1adapter;
        Spinner spellSelector;
        View prev;

        TextView headerBox = myLib.makebox(this);
        headerBox.setText(lvl0Text);
        spellsConstraint.addView(headerBox);

        cset.clone(spellsConstraint);
        cset.connect(headerBox.getId(), ConstraintSet.TOP, spellsConstraint.getId(), ConstraintSet.TOP, 16*MARGIN_FACTOR);
        cset.connect(headerBox.getId(), ConstraintSet.LEFT, spellsConstraint.getId(), ConstraintSet.LEFT, 16*MARGIN_FACTOR);
        cset.applyTo(spellsConstraint);
        prev = headerBox;

        try {
            JSONArray spells0 = DndChar.classDict.getJSONObject("Bard").getJSONObject("spells").getJSONArray("0");
            JSONArray spells1 = DndChar.classDict.getJSONObject("Bard").getJSONObject("spells").getJSONArray("1");

            for (int i = 0; i < spells0.length(); i++) { lvl0SpellList.add(spells0.get(i).toString()); }
            for (int i = 0; i < spells1.length(); i++) { lvl1SpellList.add(spells1.get(i).toString()); }

            spells0adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lvl0SpellList);
            spells1adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lvl1SpellList);

            // for x lvl0 spells, make spinner, set adapter, layout stuff
            for (int i = 0; i < n0SpellsKnown; i++) {
                spellSelector = myLib.makeAndrDefDd(this);
                spellSelector.setAdapter(spells0adapter);
                spellSelector.setBackgroundResource(android.R.drawable.btn_default_small);
                spellsConstraint.addView(spellSelector);

                cset.clone(spellsConstraint);
                cset.connect(spellSelector.getId(), ConstraintSet.TOP, prev.getId(), ConstraintSet.BOTTOM, 16*MARGIN_FACTOR);
                cset.connect(spellSelector.getId(), ConstraintSet.LEFT, headerBox.getId(), ConstraintSet.LEFT, 16*MARGIN_FACTOR);
                cset.applyTo(spellsConstraint);
                prev = spellSelector;
                selectedSpellsLvl0.add(spellSelector);
            }

            headerBox = myLib.makebox(this);
            headerBox.setText(lvl1Text);
            spellsConstraint.addView(headerBox);

            cset.clone(spellsConstraint);
            cset.connect(headerBox.getId(), ConstraintSet.TOP, prev.getId(), ConstraintSet.BOTTOM, 16*MARGIN_FACTOR);
            cset.connect(headerBox.getId(), ConstraintSet.LEFT, spellsConstraint.getId(), ConstraintSet.LEFT, 16*MARGIN_FACTOR);
            cset.applyTo(spellsConstraint);
            prev = headerBox;
            // for y lvl1 spells
            for (int i = 0; i < n1SpellsKnown; i++) {
                spellSelector = myLib.makeAndrDefDd(this);
                spellSelector.setAdapter(spells1adapter);
                spellSelector.setBackgroundResource(android.R.drawable.btn_default_small);
                spellsConstraint.addView(spellSelector);

                cset.clone(spellsConstraint);
                cset.connect(spellSelector.getId(), ConstraintSet.TOP, prev.getId(), ConstraintSet.BOTTOM, 16*MARGIN_FACTOR);
                cset.connect(spellSelector.getId(), ConstraintSet.LEFT, headerBox.getId(), ConstraintSet.LEFT, 16*MARGIN_FACTOR);
                cset.applyTo(spellsConstraint);
                prev = spellSelector;
                selectedSpellsLvl1.add(spellSelector);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveSpells(View v) {
        String spellname;
        MainActivity.globalChar.clearSpells();
        for (Spinner selectedSpell0: selectedSpellsLvl0) {
            spellname = selectedSpell0.getSelectedItem().toString();
            if (!MainActivity.globalChar.getSpellList(0).contains(spellname)) {
                MainActivity.globalChar.addSpell(spellname, 0);
            }
        }
        for (Spinner selectedSpell1: selectedSpellsLvl1) {
            spellname = selectedSpell1.getSelectedItem().toString();
            if (!MainActivity.globalChar.getSpellList(1).contains(spellname)) {
                MainActivity.globalChar.addSpell(spellname, 1);
            }
        }
    }

    public void backToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
