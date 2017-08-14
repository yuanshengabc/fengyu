public class Test99 {
    public static void main(String[] args) {
        Iterator.iterate(5,new IAction());
    }
}
abstract class Iterator<T> {
    abstract void process(T n);
    static void iterate(int n, Iterator<Integer> action) {
        for (int i = 1; i <= n; i++) {
            action.process(i);
        }
    }
}
class IAction extends Iterator<Integer> {
    public void process(Integer n) {
        iterate(n, new JAction(n));
        System.out.println();
    }
}
class JAction extends Iterator<Integer> {
    private final int x;
    JAction(int n) {
        this.x = n;
    }
    public void process(Integer n) {
        System.out.print(" "+x * n);
    }
}