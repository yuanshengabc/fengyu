package snaketest.snake;

import java.awt.*;

public class Node {
    int h = 10;
    int w = 10;
    int row;
    int col;
    void draw(Graphics graphics) {
        Color color = graphics.getColor();
        graphics.setColor(setColor());
        graphics.fillRect(this.col*10, this.row*10, w, h);
        graphics.setColor(color);
    }

    Color setColor() {
        return Color.black;
    }
}

