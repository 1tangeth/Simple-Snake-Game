/*
PERFORMNCE TASK
Snake game designed by Ethan Tang. The snake will start with 3 bodies, whenever the snake eat an apple, the snake will add one body length.
There are ghost wondering around, and there will be more and more whenever the snake gets longer.
However if the snake head touches ghost or bomb. The snake will die. But the snake will be able to kill the ghost by touching the ghost with
its tail.
 */
package PerformanceTask;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author ethan
 */
public class SnakeGame extends JPanel implements KeyListener, ActionListener {

    Random random = new Random();
    Image snakehead, apple, title, body, bomb, explosion, ghost, scared;
    //GHOST X Y
    int[] xghost = new int[750];
    int[] yghost = new int[750];
    //APPLE X Y
    int xapple = random.nextInt(800) + 25;
    int yapple = random.nextInt(450) + 150;
    //BOMB X Y
    int xbomb = random.nextInt(700) + 25;
    int ybomb = random.nextInt(500) + 150;
    //SNAKE X Y
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    //LENGTH OF SNAKE
    int lengthofsnake = 3;
    //GAME START/PAUSE
    boolean gamestart = false;
    //FONT
    Font font = new Font("Arial", Font.BOLD, 40);
    //DIRECTION OF THE SNAKE
    String direction = "Right";
    //CURRENT DIRECTION OF THE SNAKE
    String currentdirection = "Right";
    //SPEED UP AND NORMAL SPEED
    Timer timer = new Timer(130, this);
    Timer timer2 = new Timer(40, this);
    //SPEED UP
    boolean speedup = false;
    //BOMB COLLISION
    boolean exploded = false;
    //BODY COLLISION
    boolean bodycollision = false;
    //GHOST COLLISION
    boolean ghostcollision = false;
    //RESTART THE GAME
    boolean restart = false;
    //POINTS EARNED BY EATING APPLES
    int points = 3;
    //NUMBER OF GHOST
    int numberofghost = 1;
    //GHOST CHANGE DIRECTION
    int[] changedirection = new int[750];
    //GHOST ELIMINATED
    int ghostkilled = 0;
    //DETERMINE IF GHOST IS ELIMINATED
    int[] ghostvisble = new int[750];

    public SnakeGame() {
        this.addKeyListener(this);
        setFocusable(true);
        try {
            //open all the images
            snakehead = ImageIO.read(new File("snakehead.jpg"));
            apple = ImageIO.read(new File("apple.jpg"));
            title = ImageIO.read(new File("title.png"));
            body = ImageIO.read(new File("snakebody.jpg"));
            bomb = ImageIO.read(new File("bomb.png"));
            explosion = ImageIO.read(new File("explosion.png"));
            ghost = ImageIO.read(new File("ghost.png"));
        } catch (IOException e) {
            System.out.println("File not found");//if cantfind image, the program will exit
            System.exit(-1);
        }
        //Initialize the three snake head and body
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 70;
        snakey[1] = 100;
        snakex[2] = 40;
        snakey[2] = 100;
        //intialize the first ghost
        yghost[0] = random.nextInt(450) + 150;
        xghost[0] = 30;
        //start timer
        timer.start();
        //intialize direction change and visable array. Direction go right, ghost visable set to true
        for (int i = 0; i < 750; i++) {
            changedirection[i] = 1;
            ghostvisble[i] = 1;
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.black);//set background to black
        g.setColor(Color.DARK_GRAY);//set the following images to dark gray color
        //game border
        g.fillRect(25, 75, 850, 600);
        //title
        g.fillRect(25, 0, 850, 50);
        //Title title "snake game"
        g.drawImage(title, 380, 0, 150, 40, this);
        if (gamestart) {//if the game started, draw snake head
            g.drawImage(snakehead, snakex[0], snakey[0], 30, 30, null);
            for (int i = 1; i < lengthofsnake; i++) {//draw snake body
                g.drawImage(body, snakex[i], snakey[i], 30, 30, null);
            }
            //draw apples and  xbomb
            g.drawImage(apple, xapple, yapple, 30, 30, null);
            g.drawImage(bomb, xbomb, ybomb, 30, 30, null);

            for (int i = 0; i < numberofghost; i++) {
                if (ghostvisble[i] == 1) {//only draw the ghost when it is visable
                    g.drawImage(ghost, xghost[i], yghost[i], 30, 30, null);
                    if (ghostcollision == false) {//if the ghost doesnt die, when it hit the border it will go the opposite direction
                        if (xghost[i] >= 870) {
                            changedirection[i] = 0;
                        } else if (xghost[i] <= 0) {
                            changedirection[i] = 1;
                        }
                        if (changedirection[i] == 1) {
                            xghost[i] += 1;
                        } else if (changedirection[i] == 0) {
                            xghost[i] -= 1;
                        }

                    }
                }
            }
        }
        //NUMBER LENGTH OF SNAKE , GHOSTKILLED, AND CONTROLS
        g.setColor(Color.WHITE);
        g.drawString("Lengths:" + points, 800, 40);
        g.drawString("Ghost Eliminated:" + ghostkilled, 750, 30);
        g.drawString("WASD to Move", 25, 35);
        g.drawString("Shift to speed up", 25, 15);
        g.drawString("Shift to Slow Down", 25, 25);

        //if the snakehead eats the apple, the length of the snake, points, number of ghost will add 1
        if (snakex[0] > xapple - 20 && snakex[0] < xapple + 30 && snakey[0] > yapple - 20 && snakey[0] < yapple + 30) {
            lengthofsnake++;
            xapple = random.nextInt(800) + 25;
            yapple = random.nextInt(450) + 150;
            xbomb = random.nextInt(800) + 25;
            ybomb = random.nextInt(450) + 150;
            points++;
            numberofghost++;
            yghost[numberofghost - 1] = random.nextInt(450) + 150;
            xghost[numberofghost - 1] = 30;
        }
        //if hit the bomb, game ends
        if (snakex[0] > xbomb - 20 && snakex[0] < xbomb + 30 && snakey[0] > ybomb - 20 && snakey[0] < ybomb + 30) {
            g.drawImage(explosion, xbomb, ybomb, 30, 30, null);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("YOU DIED TO A BOMB. Points :" + points, 80, 300);
            g.drawString("ESC to exit. Enter to restart", 80, 350);
            exploded = true;
        }
        //if hit the ghost with snake's head, the snake will die, if hit the ghost with its body, the ghost will die
        for (int i = 1; i < lengthofsnake; i++) {
            for (int j = 0; j < numberofghost; j++) {
                if (ghostvisble[j] == 1) {
                    if (snakex[i] > xghost[j] - 20 && snakex[i] < xghost[j] + 30 && snakey[i] > yghost[j] - 20 && snakey[i] < yghost[j] + 30) {
                        ghostkilled++;
                        ghostvisble[j] = 0;
                    } else if (snakex[0] > xghost[j] - 20 && snakex[0] < xghost[j] + 30 && snakey[0] > yghost[j] - 20 && snakey[0] < yghost[j] + 30) {
                        g.setColor(Color.WHITE);
                        g.setFont(font);
                        g.drawString("YOU DIED TO A GHOST. Points :" + points, 80, 300);
                        g.drawString("ESC to exit. Enter to restart", 80, 350);
                        ghostcollision = true;
                    }
                }
            }

        }
        //if hit its tail, game ends
        for (int i = 1; i < lengthofsnake; i++) {
            if (snakex[0] > snakex[i] - 20 && snakex[0] < snakex[i] + 30 && snakey[0] > snakey[i] - 20 && snakey[0] < snakey[i] + 30) {
                bodycollision = true;
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString("You bumped into your body. GG", 150, 300);
                g.drawString("ESC to exit. Enter to restart", 80, 350);
            }
        }
        //if pause, draw the rules
        if (gamestart == false && bodycollision == false && exploded == false && ghostcollision == false) {
            g.drawString("Welcome to ETHAN'S SNAKE GAME!!!", 100, 330);
            g.drawString("Rules and Controls:", 100, 350);
            g.drawString("1. Don't run the snake into the wall, or his own tail: you die.", 100, 370);
            g.drawString("2. Use your cursor keys: up, left, right, and down.", 100, 390);
            g.drawString("3. Press shift up to speed, and press shift to slow down", 100, 410);
            g.drawString("4. SPACE may also be used for Play and Pause", 100, 430);
            g.drawString("5. Eat the colored apples to gain points. ------ >", 100, 450);
            g.drawString("6. CAREFUL THERE ARE BOMBS OUT THERE. BECAREFUL EACH TIME ------ >", 100, 470);
            g.drawString("7. THERE ARE GHOST OUT THERE, USE BODY TO KILL THEM ------ >", 100, 490);
            g.drawImage(bomb, 540, 450, 30, 30, this);
            g.drawImage(apple, 350, 430, 30, 30, this);
            g.drawImage(ghost, 480, 470, 30, 30, this);
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString("Press Space to Start or Pause.", 150, 300);
        }

        //restart the game, set all points, length of snake, number of ghost,direction, speed x and y of all variable to default
        if (restart == true) {
            points = 3;
            ghostkilled = 0;
            numberofghost = 1;
            lengthofsnake = 3;
            xghost[0] = 25;
            yghost[0] = random.nextInt(450) + 150;
            xapple = random.nextInt(800) + 25;
            yapple = random.nextInt(450) + 150;
            xbomb = random.nextInt(700) + 25;
            ybomb = random.nextInt(500) + 150;
            snakex[0] = 100;
            snakey[0] = 100;
            snakex[1] = 70;
            snakey[1] = 100;
            snakex[2] = 40;
            snakey[2] = 100;
            speedup = false;
            gamestart = false;
            bodycollision = false;
            exploded = false;
            restart = false;
            ghostcollision = false;
            for (int i = 0; i < numberofghost; i++) {
                ghostvisble[i] = 1;
            }
            direction = "Right";
            currentdirection = "Right";
        }

        repaint();
    }

    //perform the movement of the snake
    public void actionPerformed(ActionEvent e) {
        if (speedup) {//if press shit, the normal speed timer will stop, and change into a faster one
            timer.stop();//https://zhidao.baidu.com/question/51973793.html?qbl=relate_question_3 how to make timer stop
            timer2.start();
        } else {//if press shift again, the fast speed will stop and switch to normal speed
            timer2.stop();
            timer.start();
        }
        if (gamestart == true && exploded == false && bodycollision == false == ghostcollision == false) {//if gamestarted, and you do not hit any thing the snake will be able to move
            for (int i = lengthofsnake; i > 0; i--) {//intialize each snake body x and y
                snakex[i] = snakex[i - 1];
                snakey[i] = snakey[i - 1];
            }

            if (direction == "Right") {//snake head go right
                snakex[0] = snakex[0] + 30;
            } else if (direction == "Left") {//snake head go left
                snakex[0] = snakex[0] - 30;
            } else if (direction == "Up") {//snake head go up
                snakey[0] = snakey[0] - 30;
            } else if (direction == "Down") {//snake head go down
                snakey[0] = snakey[0] + 30;
            }
            for (int i = 0; i < lengthofsnake; i++) {// if touches the borderm the snake will appear on the other side
                if (snakex[i] > 855) {
                    snakex[i] = 22;
                } else if (snakex[i] < 22) {
                    snakex[i] = 855;
                } else if (snakey[i] > 640) {
                    snakey[i] = 63;
                } else if (snakey[i] < 63) {
                    snakey[i] = 640;
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char key = (char) e.getKeyCode();
        if (key == ' ') {
            gamestart = !gamestart; //press space to pause or start the game
        } else if (key == KeyEvent.VK_UP) {

            if (currentdirection != "Down") {// if the snake is not going down, you can go up
                direction = "Up";
                currentdirection = direction;
            }
        } else if (key == KeyEvent.VK_DOWN) {//if the snake is not going up you can go down
            if (currentdirection != "Up") {
                direction = "Down";
                currentdirection = direction;
            }
        } else if (key == KeyEvent.VK_RIGHT) {// if the sanke is not going left you can go right
            if (currentdirection != "Left") {
                direction = "Right";
                currentdirection = direction;
            }
        } else if (key == KeyEvent.VK_LEFT) {//if the snake is not going right you can go left
            if (currentdirection != "Right") {
                direction = "Left";
                currentdirection = direction;
            }
        } else if (key == KeyEvent.VK_SHIFT) {
            speedup = !speedup;//press shift to either speed up or slow down
        } else if (key == KeyEvent.VK_ENTER) {
            restart = true;//press enter to restart the game
        } else if (key == KeyEvent.VK_ESCAPE) {
            System.exit(0);//press escape to escape
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frame = new JFrame("SNAKE GAME");//game name
        frame.getContentPane().add(new SnakeGame());
        frame.setSize(910, 700);//set jfram size to 1200 800
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
