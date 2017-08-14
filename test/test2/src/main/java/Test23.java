public class Test23 {
    public static void main(String args[]) {
        int flag = 0;
//        for (int i = 1000; i < 10000; i++) {
//            if (flag == 10) {
//                System.out.println();
//                flag = 0;
//            }
//            if (sushu(i)) {
//                System.out.print(i);
//                System.out.print("   ");
//                flag ++;
//            }
//        }
        int i = 8017;
        if (sushu(i)) {
            System.out.println(i);
        } else {
            System.out.println(false);
        }
    }

    static boolean sushu(int a) {
        for (int i = 2; i < a / 2; i++) {
            if (a / i == 0) {
                return false;
            }
        }
        return true;
    }
}
