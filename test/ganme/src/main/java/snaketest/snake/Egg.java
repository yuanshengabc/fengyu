package snaketest.snake;

import java.awt.*;
import java.util.Random;

public class Egg extends Node {

    public Rectangle getRect() {
        return new Rectangle(this.col*10, this.row*10, w, h);
    }
    public void reAppear() {
        Random r = new Random();
        this.row = r.nextInt();
        this.col = r.nextInt();
    }

    Color setColor() {
        return Color.green;
    }
}
