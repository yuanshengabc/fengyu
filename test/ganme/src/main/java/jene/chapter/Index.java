package jene.chapter;

import jene.role.Information;

import java.util.Scanner;

public class Index {
    public Index(Information information) {
        int a = information.getGuanka();
        b(a);
        Scanner input = new Scanner(System.in);
        String yn = input.next();
    }

    private void b(int a) {
        while(a > 0) {
            System.out.println(a + ".第" + a +"章");
            a--;
        }
    }

//    private void b(int a) {
//        Mymonster mymonster = new Mymonster();
//        Init init = new Init();
//        System.out.println("第一章");
//        System.out.println("1.第一节");
//        Scanner input = new Scanner(System.in);
//        String yn = input.next();
//        if (yn.equals("1")) {
//            Slime slime = init.initSlime();
//            new Workspace(mymonster.take(),slime);
//        }
//    }
}
