package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, ActionListener {
    private static final int TILE_SIZE = 20;
    private static final int TILE_COUNT = 20;
    
    private int[] snakeX;
    private int[] snakeY;
    private int snakeLength;
    
    private int foodX;
    private int foodY;
    
    private char direction;
    private boolean isGameOver;
    private boolean isPaused;
    
    private Timer timer;
    private int score;
    
    public SnakeGame() {
        snakeX = new int[TILE_COUNT * TILE_COUNT];
        snakeY = new int[TILE_COUNT * TILE_COUNT];
        snakeLength = 3;
        
        direction = 'R';
        isGameOver = false;
        isPaused = false;
        score = 0;
        
        snakeX[0] = 5;
        snakeY[0] = 5;
        snakeX[1] = 4;
        snakeY[1] = 5;
        snakeX[2] = 3;
        snakeY[2] = 5;
        
        spawnFood();
        
        timer = new Timer(150, this);
        timer.start();
        
        setFocusable(true);
        addKeyListener(this);
    }
    
    private void spawnFood() {
        Random random = new Random();
        boolean valid;
        do {
            valid = true;
            foodX = random.nextInt(TILE_COUNT);
            foodY = random.nextInt(TILE_COUNT);
            
            for (int i = 0; i < snakeLength; i++) {
                if (snakeX[i] == foodX && snakeY[i] == foodY) {
                    valid = false;
                    break;
                }
            }
        } while (!valid);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        for (int i = 0; i < snakeLength; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.YELLOW);
            }
            g.fillRect(snakeX[i] * TILE_SIZE, snakeY[i] * TILE_SIZE, TILE_SIZE - 2, TILE_SIZE - 2);
        }
        
        g.setColor(Color.RED);
        g.fillRect(foodX * TILE_SIZE, foodY * TILE_SIZE, TILE_SIZE - 2, TILE_SIZE - 2);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 30);
        g.drawString("按 R 键重启", 10, 55);
        
        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String msg = "Game Over!";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g.drawString(msg, x, y);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("按 R 键重启", x + 50, y + 40);
        }
        
        if (isPaused && !isGameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String msg = "Paused";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g.drawString(msg, x, y);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !isPaused) {
            moveSnake();
        }
        repaint();
    }
    
    private void moveSnake() {
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        
        switch (direction) {
            case 'L': snakeX[0]--; break;
            case 'R': snakeX[0]++; break;
            case 'U': snakeY[0]--; break;
            case 'D': snakeY[0]++; break;
        }
        
        if (snakeX[0] < 0 || snakeX[0] >= TILE_COUNT || 
            snakeY[0] < 0 || snakeY[0] >= TILE_COUNT) {
            isGameOver = true;
            return;
        }
        
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                isGameOver = true;
                return;
            }
        }
        
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            score += 10;
            spawnFood();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_R) {
            resetGame();
            return;
        }
        
        if (key == KeyEvent.VK_P) {
            isPaused = !isPaused;
            return;
        }
        
        if (isPaused || isGameOver) return;
        
        switch (key) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
        }
    }
    
    private void resetGame() {
        snakeLength = 3;
        snakeX[0] = 5;
        snakeY[0] = 5;
        snakeX[1] = 4;
        snakeY[1] = 5;
        snakeX[2] = 3;
        snakeY[2] = 5;
        
        direction = 'R';
        isGameOver = false;
        isPaused = false;
        score = 0;
        
        spawnFood();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        
        frame.add(game);
        frame.setSize(TILE_COUNT * TILE_SIZE + 10, TILE_COUNT * TILE_SIZE + 35);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}