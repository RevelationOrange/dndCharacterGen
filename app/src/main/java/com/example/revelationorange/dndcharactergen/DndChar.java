package com.example.revelationorange.dndcharactergen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DndChar {
    Random rng = new Random();
    String chName, plName, chClass, race;
    List<Integer> baseStats = new ArrayList<>(6);
    Integer Str, Dex, Con, Int, Wis, Cha, level, xp, speed, initiative, ac, hitDice;

    DndChar() {
        this.xp = 0;
        this.level = 1;
    }

    void rollStats() {
        for (int i = 0; i < baseStats.size(); i++) {
            baseStats.set(i, 7);
        }
    }

    public List<Integer> getBaseStats() { return this.baseStats; }
}
