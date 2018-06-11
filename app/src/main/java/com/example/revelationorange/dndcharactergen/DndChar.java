package com.example.revelationorange.dndcharactergen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DndChar {
    Random rng = new Random();
    private static final int nBaseStats = 6;
    private static final int nD6Rolls = 4;
    String chName, plName, chClass, race;
    List<Integer> baseStats = new ArrayList<>(nBaseStats);
    List<Integer> baseStatMods = new ArrayList<>(nBaseStats);
    Integer Str, Dex, Con, Int, Wis, Cha, level, xp, speed, initiative, ac, hitDice;

    DndChar() {
        this.xp = 0;
        this.level = 1;
    }

    void rollStats() {
        this.baseStats.clear();
        this.baseStatMods.clear();
        List<Integer> statRolls = new ArrayList<>();
        int statSum = 0;
        for (int i = 0; i < nBaseStats; i++) {
            for (int j = 0; j < nD6Rolls; j++) { statRolls.add(rng.nextInt(6)+1); }
            Collections.sort(statRolls, Collections.<Integer>reverseOrder());
            for (int j = 0; j < nD6Rolls-1; j++) { statSum += statRolls.get(j); }
            this.baseStats.add(statSum);
            this.baseStatMods.add((int) Math.floor((statSum-10)/2));
            statRolls.clear();
            statSum = 0;
        }
    }

    public List<Integer> getBaseStats() { return this.baseStats; }

    public List<Integer> getBaseStatMods() { return baseStatMods; }
}
