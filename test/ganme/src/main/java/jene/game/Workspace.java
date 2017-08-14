package jene.game;

import jene.monster.King;
import jene.monster.Monster;
import jene.monster.Slime;

public class Workspace {

    public Workspace() {
        Init init = new Init();
        King king  = init.initKing();
        Slime slime = init.initSlime();
        start();
        fight(king, slime);
        end();
    }

    public Workspace(Monster monster1, Monster monster2) {
        start();
        fight(monster1,monster2);
        end();
    }

    private void start() {
        System.out.println("战斗开始");
    }

    private void end() {
        System.out.println("战斗结束");
    }

    private void fight(Monster monster1,Monster monster2) {
        int harm;
        while(monster1.getHP() > 0 && monster2.getHP() > 0) {
            if (monster1.getSpeed() > monster2.getSpeed()) {
                harm = monster1.getAttack() - monster2.getDefense();
                monster2.setHP(monster2.getHP() - harm);
                System.out.println(monster1.getName() + "对" + monster2.getName() + "造成了" + harm + "点伤害");
                if (monster2.getHP() > 0) {
                    harm = monster2.getAttack() - monster1.getDefense();
                    monster1.setHP(monster1.getHP() - harm);
                    System.out.println(monster2.getName() + "对" + monster1.getName() + "造成了" + harm + "点伤害");
                }
            } else {
                harm = monster2.getAttack() - monster1.getDefense();
                monster1.setHP(monster1.getHP() - harm);
                System.out.println(monster2.getName() + "对" + monster1.getName() + "造成了" + harm + "点伤害");
                if (monster1.getHP() > 0) {
                    harm = monster1.getAttack() - monster2.getDefense();
                    monster2.setHP(monster2.getHP() - harm);
                    System.out.println(monster1.getName() + "对" + monster2.getName() + "造成了" + harm + "点伤害");
                }
            }
        }
    }
}
