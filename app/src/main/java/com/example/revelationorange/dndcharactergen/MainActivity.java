package com.example.revelationorange.dndcharactergen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DndChar globalChar = new DndChar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void rollStats(View v) {
        globalChar.rollStats();
        List<Integer> bStats = globalChar.getBaseStats();
        List<Integer> bStatMods = globalChar.getBaseStatMods();

        TextView statBox = findViewById(R.id.strVal);
        statBox.setText(String.format(bStats.get(0).toString()));
        statBox = findViewById(R.id.dexVal);
        statBox.setText(String.format(bStats.get(1).toString()));
        statBox = findViewById(R.id.conVal);
        statBox.setText(String.format(bStats.get(2).toString()));
        statBox = findViewById(R.id.intVal);
        statBox.setText(String.format(bStats.get(3).toString()));
        statBox = findViewById(R.id.wisVal);
        statBox.setText(String.format(bStats.get(4).toString()));
        statBox = findViewById(R.id.chaVal);
        statBox.setText(String.format(bStats.get(5).toString()));

        int mod;
        String pm;
        statBox = findViewById(R.id.strMod);
        mod = bStatMods.get(0);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
        statBox = findViewById(R.id.dexMod);
        mod = bStatMods.get(1);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
        statBox = findViewById(R.id.conMod);
        mod = bStatMods.get(2);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
        statBox = findViewById(R.id.intMod);
        mod = bStatMods.get(3);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
        statBox = findViewById(R.id.wisMod);
        mod = bStatMods.get(4);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
        statBox = findViewById(R.id.chaMod);
        mod = bStatMods.get(5);
        pm = mod<0 ? "" : "+";
        statBox.setText(String.format("%s%s", pm, mod));
    }
}
