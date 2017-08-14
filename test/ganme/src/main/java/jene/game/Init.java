package jene.game;

import jene.monster.Fox;
import jene.monster.King;
import jene.monster.Slime;
import jene.role.Information;

public class Init {
    public Information initInformation() {
        Information information = new Information();
        information.setGuanka(1);
        return information;
    }

    public King initKing() {
        King king = new King();
        king.setName("King");
        king.setSkin(1);
        king.setHP(100);
        king.setBP(20);
        king.setAttack(10);
        king.setDefense(5);
        king.setSpeed(9);
        king.show();
        return king;
    }

    public Slime initSlime() {
        Slime slime = new Slime();
        slime.setName("Slime");
        slime.setColor(1);
        slime.setHP(20);
        slime.setBP(5);
        slime.setAttack(5);
        slime.setDefense(3);
        slime.setSpeed(5);
        slime.show();
        return slime;
    }

    public Fox initFox() {
        Fox fox = new Fox();
        fox.setName("Fox");
        fox.setSkin(1);
        fox.setHP(20);
        fox.setBP(5);
        fox.setAttack(5);
        fox.setDefense(3);
        fox.setSpeed(5);
        fox.show();
        return fox;
    }
}
