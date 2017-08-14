public class Test24 {
    public static void main(String args[]) {
        int flag = 0;
        for (int x = 1; x < 1000000 && x != 400; x++) {
            if ((200*x)%(x-400)==0) {
                flag++;
            }
        }
        System.out.println(flag);
    }
}
