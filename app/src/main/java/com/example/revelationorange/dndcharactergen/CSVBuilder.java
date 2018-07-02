package com.example.revelationorange.dndcharactergen;

import android.text.TextUtils;
import android.view.View;

public class CSVBuilder {
    private String str;
    private int maxArgs;

    CSVBuilder() {
        this.str = "";
        this.maxArgs = 0;
    }

    public void addLine(Object... args) {
        this.str += TextUtils.join(",", args) + "\n";
        this.maxArgs = Math.max(this.maxArgs, args.length);
    }
    public String getStr() {
        StringBuilder finalStr = new StringBuilder();
        String[] lines = this.str.split("\n");
        String[] splitLine;
        StringBuilder filler;
        int diff;
        for (String line : lines) {
            splitLine = line.split(",");
            diff = this.maxArgs+1 - splitLine.length;
            filler = new StringBuilder();
            for (int i = 0; i < diff; i++) { filler.append(","); }
            finalStr.append(line).append(filler.toString()).append("\n");
        }
        return finalStr.toString();
    }
}
