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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class wizardSpells extends AppCompatActivity {
    List<Spinner> selectedSpellsLvl1 = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_spells);

        Integer n1stLvlSpells = 3 + MainActivity.globalChar.getBaseStatMods().get(3);
        String lvl0Text = "Level 0";
        String lvl1Text = "Level 1";
        ConstraintSet set = new ConstraintSet();
        ConstraintLayout spellsConstraint = findViewById(R.id.wizSpellsConstraint);
        spellsConstraint.setPadding(0,0,0, (64+64)*3);

        TextView lvl0TextBox = myLib.makebox(this);
        lvl0TextBox.setText(lvl0Text);
        spellsConstraint.addView(lvl0TextBox);

        TextView lvl1TextBox = myLib.makebox(this);
        lvl1TextBox.setText(lvl1Text);
        spellsConstraint.addView(lvl1TextBox);

        Button save = findViewById(R.id.saveSpells);
        save.setText(R.string.saveSelectedSpellsText);
//        spellsConstraint.addView(save);

        set.clone(spellsConstraint);
        set.connect(lvl0TextBox.getId(), ConstraintSet.TOP, spellsConstraint.getId(), ConstraintSet.TOP, 16*3);
        set.connect(lvl0TextBox.getId(), ConstraintSet.LEFT, spellsConstraint.getId(), ConstraintSet.LEFT, 16*3);
        set.applyTo(spellsConstraint);

        JSONArray wizSpells0List;
        View prevBox = lvl0TextBox;
        TextView curBox;
        int longestSpellRightSide = lvl0Text.length();
        TextView longestBox = lvl0TextBox;
        try {
            wizSpells0List = DndChar.classDict.getJSONObject("Wizard").getJSONObject("spells").getJSONArray("0");
            String spellName;
            for (int i = 0; i < wizSpells0List.length(); i++) {
                spellName = wizSpells0List.getString(i);
                curBox = myLib.makesmallbox(this);
                curBox.setText(spellName);
                spellsConstraint.addView(curBox);
                set.clone(spellsConstraint);
                set.connect(curBox.getId(), ConstraintSet.TOP, prevBox.getId(), ConstraintSet.BOTTOM, 16*3);
                set.connect(curBox.getId(), ConstraintSet.LEFT, lvl0TextBox.getId(), ConstraintSet.LEFT, 16*3);
                set.applyTo(spellsConstraint);


                if (curBox.getText().length() > longestSpellRightSide) {
                    longestSpellRightSide = curBox.getText().length();
                    longestBox = curBox;
                }
                prevBox = curBox;
            }

            set.connect(save.getId(), ConstraintSet.TOP, spellsConstraint.getId(), ConstraintSet.TOP, 16*3);
            set.connect(save.getId(), ConstraintSet.LEFT, longestBox.getId(), ConstraintSet.RIGHT, 8*3);
            set.connect(lvl1TextBox.getId(), ConstraintSet.TOP, save.getId(), ConstraintSet.BOTTOM, 16*3);
            set.connect(lvl1TextBox.getId(), ConstraintSet.LEFT, longestBox.getId(), ConstraintSet.RIGHT, 8*3);
            set.applyTo(spellsConstraint);

            ArrayAdapter<String> lvl1Spells;
            Spinner lvl1spellsDropdown;
            prevBox = lvl1TextBox;
            List<String> spellList = new ArrayList<>();
            JSONArray spLJson = DndChar.classDict.getJSONObject("Wizard").getJSONObject("spells").getJSONArray("1");
            for (int i = 0; i < spLJson.length(); i++) {
                spellList.add(spLJson.get(i).toString());
            }
            for (int i = 0; i < n1stLvlSpells; i++) {
                lvl1Spells = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spellList);
                lvl1spellsDropdown = myLib.makeDropdown(this);
                lvl1spellsDropdown.setAdapter(lvl1Spells);

                spellsConstraint.addView(lvl1spellsDropdown);
                set.clone(spellsConstraint);
                set.connect(lvl1spellsDropdown.getId(), ConstraintSet.TOP, prevBox.getId(), ConstraintSet.BOTTOM, 16*3);
                set.connect(lvl1spellsDropdown.getId(), ConstraintSet.LEFT, lvl1TextBox.getId(), ConstraintSet.LEFT, 16*3);
                set.applyTo(spellsConstraint);

                prevBox = lvl1spellsDropdown;
                selectedSpellsLvl1.add(lvl1spellsDropdown);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveSpells(View v) {
        String spellName;
        MainActivity.globalChar.clearSpells();
        for (Spinner sp: selectedSpellsLvl1) {
            spellName = sp.getSelectedItem().toString();
            if (!MainActivity.globalChar.getSpellList().get(1).contains(spellName)) {
                MainActivity.globalChar.addSpell(spellName, 1);
            }
        }
    }

    public void backToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // int mod gives +spells known, bonus spells -> spells per day, from table
}
