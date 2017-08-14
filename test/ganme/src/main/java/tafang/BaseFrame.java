package tafang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class BaseFrame extends JFrame implements Runnable, KeyListener {

    /**
     * 绘制方块x坐标
     */
    private int x = 200;

    /**
     * 绘制方块y坐标
     */
    private int y = 200;

    /**
     * 构造函数
     */
    public BaseFrame() {
        this.setBounds(100, 100, 500, 500); // 设置窗体的位置、大小
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体右上角那个叉关闭程序
        this.setVisible(true); // 设置窗体可见

        this.addKeyListener(this); // 添加键盘监听器

        Thread thread = new Thread(this); // 创建线程
        thread.start(); // 启动线程
    }

    /**
     * 绘制方法
     */
    public void paint(Graphics gr) {
        BufferedImage image = new BufferedImage(500, 500,
                BufferedImage.TYPE_3BYTE_BGR); // 创建一张500＊500的缓冲图片
        Graphics g2 = image.getGraphics(); // 获取缓冲图片的画笔

        g2.setColor(Color.WHITE); // 设置画笔颜色
        g2.fillRect(x, y, 100, 100);

        gr.drawImage(image, 0, 0, this); // 将缓冲图片画到窗体上
    }

    /**
     * 线程执行方法
     */
    public void run() {
        try {
            while (true) {
                this.repaint();
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 按下时调用
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        // System.out.println(keyCode); // 打印按键的keyCode
        if (keyCode == 38) { // 上按键
            y -= 10;
        }
        if (keyCode == 40) { // 下按键
            y += 10;
        }
        if (keyCode == 37) { // 左按键
            x -= 10;
        }
        if (keyCode == 39) { // 右按键
            x += 10;
        }
    }

    /**
     * 释放按键时调用
     */
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 不解释
     */
    public void keyTyped(KeyEvent e) {

    }

    public static void main(String[] args) {
        new BaseFrame();
    }

}