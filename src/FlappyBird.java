package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBird implements ActionListener, MouseListener, KeyListener
{
    public static FlappyBird flappyBird;
    private final int WIDTH = 800, HEIGHT = 800;
    private Game.Renderer renderer;
    private Rectangle bird;
    private ArrayList<Rectangle> columns;
    private Random rand;
    private int yMotion,score=0;
    private boolean gameOver;
    public boolean started = false;
    private FlappyBird() {
        JFrame frame = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Game.Renderer();
        rand = new Random();
        //configuring frame
        frame.setTitle("Flappy Bird");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.addMouseListener(this);
        frame.addKeyListener(this);
        frame.add(renderer);
        frame.setVisible(true);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }
    public void jump()
    {
        if(!started)
            started = true;
        if(gameOver)
        {   bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            gameOver = false;
            started = false;
            score = 0;
        }
        yMotion = 0;
        yMotion = yMotion - 10;
    }
    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        jump();
    }
    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.getKeyCode() == KeyEvent.VK_SPACE || keyEvent.getKeyCode() == KeyEvent.VK_UP)
            jump();
    }

    // unimplemented methods
    @Override public void mousePressed(MouseEvent mouseEvent){}@Override public void mouseReleased(MouseEvent mouseEvent){}@Override public void mouseEntered(MouseEvent mouseEvent){}@Override public void mouseExited(MouseEvent mouseEvent){}
    @Override public void keyTyped(KeyEvent keyEvent){} @Override public void keyReleased(KeyEvent keyEvent){}

    private void addColumn(boolean start)
    {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start)
        {	//adding columns
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else
        {	//if some of the columns has been removed/deleted
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }
    }
    private void paintColumn(Graphics g, Rectangle column)
    {
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    @Override
    public void actionPerformed(ActionEvent arg0)
    {
        int speed = 10;
        if(started)
        {
            //Move pipes
            for (Rectangle column : columns)
            {
                column.x -= speed;
            }
            //Gravity
            if (yMotion < 15)
            {
                yMotion += 1;
            }
            for (int i = 0; i < columns.size(); i++)
            {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0)
                {
                    columns.remove(column);
                    if (column.y == 0)
                    {
                        addColumn(false);
                    }
                }
            }
            bird.y += yMotion;
            for(int i=0;i<columns.size();i++)
            {
                Rectangle column = columns.get(i);
                if(column.intersects(bird))
                {
                    gameOver = true;
                    bird.x = column.x - bird.width;
                }
                if(bird.x > column.x - 10 && bird.x < column.x +10 && column.y == 0)
                    score++;
            }
            if (bird.y > HEIGHT - 140 || bird.y < 0)
            {
                bird.y = HEIGHT - 140;
                gameOver = true;
            }
        }
        renderer.repaint();
    }
    public void repaint(Graphics g)
    {
        // Background
        g.setColor(Color.cyan);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //Below Ground
        g.setColor(Color.orange);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        //Ground
        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 120, WIDTH, 20);

        //Bird
        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        //Pipes
        for (Rectangle column : columns)
            paintColumn(g, column);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 100));
        if(!started)
        {
            g.drawString("Click to start!",100,HEIGHT/2 - 50);
        }
        if (gameOver)
        {
            g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
        }
        if(started && !gameOver)
        {
            g.drawString(""+String.valueOf(score),WIDTH/2 - 25,100);
        }
    }

    public static void main(String[] args)
    {
        flappyBird = new FlappyBird();
    }
}
