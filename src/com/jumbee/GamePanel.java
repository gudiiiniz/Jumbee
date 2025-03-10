package com.jumbee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends JPanel {
    private static final Logger logger = Logger.getLogger(GamePanel.class.getName());
    private static final int BASE_WIDTH = 800;
    private static final int BASE_HEIGHT = 600;

    private boolean running = false;
    private final Bee bee;
    private final ObstacleManager obstacleManager;
    private final SoundManager soundManager;
    private final GameOverHandler gameOverHandler;

    private int score = 0;
    private boolean gameOver = false;
    private long startTime;

    //variáveis para controlar o 'pulo' via mouse
    private boolean mouseJumpReady = true;
    private long lastJumpTime = 0;
    private static final long COOLDOWN_TIME = 1;

    private BufferedImage backgroundImage;

    public GamePanel() {
        setPreferredSize(new Dimension(BASE_WIDTH, BASE_HEIGHT));
        setFocusable(true);
        setBackground(Color.BLACK);

        bee = new Bee(BASE_HEIGHT / 2);
        obstacleManager = new ObstacleManager(BASE_WIDTH, BASE_HEIGHT);
        soundManager = new SoundManager();
        gameOverHandler = new GameOverHandler(); //inicializa a classe gameover

        try {
            backgroundImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/background.png")));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Erro ao carregar a imagem de fundo.", e);
        }

        setupInput(); //configura o KeyListener e MouseListener
    }

    private void setupInput() {
        //configura o KeyListener para w/space
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_W)) {
                    performJump();
                }
            }
        });

        // Múltiplos listeners para o mouse para máxima responsividade
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseInput(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseJumpReady = true;
            }
        };

        addMouseListener(mouseAdapter);
    }

    private void handleMouseInput(MouseEvent e) {
        if (gameOver) {
            gameOverHandler.handleClick(e.getPoint(), this::startGame,
                    () -> ((JumbeeGame) SwingUtilities.getWindowAncestor(GamePanel.this)).showMainMenu());
        } else if (mouseJumpReady) {
            performJump();
            //impedir pulos ate que o botão do mouse seja solto
            mouseJumpReady = false;
        }
    }

    private void performJump() {
        long currentTime = System.currentTimeMillis();
        //cooldown para evitar pulos muito rapidos
        if (currentTime - lastJumpTime >= COOLDOWN_TIME) {
            bee.jump();
            soundManager.playSound("jump");
            lastJumpTime = currentTime;
        }
    }

    public void startGame() {
        running = true;
        gameOver = false;
        score = 0;
        startTime = System.currentTimeMillis();
        lastJumpTime = 0;
        mouseJumpReady = true;

        bee.reset(BASE_HEIGHT / 2);
        obstacleManager.reset();

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (running) {
                    updateGame();
                    SwingUtilities.invokeLater(() -> repaint());
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000 / 60); //60fps

        requestFocusInWindow();
    }

    private void updateGame() {
        if (!gameOver) {
            bee.update();
            obstacleManager.update();
            checkCollisions();
            checkScore();
        }
    }

    private void checkCollisions() {
        if (obstacleManager.checkCollision(bee)) {  //verifica se houve colisao
            triggerGameOver();
        }
    }

    private void checkScore() {
        int newScore = obstacleManager.checkScore(bee.getX());
        if (newScore > 0) {
            score += newScore;
            soundManager.playSound("score");
        }
    }

    private void triggerGameOver() {
        gameOver = true;
        running = false;
        soundManager.playSound("gameover");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderToBuffer(g);
    }

    private void renderToBuffer(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, BASE_WIDTH, BASE_HEIGHT, null); //background
        bee.draw((Graphics2D) g);
        obstacleManager.draw(g);
        drawScore(g);
        drawTimer(g);
        if (gameOver) {
            gameOverHandler.draw((Graphics2D) g, new Dimension(BASE_WIDTH, BASE_HEIGHT), score);
        }
    }

    //desenha o score
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        g.drawString("Score: " + score, 20, 40);
    }

    //desenha o timer
    private void drawTimer(Graphics g) {
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
        g.setColor(Color.WHITE);
        g.setFont(new Font("JetBrains Mono", Font.BOLD, 30));
        String timerText = "Tempo: " + elapsedTime + "s";
        int textWidth = g.getFontMetrics().stringWidth(timerText);
        g.drawString(timerText, BASE_WIDTH - textWidth - 20, 40);
    }
}