public class Test10 {
    public static void main(String args[]) {
        char[] a = {'x','z','t','i','o','f','w','h','f'};
        for(int i=0;i<9;i++)
        {
            int t;
            t=a[i]-'a';
            t=(21*t+3)%26;
            a[i]=(char)(t+'a');
            System.out.println(a[i]);
        }
    }
}
