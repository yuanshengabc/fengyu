public class Test17 {
    public static void main(String args[]) {
//        char[] chars = {45, 46, 45, 46, 32, 45, 32, 46, 46, 45, 46, 32, 46, 45, 32};
//        System.out.print(chars);
//        char[] chars1 = {48, 50, 43, 8, 57, 10, 56, 50};
//        for (int i = 0; i < chars1.length; i++) {
//            chars1[i] = (char) (chars1[i] + 97);
//        }
//        System.out.println(chars1);
//        char[] chars2 = {89, 51, 82, 109, 89, 50, 86, 122, 97, 71, 107, 61};
//        System.out.print(chars2);
        String string = "Os drnuzearyuwn, y jtkjzoztzoes douwlr oj y ilzwex eq lsdexosa kn pwodw tsozj eq ufyoszlbz yrl rlufydlx pozw douwlrzlbz, ydderxosa ze y rlatfyr jnjzli; mjy gfbmw vla xy wbfnsy symmyew (mjy vrwm qrvvrf), hlbew rd symmyew, mebhsymw rd symmyew, vbomgeyw rd mjy lxrzy, lfk wr dremj. Mjy eyqybzye kyqbhjyew mjy myom xa hyedrevbfn lf bfzyewy wgxwmbmgmbrf. Wr mjy dsln bw f1_2jyf-k3_jg1-vb-vl_l";
        StringBuffer str = new StringBuffer();
        int j = 'W' - 'O';
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i)!=' ') {
                char k = (char) (string.charAt(i) + j);
                str.append(k);
            } else {
                str.append(' ');
            }
        }
        System.out.print(str);
    }
}
