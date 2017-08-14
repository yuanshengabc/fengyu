package varWord;

import java.util.Scanner;

public class Game {
    private int count;
    private Player player = new Player();
    private Monster monster = new Monster();
    private Scanner input = new Scanner(System.in);
    public void init() {
        System.out.println("请选择初始人物");
        System.out.println("1.小白");
        System.out.println("2.小红");
        System.out.println("3.苒苒");
        System.out.println("4.雅璇");
        int character = input.nextInt();
        switch (character) {
            case 1:
                player.name = "小白";
                player.atc = 80;
                player.def = 40;
                player.hp = 100;
                break;
            case 2:
                player.name = "小红";
                player.atc = 100;
                player.def = 100;
                player.hp = 100;
                break;
            case 3:
                player.name = "苒苒";
                player.atc = 85;
                player.def = 30;
                player.hp =100;
                break;
            case 4:
                player.name = "雅璇";
                player.atc = 90;
                player.def = 100;
                break;
        }
        player.show();
    }
    public void randomMonster() {
        int rd = (int) (Math.random() * 3 + 1);
        switch (rd) {
            case 1:
                monster.name = "";
                monster.atc = 10;
                monster.def = 50;
                monster.hp = 40;
                break;
            case 2:
                monster.name = "";
                monster.atc = 10;
                monster.def = 50;
                monster.hp = 40;
                break;
            case 3:
                monster.name = "";
                monster.atc = 10;
                monster.def = 50;
                monster.hp = 40;
                break;
        }
        monster.show();
    }
    public void start() {
        init();
        System.out.println("可以开始了吗?");
        String yn = input.next();
        if (yn.equals("y")) {

            while (true) {
                randomMonster();
                int deHp = player.attack(monster.def);
                if (deHp < 0) {
                    deHp = 0;
                    count++;
                }
                monster.hp = deHp;

                System.out.println("你攻击了" + monster.name + "造成了" + deHp + "伤害");

                if (monster.hp <= 0) {
                    count++;
                    break;
                } else {
                    monster.show();
                    int dH = monster.attack(player.def);
                    if (dH <= 0) {
                        dH = 0;
                    }
                    player.hp -= dH;
                    System.out.println(monster.name + "攻击了你造成了" + dH + "伤害");
                    player.show();
                    if (player.hp < 0) {
                        count++;
                        System.out.println("你已经被击败了！");
                        break;
                    }
                }
                if (player.hp > monster.hp) {
                    count++;
                    System.out.println("你击败了" + monster.name + "是否继续战斗(y/n)");
                    String y = input.next();
                    if (y.equals("n")) {
                        player.show();
                        System.out.println("你击败了" + count + "只怪兽");
                        break;
                    }
                }
            }
        } else {
            System.out.println("你击败了" + count + "个怪物");
        }
    }
}
