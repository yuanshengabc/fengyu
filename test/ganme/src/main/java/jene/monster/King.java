package jene.monster;

public class King extends Monster {
    private int skin;

    public int getSkin() {
        return skin;
    }

    public void setSkin(int skin) {
        this.skin = skin;
    }

    public void show() {
        System.out.println(getName());
        System.out.print("HP:" + getHP());
        System.out.print(" " + "BP:" + getBP());
        System.out.print(" " + "atc:" + getAttack());
        System.out.print(" " + "def:" + getDefense());
        System.out.print(" " + "spe:" + getSpeed());
        System.out.println(" " + "skin:" + getSkin());
    }
}
