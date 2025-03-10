package com.jumbee;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class ObstacleManager {
    private final int baseWidth;
    private final int baseHeight;
    private final ArrayList<Obstacle> obstacles;
    private final Random random;

    public ObstacleManager(int baseWidth, int baseHeight) {
        this.baseWidth = baseWidth;
        this.baseHeight = baseHeight;
        this.random = new Random();
        this.obstacles = new ArrayList<>();
        addInitialObstacles();
    }

    private void addInitialObstacles() {
        for (int i = 0; i < 3; i++) {
            addObstacle(baseWidth + (i * 300));
        }
    }

    public void update() {
        moveObstacles();
        removeOffscreenObstacles();
        addNewObstacles();
    }

    public void draw(Graphics g) {
        g.setColor(new Color(151, 78, 20, 255));
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }
    }

    public boolean checkCollision(Bee bee) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getBounds().intersects(bee.getBounds())) {
                return true; //colisao detectada
            }
        }
        return false; //nao houve colisao
    }

    public int checkScore(int birdX) {
        int scored = 0;
        for (int i = 0; i < obstacles.size(); i += 2) {
            if (!obstacles.get(i).isScored() && obstacles.get(i).getX() + obstacles.get(i).getWidth() < birdX) {
                scored++;
                obstacles.get(i).markScored();
                obstacles.get(i + 1).markScored();
            }
        }
        return scored;
    }

    private void moveObstacles() {
        for (Obstacle obstacle : obstacles) {
            int scrollSpeed = 3;
            obstacle.move(scrollSpeed);
        }
    }

    private void removeOffscreenObstacles() {
        obstacles.removeIf(obstacle -> obstacle.getX() + obstacle.getWidth() < 0);
    }

    private void addNewObstacles() {
        if (obstacles.getLast().getX() < baseWidth - 300) {
            addObstacle(baseWidth);
        }
    }

    private void addObstacle(int x) {
        int height = random.nextInt(baseHeight / 2 - 100) + 100;
        int gap = 150;  // O valor de gap agora está sendo utilizado
        int pipeWidth = 50;

        //adicionando dois obstaculos com o gap entre eles
        obstacles.add(new Obstacle(x, 0, pipeWidth, height));
        obstacles.add(new Obstacle(x, height + gap, pipeWidth, baseHeight - height - gap));  // Utiliza 'gap' aqui
    }

    public void reset() {
        obstacles.clear();
        addInitialObstacles();
    }

    public static class Obstacle {
        private final Rectangle bounds;
        private boolean scored = false;

        public Obstacle(int x, int y, int width, int height) {
            this.bounds = new Rectangle(x, y, width, height);
        }

        public void move(int speed) {
            bounds.x -= speed;
        }

        public void draw(Graphics g) {
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public boolean isScored() {
            return scored;
        }

        public void markScored() {
            scored = true;
        }

        public int getX() {
            return bounds.x;
        }

        public int getWidth() {
            return bounds.width;
        }
    }
}
