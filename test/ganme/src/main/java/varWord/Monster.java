package varWord;

class Monster {
    String name; //名字
    int hp; //血量
    int atc; //攻击力
    int def; //防御力
    int attack(int def) {
        return atc - def;
    }

    void show() {
        System.out.println(name + "攻击力:" + atc + "防御力:" + def + "生命值" + hp);
    }
}
