package BrickBreaker;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BREAK IT ALL!");
        Gameplay gamePlay = new Gameplay();
        frame.setVisible(true);
        frame.setBounds(10, 10, 710, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);

    }
}
