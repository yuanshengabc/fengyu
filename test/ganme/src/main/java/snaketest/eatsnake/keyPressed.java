package snaketest.eatsnake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class keyPressed implements KeyListener {
    Snake snake;
    @Override
    public void keyTyped(KeyEvent e) {
        snake.ifChange(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
