package jene.monster;

public class Monster {
    private String name;
    private int HP;
    private int BP;
    private int attack;
    private int defense;
    private int speed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getBP() {
        return BP;
    }

    public void setBP(int BP) {
        this.BP = BP;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int atc) {
        this.attack = atc;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int def) {
        this.defense = def;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
