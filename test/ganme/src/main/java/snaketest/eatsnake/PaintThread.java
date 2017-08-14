package snaketest.eatsnake;

import javax.swing.*;
import java.awt.*;

public class PaintThread extends JFrame implements Runnable {
    static int COLS = 30;
    static int ROWS = 30;
    static int BLOCK_SIZE = 10;
    private boolean flag = true;
    Snake snake = new Snake();
    Egg egg = new Egg();
    Score score =new Score();


    public void launch() {
        this.setBounds(100,100,COLS*BLOCK_SIZE,ROWS*BLOCK_SIZE);
        this.setVisible(true);
        this.addKeyListener(new keyPressed());
        new Thread(new PaintThread()).start();
    }

    @Override
    public void run() {
        try {
            while (flag) {
                snake.move();
                repaint();
                Thread.sleep(50);
                if (gameOver()) {
                    flag = false;
                }
            }
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
    }
    public void paint(Graphics graphics) {
        Color c = graphics.getColor();
        graphics.setColor(Color.cyan);
        graphics.fillRect(0,0,ROWS*BLOCK_SIZE,COLS*BLOCK_SIZE);
        graphics.setColor(Color.black);
        for(int i = 1; i < ROWS; i++) {
            graphics.drawLine(0,BLOCK_SIZE*i,ROWS*BLOCK_SIZE,BLOCK_SIZE*i);
        }
        for(int i = 1; i < COLS; i++) {
            graphics.drawLine(i*BLOCK_SIZE,0,BLOCK_SIZE*i,COLS*BLOCK_SIZE);
        }
        snake.eat(egg,score);
        snake.draw(graphics);
        egg.draw(graphics);
        graphics.setColor(Color.red);
        graphics.setFont(new Font("宋体",Font.BOLD,20));
        graphics.drawString("score" + score, 20, 60);
        if (gameOver()) {
            graphics.setColor(Color.red);
            graphics.setFont(new Font("宋体",Font.BOLD,20));
            graphics.drawString("游戏结束！", 100, 200);
        }
        graphics.setColor(c);
    }

//    public void update(Graphics g) {
//        if (offScreenImage == null) {
//            offScreenImage = this.createImage(BLOCK_SIZE*COLS,BLOCK_SIZE*ROWS);
//        }
//        Graphics gOff = offScreenImage.getGraphics();
//        paint(gOff);
//        g.drawString(offScreenImage, 0, 0, null);
//    }

    public void stop() {
        gameOver();
    }

    public boolean gameOver() {
        return false;
    }

    public static void main(String args[]) {
        new PaintThread().launch();
    }

}
