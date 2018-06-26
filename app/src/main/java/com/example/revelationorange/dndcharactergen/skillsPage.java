package com.example.revelationorange.dndcharactergen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class skillsPage extends AppCompatActivity {
    private int idStart = 100;
    private int idCounter = idStart;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);

        ConstraintSet set = new ConstraintSet();
        ConstraintLayout skillsConstraint = findViewById(R.id.skillsConstraint);
        skillsConstraint.setPadding(0,0,64*3, 64*3);

        TextView tB = makebox();
        tB.setText(R.string.unusedRanksText);
        skillsConstraint.addView(tB);

        Integer ranks = (MainActivity.globalChar.getSkillRanksPerLvl() + Math.max(MainActivity.globalChar.getBaseStatMods().get(3), 0))*4;
        TextView ranksBox = makebox();
        ranksBox.setText(ranks.toString());
        skillsConstraint.addView(ranksBox);

        set.clone(skillsConstraint);
        set.connect(tB.getId(), ConstraintSet.TOP, skillsConstraint.getId(), ConstraintSet.TOP, 48);
        set.connect(tB.getId(), ConstraintSet.LEFT, skillsConstraint.getId(), ConstraintSet.LEFT, 48);
        set.connect(ranksBox.getId(), ConstraintSet.LEFT, tB.getId(), ConstraintSet.RIGHT, 16*3);
        set.connect(ranksBox.getId(), ConstraintSet.BASELINE, tB.getId(), ConstraintSet.BASELINE);
        set.applyTo(skillsConstraint);

        TextView repBox;
        String skill;
        int prevID, topMargin;
        for (int i = 0; i < MainActivity.globalChar.getSkillList().size(); i++) {
            repBox = makebox();
            skill = MainActivity.globalChar.getSkillList().get(i);
            repBox.setText(skill);
            skillsConstraint.addView(repBox);
            set.clone(skillsConstraint);
            if (i > 0) { prevID = repBox.getId()-1; topMargin = 4; }
            else { prevID = repBox.getId()-2; topMargin = 16; }
            set.connect(repBox.getId(), ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, topMargin * 3);
            set.connect(repBox.getId(), ConstraintSet.LEFT, prevID, ConstraintSet.LEFT, 0);
            set.applyTo(skillsConstraint);
        }
    }

    int getNextID() { return idCounter++; }
    int getCurID() { return idCounter; }

    @RequiresApi(api = Build.VERSION_CODES.M)
    TextView makebox() {
        TextView retBox = new TextView(this);
        retBox.setTextAppearance(R.style.TextAppearance_AppCompat_Large);
        retBox.setTextColor(0xFFFFFFFF);
        retBox.setBackgroundResource(R.drawable.black);
        retBox.setId(getNextID());
        return retBox;
    }

    public void backToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
