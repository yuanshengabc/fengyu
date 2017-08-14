//算术题
import java.util.HashSet;
import java.util.Set;

public class Test9 {
    public static void main(String args[]) {
        int a,b,c,d,e,f,g,h,i,j;
        for (a = 1; a < 11; a++) {
            for (b = 1; b < 11; b++) {
                for (c = 1; c < 11; c++) {
                    for (d = 1; d < 11; d++) {
                        for (e = 1; e < 11; e++) {
                            for (f = 1; f < 11; f++) {
                                for (g = 1; g < 11; g++) {
                                    for (h = 1; h < 11; h++) {
                                        for (i = 1; i < 11; i++) {
                                            for (j = 1; j < 11; j++) {
                                                if (f == 10 || g == 10 || h == 10 || i == 10 || j == 10){
                                                    if (f == 6) {
                                                        Set<Integer> uniqNum = new HashSet<>();
                                                        uniqNum.add(a);
                                                        uniqNum.add(b);
                                                        uniqNum.add(c);
                                                        uniqNum.add(d);
                                                        uniqNum.add(e);
                                                        uniqNum.add(f);
                                                        uniqNum.add(g);
                                                        uniqNum.add(h);
                                                        uniqNum.add(i);
                                                        uniqNum.add(j);
                                                        if (uniqNum.size() == 10) {
                                                            Set<Integer> num = new HashSet<>();
                                                            int sum;
                                                            sum = f + a + b;
                                                            num.add(sum);
                                                            sum = g + b + c;
                                                            num.add(sum);
                                                            sum = h + c + d;
                                                            num.add(sum);
                                                            sum = i + d + e;
                                                            num.add(sum);
                                                            sum = j + e + a;
                                                            num.add(sum);
                                                            if (num.size() == 1) {
                                                                System.out.print(g);
                                                                System.out.print(b);
                                                                System.out.println(c);
                                                                System.out.print(h);
                                                                System.out.print(c);
                                                                System.out.println(d);
                                                                System.out.print(i);
                                                                System.out.print(d);
                                                                System.out.println(e);
                                                                System.out.print(j);
                                                                System.out.print(e);
                                                                System.out.println(a);
                                                                System.out.print(f);
                                                                System.out.print(a);
                                                                System.out.println(b);
                                                                System.out.println();
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
            }
        }
    }
}
