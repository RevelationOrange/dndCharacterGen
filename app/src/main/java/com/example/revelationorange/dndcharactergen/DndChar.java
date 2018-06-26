package com.example.revelationorange.dndcharactergen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DndChar {
    Random rng = new Random();
    private static final int nBaseStats = 6;
    private static final int nD6Rolls = 4;

    static String[] races = {"Human", "Elf", "Dwarf", "Half-Elf", "Half-Orc", "Gnome", "Halfling", "Dragonborn", "Tiefling"};
    static String[] classes = {"Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Wizard"};
    static String[] alignments0 = {"Lawful", "Neutral", "Chaotic"};
    static String[] alignments1 = {"Good", "Neutral", "Evil"};
    static HashMap<String, List<String>> skillsDict = new HashMap<>();

    String chName, plName, chClass, race;
    private List<Integer> baseStats = new ArrayList<>(nBaseStats);
    private List<Integer> baseStatMods = new ArrayList<>(nBaseStats);
    List<String> skillList = new ArrayList<>();
    List<Integer> skillRanks = new ArrayList<>();
    Integer Str, Dex, Con, Int, Wis, Cha, level, xp, speed, initiative, ac, hitDice, skillRanksPerLvl, chClassID, raceID;
    private boolean rolled;

    DndChar() {
        this.xp = 0;
        this.level = 1;
        this.skillRanksPerLvl = 2;
        this.rolled = false;
    }

    static void setup(InputStream skillsIS) {
        InputStreamReader isr = new InputStreamReader(skillsIS);
        BufferedReader br = new BufferedReader(isr);
        String[] lineArr;

        try {
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                List<String> skillNames = new ArrayList<>();
                lineArr = lineStr.split(",");
                skillNames.addAll(Arrays.asList(lineArr).subList(1, lineArr.length));
                skillsDict.put(lineArr[0], skillNames);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        this.rolled = true;
    }

    void makeSkillList() {
        this.skillList = skillsDict.get(this.chClass);
    }

    public void setChClass(String chClass, int chClassID) {
        this.chClass = chClass;
        this.makeSkillList();
        this.skillRanks.clear();
        this.chClassID = chClassID;
        for (String s: this.skillList) { this.skillRanks.add(0); }
    }
    public void setRace(String race, int raceID) { this.race = race; this.raceID = raceID; }

    public List<Integer> getBaseStats() { return this.baseStats; }
    public List<Integer> getBaseStatMods() { return baseStatMods; }
    public Integer getSkillRanksPerLvl() { return skillRanksPerLvl; }
    public List<String> getSkillList() { return skillList; }
    public String getChClass() { return chClass; }
    public Integer getChClassID() { return chClassID; }
    public Integer getRaceID() { return raceID; }
    public boolean isRolled() { return rolled; }
}
