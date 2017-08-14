package snaketest.eatsnakes;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class MainClass extends JFrame {

    private static final int FWIDTH = 315;

    private static final int FHEIGHT = 380;

    public static void main(String[] args) {
        new MainClass("my snake");
    }

    private MainClass(String s) {
        super(s);
        ControlSnake control = new ControlSnake();
        control.setFocusable(true);
        Dimension dimen = Toolkit.getDefaultToolkit().getScreenSize();

        add(control);
        setLayout(new BorderLayout());
        setLocation(dimen.width / 3, dimen.height / 3);// dimen.width/3,dimen.height/3
        setSize(FWIDTH, FHEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

}