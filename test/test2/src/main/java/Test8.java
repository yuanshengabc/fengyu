public class Test8 {
    public static void main(String args[]) {
        int flag = 0;
        for (int i = 901; i < 1000; i++) {
            int flag1 = loop(i);
            if (flag1 > flag) {
                flag = flag1;
            }
        }
        System.out.print(flag);
    }

    private static int loop(int n) {
        int j;
        for (j = 1; n != 1; j++) {
            if (n % 2 == 1) {
                n = 3 * n + 1;
            } else {
                n = n / 2;
            }
        }
        return j;
    }
}
