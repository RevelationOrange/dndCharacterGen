package com.example.revelationorange.dndcharactergen;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.revelationorange.dndcharactergen.MainActivity.MARGIN_FACTOR;

public class sendPage extends AppCompatActivity {
    String sendFName = "";
    File sDir;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_page);
        Intent i = getIntent();
        sDir = new File(i.getStringExtra("fileLoc"));
        LinearLayout fileListLayout = findViewById(R.id.fileListLayout);

        List<String> fileNameList = new ArrayList<>(Arrays.asList(sDir.list()));
        Button curButton;

        for (String fname: fileNameList) {
            curButton = myLib.makebutton(this);
            curButton.setText(fname);
            curButton.setBackgroundResource(android.R.drawable.alert_dark_frame);
            curButton.setTextColor(0xFFFFFFFF);
            curButton.setPadding(24*MARGIN_FACTOR,0,0,0);
            curButton.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            curButton.setOnClickListener(sendAFile(curButton));
            fileListLayout.addView(curButton);
        }
    }


    View.OnClickListener sendAFile(final Button b) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(b.getText());
                File sendFile = new File(sDir + File.separator + b.getText());
                Intent sendIntent = new Intent();
                sendIntent.setType("application/csv");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(sendPage.this, "com.example.revelationorange.dndcharactergen.fileprovider", sendFile));
                startActivity(sendIntent);
            }
        };
    }
}
