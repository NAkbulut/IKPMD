package com.example.ikpmd_periode2.models;
import java.io.Serializable;

public class pokemon implements Serializable{
    private String Name;
    private String type1;
    private String type2;
    private int HP;
    private int ATK;
    private int SP_ATK;
    private int DEF;
    private int SP_DEF;
    private int SPD;
    private int Weight;
    private int Height;

    public pokemon(String name, String type1, String type2, int HP, int ATK, int SP_ATK, int DEF, int SP_DEF, int SPD, int weight, int height) {
        Name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.HP = HP;
        this.ATK = ATK;
        this.SP_ATK = SP_ATK;
        this.DEF = DEF;
        this.SP_DEF = SP_DEF;
        this.SPD = SPD;
        Weight = weight;
        Height = height;
    }

    public pokemon(){
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getATK() {
        return ATK;
    }

    public void setATK(int ATK) {
        this.ATK = ATK;
    }

    public int getSP_ATK() {
        return SP_ATK;
    }

    public void setSP_ATK(int SP_ATK) {
        this.SP_ATK = SP_ATK;
    }

    public int getDEF() {
        return DEF;
    }

    public void setDEF(int DEF) {
        this.DEF = DEF;
    }

    public int getSP_DEF() {
        return SP_DEF;
    }

    public void setSP_DEF(int SP_DEF) {
        this.SP_DEF = SP_DEF;
    }

    public int getSPD() {
        return SPD;
    }

    public void setSPD(int SPD) {
        this.SPD = SPD;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }
}
