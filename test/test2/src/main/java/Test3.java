public class Test3 {
    public static void main(String args[]) {
        rule(8);
    }

    //采用贝格尔编排法，百度单循环赛制
    private static void rule(int n) {
        if (n < 2 || n > 15) {
            System.out.println("false");
            return;
        }
        if (n % 2 == 1) {
            n = n + 1;
            int [] array = new int[n];
            for (int i = 0; i < n - 1; i++) {
                array[i] = i + 1;
            }
            array[n - 1] = 0;
            print(array, n);
        } else {
            int [] array = new int[n];
            for (int i = 0; i < n; i++) {
                array[i] = i + 1;
            }
            print(array, n);
        }
    }

    static void print(int[] array, int n) {
        //数组倒数第二位
        int max = array[n - 2];
        for (int j = 1; j < n; j++) {
            System.out.println("第" + j + "轮");
            //ｂ记录左边，ｃ记录右边
            int [] b = new int[n / 2];
            int [] c = new int[n / 2];
            if (j % 2 == 1) {
                for (int k = 0; k < (n + 1) / 2; k++) {
                    b[k] = array[k + (j - 1) / 2];
                    if (k == 0) {
                        c[k] = array[n - 1];
                    } else {
                        c[k] = b[k] + (max - k * 2);
                        if (c[k] > max) {
                            c[k] = c[k] % max;
                        }
                    }
                    System.out.print(b[k] + "-" + c[k]+ "  ");
                }
            } else {
                for (int k = 0; k < (n + 1) / 2; k ++) {
                    c[k] = array[n / 2 + j / 2 - 1 - k];
                    if (k == 0) {
                        b[k] = array[n - 1];
                    } else {
                        b[k] = c[k] - (max - k * 2);
                        if (b[k] <= 0) {
                            b[k]+= max;
                        }
                    }
                    System.out.print(b[k] + "-" + c[k]+"  ");
                }
            }
            System.out.println();
        }
    }
}
