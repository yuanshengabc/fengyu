public class Test16 {
    public static void main(String args[]) {
        int flag = 0;
        int m;
        for (int i = 1; i <= 6789; i++) {
            m = i;
            while (m % 5 == 0) {
                flag++;
                m = m / 5;
            }
        }
        System.out.println(flag);
    }
}