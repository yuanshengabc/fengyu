//三羊献瑞
import java.util.HashSet;
import java.util.Set;

public class Test7 {
    public static void main(String args[]) {
        int a,b,c,d,e,f,g,h,sum;
        for (a = 0; a < 10; a++) {
            for (b = 0; b < 10; b++) {
                for (c = 0; c < 10; c++) {
                    for (d = 0; d < 10; d++) {
                        for (e = 1; e < 10; e++) {
                            for (f = 0; f < 10; f++) {
                                for (g = 0; g < 10; g++) {
                                    for (h = 0; h < 10; h++){
                                        Set<Integer> uniqNum = new HashSet<>();
                                        uniqNum.add(a);
                                        uniqNum.add(b);
                                        uniqNum.add(c);
                                        uniqNum.add(d);
                                        uniqNum.add(e);
                                        uniqNum.add(f);
                                        uniqNum.add(g);
                                        uniqNum.add(h);
                                        if (uniqNum.size() ==8) {
                                            sum = a*1000+e*1000+b*100+f*100+c*10+g*10+d+b;
                                            if (sum == e*10000+f*1000+c*100+b*10+h) {
                                                System.out.println(e);
                                                System.out.println(f);
                                                System.out.println(g);
                                                System.out.println(b);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
