package BrickBreaker;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class Gameplay extends JPanel implements ActionListener, KeyListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 0;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -2;
    private int ballYdir = -3;

    private BrickGenerator brick;

    public Gameplay() {
        brick = new BrickGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
//         background
        g.setColor(Color.black);
        g.fillRect(0, 0, 692, 592);

//         borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

//         paddle
        g.setColor(Color.darkGray);
        g.fillRect(playerX, 550, 10, 8);
        g.setColor(Color.darkGray);
        g.fillRect(playerX + 90, 550, 10, 8);
        g.setColor(Color.lightGray);
        g.fillRect(playerX + 11, 550, 78, 8);

//         ball
        g.setColor(Color.white);
        g.fillOval(ballposX, ballposY, 20, 20);

//        drawing bricks
        brick.draw((Graphics2D) g);

//        score
        g.setColor(Color.cyan);
        g.setFont(new Font("roboto", Font.BOLD, 23));
        g.drawString("" + score, 645, 30);

//        when all the bricks are destroyed
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("roboto", Font.BOLD, 40));
            g.drawString("You Won!", 190, 300);
            g.setColor(Color.yellow);
            g.setFont(new Font("roboto", Font.BOLD, 25));
            g.drawString("Press Enter to Restart", 163, 330);
        }

//        when the ball falls
        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.white);
            g.setFont(new Font("roboto", Font.BOLD, 40));
            g.drawString("Game Over", 190, 300);
            g.setFont(new Font("roboto", Font.BOLD, 30));
            g.drawString("Score: " + score, 225, 330);
            g.setColor(Color.yellow);
            g.setFont(new Font("roboto", Font.BOLD, 25));
            g.drawString("Press Enter to Restart", 163, 360);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            timer.start();

            ballposX += ballXdir;
            ballposY += ballYdir;

//           when the ball touches the paddle
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 10, 8))) {
                if (ballYdir > 0) ballYdir *= -1;
                if (ballXdir > 0) ballXdir *= -1;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 90, 550, 10, 8))) {
                if (ballYdir > 0) ballYdir *= -1;
                if (ballXdir < 0) ballXdir *= -1;
            } else if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 10, 550, 80, 8)))
                ballYdir *= -1;

//            when the ball touches the left border
            if (ballposX < 0) ballXdir *= -1;

//            when the ball touches the top border
            if (ballposY < 0) ballYdir *= -1;

//            when the ball touches the right border
            if (ballposX > 670) ballXdir *= -1;

//            when the ball touches a brick
            A:
            for (int i = 0; i < brick.grid.length; i++) {
                for (int j = 0; j < brick.grid[i].length; j++) {
                    if (brick.grid[i][j] > 0) {
                        int brickX = j * brick.brickWidth + 80;
                        int brickY = i * brick.brickHeight + 50;
                        int brickWidth = brick.brickWidth;
                        int brickHeight = brick.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
//                            removing the brick
                            brick.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 10;

                            if (ballposX + 18 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir *= -1;
                            } else ballYdir *= -1;

                            break A;
                        }
                    }
                }
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
//         when right arrow is pressed
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 590) playerX = 590;
            else moveRight();
        }

//         when left arrow is pressed
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 4) playerX = 4;
            else moveLeft();
        }

//        when the Enter key is pressed after 'Game over'
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                score = 0;
                totalBricks = 21;
                playerX = 310;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -2;
                ballYdir = -3;
                brick = new BrickGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

}
