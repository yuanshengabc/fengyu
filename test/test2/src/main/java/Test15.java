//大数据问题
public class Test15 {
    public static void main(String args[]) {
        int sum = 0;
        for (int i = 1; i <= 6789; i++) {
            sum += jiecheng(i);
            sum = sum % 100000;
        }
        System.out.println(sum);
    }

    static int jiecheng(int n) {
        int a = 1;
        for (int i = 1; i <= n; i++) {
            a = a * i;
            a = a % 100000;
        }
        return a;
    }
}
