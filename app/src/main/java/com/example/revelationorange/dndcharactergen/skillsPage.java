package com.example.revelationorange.dndcharactergen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.revelationorange.dndcharactergen.MainActivity.MARGIN_FACTOR;

public class skillsPage extends AppCompatActivity {
    private int idStart = 100;
    private int idCounter = idStart;
    List<TextView> skillRows = new ArrayList<>();
    TextView unusedRanksBox;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_page);

        ConstraintSet set = new ConstraintSet();
        ConstraintLayout skillsConstraint = findViewById(R.id.skillsConstraint);
        skillsConstraint.setPadding(0,0,0, (64+64)*MARGIN_FACTOR);

        TextView tB = findViewById(R.id.unusedRanksTextbox);
        tB.setText(R.string.unusedRanksText);

        Integer unusedRanks = (MainActivity.globalChar.getSkillRanksPerLvl() + Math.max(MainActivity.globalChar.getBaseStatMods().get(3), 0));
        unusedRanksBox = findViewById(R.id.unusedRanksVal);
        unusedRanksBox.setText(unusedRanks.toString());

        TextView skillBox, ranksBox;
        Button plus, minus;
        String skill, ranks;
        List<TextView> skillBoxAnchors = new ArrayList<>();
        int prevID, topMargin, midMargin = 4, leftMargin;
        for (int i = 0; i < MainActivity.globalChar.getSkillList().size(); i++) {
            skillBox = myLib.makewhitetextbox(this);
            if (i > 0) { prevID = skillBoxAnchors.get(skillBoxAnchors.size()-1).getId(); topMargin = 12; leftMargin = 16; }
            else { prevID = tB.getId(); topMargin = 16; leftMargin = 16; }
            skill = MainActivity.globalChar.getSkillList().get(i);
            skillBox.setText(skill);

            ranksBox = myLib.makewhitetextbox(this);
            ranks = MainActivity.globalChar.getSkillRanks().get(i).toString();
            ranksBox.setText(ranks);

            plus = myLib.makePMbutton(this, true);
            minus = myLib.makePMbutton(this, false);
//            plus = makefabbutton(true);
//            minus = makefabbutton(false);
            plus.setOnClickListener(addSkillPoint(plus, i));
            minus.setOnClickListener(subSkillPoint(minus, i));

            skillsConstraint.addView(skillBox);
            skillsConstraint.addView(ranksBox);
            skillsConstraint.addView(plus);
            skillsConstraint.addView(minus);

            set.clone(skillsConstraint);
            set.connect(skillBox.getId(), ConstraintSet.TOP, prevID, ConstraintSet.BOTTOM, topMargin * MARGIN_FACTOR);
            set.connect(skillBox.getId(), ConstraintSet.LEFT, skillsConstraint.getId(), ConstraintSet.LEFT, leftMargin*MARGIN_FACTOR);
            set.connect(minus.getId(), ConstraintSet.TOP, skillBox.getId(), ConstraintSet.BOTTOM, midMargin*MARGIN_FACTOR);
            set.connect(minus.getId(), ConstraintSet.LEFT, skillBox.getId(), ConstraintSet.LEFT, 0);
            set.connect(ranksBox.getId(), ConstraintSet.TOP, skillBox.getId(), ConstraintSet.BOTTOM, midMargin*MARGIN_FACTOR);
            set.connect(ranksBox.getId(), ConstraintSet.LEFT, minus.getId(), ConstraintSet.RIGHT, 16*MARGIN_FACTOR);
            set.connect(plus.getId(), ConstraintSet.TOP, skillBox.getId(), ConstraintSet.BOTTOM, midMargin*MARGIN_FACTOR);
            set.connect(plus.getId(), ConstraintSet.LEFT, ranksBox.getId(), ConstraintSet.RIGHT, 16*MARGIN_FACTOR);
            set.applyTo(skillsConstraint);

            skillBoxAnchors.add(minus);
            skillRows.add(ranksBox);
        }
    }

    int getNextID() { return idCounter++; }
    int getCurID() { return idCounter; }

    public void backToMain(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    View.OnClickListener addSkillPoint(final Button b, final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sp = MainActivity.globalChar.getSkillRanks().get(index)+1;
                MainActivity.globalChar.setSkillRank(index, sp);
                skillRows.get(index).setText(sp.toString());
                Integer newUsp = Integer.parseInt(unusedRanksBox.getText().toString())-1;
                unusedRanksBox.setText(newUsp.toString());
            }
        };
    }

    View.OnClickListener subSkillPoint(final Button b, final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer sp = MainActivity.globalChar.getSkillRanks().get(index)-1;
                MainActivity.globalChar.setSkillRank(index, sp);
                skillRows.get(index).setText(sp.toString());
                Integer newUsp = Integer.parseInt(unusedRanksBox.getText().toString())+1;
                unusedRanksBox.setText(newUsp.toString());
            }
        };
    }

//    public void onClick(View v) {
//        Integer x = v.getBaseline();
//        Toast.makeText(this, x.toString(), Toast.LENGTH_LONG).show();
//    }
}
