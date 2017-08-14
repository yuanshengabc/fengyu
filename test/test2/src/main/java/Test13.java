public class Test13 {
    public static void main(String args[]) {
        boolean flag = false;
        int n;
        for (n = 1600000; !flag; n++) {
            flag = shuang(n);
        }
        System.out.println(n);
    }

    private static boolean shuang(int n) {
        int flag = 0;
        for(int i = 2; i <= 10; i++) {
            if (transform(n,i)) {
                flag++;
            }
        }
        return flag == 2;
    }
    
    private static boolean transform(int n, int k) {
        int [] a = new int[50];
        int i;
        for (i = 0; n != 0;i++) {
            a[i] = n % k;
            n = n / k;
        }
        return huiwen(a, i);
    }

    private static boolean huiwen(int[] array, int n) {
        for (int i = 0; i < n / 2; i ++) {
            if (array[i] != array[n - i - 1]) {
                return false;
            }
        }
        return true;
    }
}
