package snaketest.snake;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Snake {
    private int size = 1;
    private Snakenode head = new Snakenode(10, 10, Dir.R);
    private Snakenode tail = head;

    public void eat (Egg e,Score s) {
        if (this.getRect().intersects(e.getRect())) {
            e.reAppear();
            this.addToHead();
            s.setScore(s.getScore() + 5);
        }
    }
    public Rectangle getRect() {
        return new Rectangle(head.col*10, head.row*10, head.w, head.h);
    }
    private void addToHead() {
        Snakenode n = new Snakenode();
        switch (head.dir) {
            case L:
                n = new Snakenode(head.row, head.col - 1, head.dir);
                break;
            case U:
                n = new Snakenode(head.row, head.col - 1, head.dir);
                break;
            case R:
                n = new Snakenode(head.row, head.col - 1, head.dir);
                break;
            case D:
                n = new Snakenode(head.row, head.col - 1, head.dir);
                break;
        }
        n.next = head;
        head.prev = n;
        head =n;
        size++;
    }

    private void deleteFromTail() {
        if (size == 0) return;
        tail = tail.prev;
        tail.next = null;
    }
    void draw(Graphics graphics) {
        if (size == 0) return;
        move();
        for (Snakenode n = head; n != null; n = n.next) {
            n.draw(graphics);
        }
    }
    private void move() {
        addToHead();
        deleteFromTail();
        checkDead();
    }
    private void checkDead() {
        if (head.col < 1 || head.col > 10 || head.row < 3 || head.row > 10) {
            stop();
        }
        for (Snakenode n = head.next; n != null; n = n.next) {
            if (n.col == head.col && n.row == head.row) {
                stop();
            }
        }
    }

    private void stop() {

    }
    public void Key(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (head.dir != Dir.R)
                    head.dir = Dir.L;
                break;
            case KeyEvent.VK_UP:
                if (head.dir != Dir.D)
                    head.dir = Dir.U;
                break;
            case KeyEvent.VK_RIGHT:
                if (head.dir != Dir.L)
                    head.dir = Dir.R;
                break;
            case KeyEvent.VK_DOWN:
                if (head.dir != Dir.U)
                    head.dir = Dir.D;
                break;
        }
    }
}
