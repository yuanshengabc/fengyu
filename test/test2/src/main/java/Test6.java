//找素数
public class Test6 {
    public static void main(String args[]) {
        int num = 367;
        int flag = 0;
        while (flag != 1) {
            if (sub(num)) {
                flag++;
            }
            num += 186;
        }
        System.out.print(num - 186);
    }

    static boolean sub(int n) {
        for (int i = 2; i < n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
