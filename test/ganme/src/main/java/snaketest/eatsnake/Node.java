package snaketest.eatsnake;

import java.awt.*;

public class Node {
    int h = PaintThread.BLOCK_SIZE;
    int w = PaintThread.BLOCK_SIZE;
    int row,col;
    void draw(Graphics g) {
        Color c = g.getColor();
        g.setColor(Color.black);
        g.fillRect(this.col*PaintThread.BLOCK_SIZE, this.row*PaintThread.BLOCK_SIZE, w, h);
        g.setColor(c);
    }
}
