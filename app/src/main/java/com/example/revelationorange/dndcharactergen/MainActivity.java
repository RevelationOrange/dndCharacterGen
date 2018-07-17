package com.example.revelationorange.dndcharactergen;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
//    private static final String CHANNEL_ID = "dndnotif";
//    public static final String EXTRA_CHARACTER = "com.example.myfirstapp.CHARACTER";
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 7;
    public static final int MARGIN_FACTOR = 3;
    public static DndChar globalChar = new DndChar();
    public File saveDir;
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
        saveDir = new File(getPublicStorageDir("dndChars").toString());
        raceDropdown = findViewById(R.id.raceSelect);
        adapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DndChar.races);
        classDropdown = findViewById(R.id.classSelect);
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, DndChar.classes);
        setupBoxes();
        ConstraintLayout mainConstraint = findViewById(R.id.mainConstr);
        mainConstraint.setPadding(0,0,0, (64)*MARGIN_FACTOR);
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
            DndChar.setup(getResources().openRawResource(R.raw.skills), getResources().openRawResource(R.raw.feats), getResources().openRawResource(R.raw.classdata));
        }
    }

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
            String cl = classDropdown.getSelectedItem().toString();
            globalChar.setChClass(cl, classDropdown.getSelectedItemPosition());
            globalChar.setRace(raceDropdown.getSelectedItem().toString(), raceDropdown.getSelectedItemPosition());
            if (globalChar.isCaster()) {
                Class intentClass;
                switch (cl) {
                    case "Wizard":
                        intentClass = wizardSpells.class;
                        break;
                    case "Sorcerer":
                        intentClass = sorcererSpells.class;
                        break;
                    case "Druid":
                        intentClass = druidSpells.class;
                        break;
                    case "Cleric":
                        intentClass = clericSpells.class;
                        break;
                    case "Bard":
                        intentClass = bardSpells.class;
                        break;
                    default:
                        return;
                }

                Intent intent = new Intent(this, intentClass);
                startActivity(intent);
                // intent stuff
            } else {
                Toast.makeText(this, "You must be a spellcaster class in order to use spells", Toast.LENGTH_SHORT).show();
            }
        }
        else { Toast.makeText(this,"You have to roll stats before dealing with spells", Toast.LENGTH_SHORT).show(); }
    }

    public void saveCharacter(View v) {
        if (checkPermissions()) {
            if (!saveDir.exists()) { saveDir.mkdir(); }
            String charFilename;
            charFilename = ((TextView) findViewById(R.id.charNameEnter)).getText().toString();
            if (charFilename.length() > 0) {
                globalChar.setChName(charFilename);
                charFilename += ".csv";
                File saveFile = new File(saveDir + File.separator + charFilename);

                List<Integer> bStats = globalChar.getBaseStats();
                List<Integer> bStatMods = globalChar.getBaseStatMods();

                CSVBuilder csvString = new CSVBuilder();
                csvString.addLine(globalChar.getChName());
                csvString.addLine("Level " + globalChar.getLevel(), globalChar.getRace(), globalChar.getChClass());
                for (int i = 0; i < DndChar.statNamesShort.length; i++) {
                    csvString.addLine(DndChar.statNamesShort[i], bStats.get(i), bStatMods.get(i));
                }
                csvString.addLine("Skill", "ranks");
                for (int i = 0; i < globalChar.getSkillList().size(); i++) {
                    csvString.addLine(globalChar.getSkillList().get(i), globalChar.getSkillRanks().get(i));
                }
                csvString.addLine("Feats");
                for (String fn: globalChar.getFeatsList()) { csvString.addLine(fn); }
                if (globalChar.isCaster()) {
                    csvString.addLine("Spells");
                    csvString.addLine("Level 0");
                    for (String sn: globalChar.getSpellList(0)) { csvString.addLine(sn); }
                    csvString.addLine("Level 1");
                    for (String sn: globalChar.getSpellList(1)) { csvString.addLine(sn); }
                }
                try {
                    FileWriter fw = new FileWriter(saveFile);
                    fw.write(csvString.getStr());
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else { Toast.makeText(this, "You have to enter a character name to save a file", Toast.LENGTH_SHORT).show(); }
        }
    }

    public void sendCharacter(View v) {
        if (checkPermissions()) {
            Intent intent = new Intent(this, sendPage.class);
            intent.putExtra("fileLoc", saveDir.toString());
            startActivity(intent);
        }
    }



    boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        }
        else { return true; }
    }

    public File getPublicStorageDir(String dirname) {
        // Get the directory for the user's public documents directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), dirname);
        if (!file.mkdirs()) {
            System.out.println("Directory not created");
        }
        return file;
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
