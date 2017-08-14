public class Test25 {
    public static void main(String args[]) {
        int j = 1;
        for (int x = 1; j != 435718618; x++){
            for (int i = 0;i < 7;i++) {
                while (j / 10000000000L != 0) {
                    j = j / 10;
                }
                j = j * x;
            }
            if (x%1000000==0) {
                System.out.println(x);
            }
        }
        System.out.println(j);
    }
}
