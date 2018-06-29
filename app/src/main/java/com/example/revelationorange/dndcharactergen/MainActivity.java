package com.example.revelationorange.dndcharactergen;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private static final String CHANNEL_ID = "dndnotif";
//    public static final String EXTRA_CHARACTER = "com.example.myfirstapp.CHARACTER";
    public static DndChar globalChar = new DndChar();
    List<TextView> statBoxes = new ArrayList<>();
    List<TextView> statModBoxes = new ArrayList<>();
    Spinner raceDropdown;
    ArrayAdapter<String> adapter0;
    Spinner classDropdown;
    ArrayAdapter<String> adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        raceDropdown = findViewById(R.id.raceSelect);
        adapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DndChar.races);
        classDropdown = findViewById(R.id.classSelect);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DndChar.classes);
        setupBoxes();
        setupUI(findViewById(R.id.parent));
//        createNotificationChannel();
        if (globalChar.isRolled()) {
            List<Integer> stats = globalChar.getBaseStats();
            List<Integer> statMods = globalChar.getBaseStatMods();
            for (int i = 0; i < statBoxes.size(); i++) {
                statBoxes.get(i).setText(String.format(stats.get(i).toString()));
                statModBoxes.get(i).setText(String.format(statMods.get(i).toString()));
            }
            classDropdown.setSelection(globalChar.getChClassID());
            raceDropdown.setSelection(globalChar.getRaceID());
        }
        else {
            DndChar.setup(getResources().openRawResource(R.raw.skills), getResources().openRawResource(R.raw.feats));
        }
        System.out.println(DndChar.casterList);
        System.out.println(DndChar.casterList.contains("Cleric"));
        System.out.println("caster list");
    }

    /*
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    */


    void setupBoxes() {
        statBoxes.clear();
        statBoxes.add((TextView) findViewById(R.id.strVal));
        statBoxes.add((TextView) findViewById(R.id.dexVal));
        statBoxes.add((TextView) findViewById(R.id.conVal));
        statBoxes.add((TextView) findViewById(R.id.intVal));
        statBoxes.add((TextView) findViewById(R.id.wisVal));
        statBoxes.add((TextView) findViewById(R.id.chaVal));

        statModBoxes.clear();
        statModBoxes.add((TextView) findViewById(R.id.strMod));
        statModBoxes.add((TextView) findViewById(R.id.dexMod));
        statModBoxes.add((TextView) findViewById(R.id.conMod));
        statModBoxes.add((TextView) findViewById(R.id.intMod));
        statModBoxes.add((TextView) findViewById(R.id.wisMod));
        statModBoxes.add((TextView) findViewById(R.id.chaMod));

        raceDropdown.setAdapter(adapter0);
        classDropdown.setAdapter(adapter1);
    }

    public void rollStats(View v) {
        globalChar.rollStats();
        List<Integer> bStats = globalChar.getBaseStats();
        List<Integer> bStatMods = globalChar.getBaseStatMods();
        TextView textBox;
        int mod;
        String pm;

        for (int i = 0; i < statBoxes.size(); i++) {
            textBox = statBoxes.get(i);
            textBox.setText(String.format(bStats.get(i).toString()));

            textBox = statModBoxes.get(i);
            mod = bStatMods.get(i);
            pm = (mod < 0) ? "" : "+";
            textBox.setText(String.format("%s%s", pm, mod));
        }

        globalChar.setChClass(classDropdown.getSelectedItem().toString(), classDropdown.getSelectedItemPosition());
        globalChar.setRace(raceDropdown.getSelectedItem().toString(), raceDropdown.getSelectedItemPosition());
    }

    public void goToSkills(View v) {
        if (globalChar.isRolled()) {
            globalChar.setChClass(classDropdown.getSelectedItem().toString(), classDropdown.getSelectedItemPosition());
            globalChar.setRace(raceDropdown.getSelectedItem().toString(), raceDropdown.getSelectedItemPosition());
            Intent intent = new Intent(this, skillsPage.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"You have to roll stats before dealing with skills", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToFeats(View v) {
        if (globalChar.isRolled()) {
            globalChar.setChClass(classDropdown.getSelectedItem().toString(), classDropdown.getSelectedItemPosition());
            globalChar.setRace(raceDropdown.getSelectedItem().toString(), raceDropdown.getSelectedItemPosition());
            Intent intent = new Intent(this, featsPage.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"You have to roll stats before dealing with feats", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSpells(View v) {
        if (globalChar.isRolled()) {
            if (globalChar.isCaster()) {
                // intent stuff
            } else {
                Toast.makeText(this, "You must be a spellcaster class in order to use spells", Toast.LENGTH_SHORT).show();
            }
        }
        else { Toast.makeText(this,"You have to roll stats before dealing with spells", Toast.LENGTH_SHORT).show(); }
    }

    // straight from stackoverflow
    ///
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this, true);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard (Activity act, boolean hide) {
        InputMethodManager inputManager = (InputMethodManager)
                act.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (hide) {
            inputManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        else {
            inputManager.showSoftInputFromInputMethod(act.getCurrentFocus().getWindowToken(),
                    0);
        }
    }
    ///
}
