//约瑟夫环
public class Test14 {
    public static void main(String args[]) {
        int [] array = new int [25];
        boolean flag = false;
        int m;
        for (int i = 0; i < 25; i++) {
            array[i] = i;
        }
        for (m = 1; !flag ; m++) {
            flag = a(array, m);
        }
        System.out.println(m - 1);
    }

    private static boolean a(int[] array, int m) {
        int k = 1;
        for (int i = 1; i < 13;i++) {
            int point = 1;
            while (point != m) {
                if (k > 24) {
                    k = 1;
                }
                if (array[k] == 0) {
                    k++;
                } else {
                    point++;
                    k++;
                }
            }
            if (k < 13 || k > 24) {
                return false;
            } else {
                array[k] = 0;
                k++;
            }
        }
        return true;
    }
}
