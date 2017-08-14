package snaketest.eatsnake;

public class Snakenode extends Node {
    Dir dir;
    Snakenode next = null;
    Snakenode prev = null;
    public Snakenode(int row, int col , Dir dir) {
        this.row = row;
        this.col = col;
        this.dir = dir;
    }
}
