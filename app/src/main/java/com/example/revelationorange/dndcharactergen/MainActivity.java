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
        List<Integer> bStats = globalChar.getBaseStats();

        TextView statBox = findViewById(R.id.strVal);
        statBox.setText(bStats.get(0));
    }
}
