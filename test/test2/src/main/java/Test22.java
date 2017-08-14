public class Test22 {
    public static void main(String args[]) {
        long a = 1, b = 1, c = 1;
        long n = 0;
        for (int i = 1; i < 99 - 1; i++) {
            if (a > 1000000000000L) {
                a = a / 10;
                b = b / 10;
                c = c / 10;
            }
            n = a + b + c;
            a = b;
            b = c;
            c = n;
        }
        System.out.println(n);
    }
}
