package snaketest.eatsnake;

import java.awt.*;
import java.util.Random;

public class Egg extends Node{

    public Rectangle getRect() {
        return new Rectangle(this.col*PaintThread.BLOCK_SIZE, this.row*PaintThread.BLOCK_SIZE, w, h);
    }
    public void reAppear() {
        Random r = new Random();
        this.row = r.nextInt(PaintThread.ROWS -3) + 3;
        this.col = r.nextInt(PaintThread.COLS);
    }
    public void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.green);
        g.fillOval(this.col*PaintThread.BLOCK_SIZE, this.row*PaintThread.BLOCK_SIZE, w, h);
        g.setColor(c);
    }
}
