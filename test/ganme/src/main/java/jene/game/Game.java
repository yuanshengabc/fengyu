package jene.game;

import jene.chapter.Index;
import jene.monster.*;
import jene.role.Information;
import jene.role.Mymonster;

import java.util.Scanner;

public class Game {

    public Game() {
        start();
    }

    private Mymonster mymonster = new Mymonster();

    private Information information;

    private void start() {
        Scanner input = new Scanner(System.in);
        System.out.println("游戏开始！");
        System.out.println("1.新的旅程");
        System.out.println("2.读档");
        String ys = input.next();
        if (ys.equals("1")) {
            init();
        } else if (ys.equals("2")) {
            load();
        }
        choose();
    }

    private void init() {
        Init init = new Init();
        information = init.initInformation();
        Scanner input = new Scanner(System.in);
        System.out.println("请选择初始怪兽:");
        System.out.println("1.King");
        System.out.println("2.Slime");
        System.out.println("3.Fox");
        String yn = input.next();
        switch (yn) {
            case "1":
                King king = init.initKing();
                mymonster.add(king);
                break;
            case "2":
                init.initSlime();
                break;
            case "3":
                init.initFox();
                break;
        }
    }

    private void load() {

    }

    private void choose() {
        new Index(information);
    }
}
