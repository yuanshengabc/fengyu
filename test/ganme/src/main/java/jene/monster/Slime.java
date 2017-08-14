package jene.monster;

public class Slime extends Monster {
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void show() {
        System.out.println(getName());
        System.out.print("HP:" + getHP());
        System.out.print(" " + "BP:" + getBP());
        System.out.print(" " + "atc:" + getAttack());
        System.out.print(" " + "def:" + getDefense());
        System.out.print(" " + "spe:" + getSpeed());
        System.out.println(" " + "color:" + getColor());
    }
}
