//奖券
public class Test5 {
    public static void main (String args[]) {
        int flag = 0;
        for (int i = 100000; i < 1000000; i++) {
            if (sub(i)) {
                flag++;
            }
        }
        System.out.print(flag);
    }

    static boolean sub(int j) {
        while (j != 0) {
            if (j % 10 == 4) {
                return false;
            }
            j = j / 10;
        }
        return true;
    }
}
