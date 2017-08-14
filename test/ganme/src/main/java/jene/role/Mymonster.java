package jene.role;

import jene.game.Init;
import jene.monster.King;
import jene.monster.Monster;

import java.util.ArrayList;
import java.util.List;

public class Mymonster {
    List<Monster> m = new ArrayList<>();
    public void add(Monster monster) {
        m.add(monster);
    }

    public void delete(Monster monster) {
        m.remove(monster);
    }

    public Monster take() {
        Init init = new Init();
        King king = init.initKing();
        return king;
    }
}
