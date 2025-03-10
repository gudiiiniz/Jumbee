package com.jumbee;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Bee {
    private final int x;
    private int y;
    private int velocity = 0;
    private BufferedImage beeImage;
    private final int width = 50;  //largura abelha
    private final int height = 50; //altura abelha

    public Bee(int startY) {
        this.x = 100;  //posicao inicial x
        this.y = startY;  //posicao inicial y
        loadBeeImage();
    }

    private void loadBeeImage() {
        try {
            beeImage = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/bee.gif")));

            if (beeImage == null) {
                throw new IOException("Imagem não encontrada");
            }

            beeImage = resizeImage(beeImage);

        } catch (IOException e) {
            System.out.println("Erro ao carregar a imagem da abelha: " + e.getMessage());
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage) {
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();

        return bufferedImage;
    }

    public void update() {
        int gravity = 1;
        velocity += gravity;
        y += velocity;

        if (y >= 600 - height) {  //abelha nao ultrapasse o nível do chão
            y = 600 - height;
            velocity = 0;
        }
    }

    public void jump() {
        if (y > 0) {  //abelha nao pula se estiver no topo da tela
            velocity = -10; //controla o quanto a abelha "pula"
        }
    }

    public void draw(Graphics2D g) {
        //desenha a imagem da abelha na posição atual
        g.drawImage(beeImage, x, y, null);
    }

    public void reset(int startY) {
        this.y = startY;  //reseta a posição para o valor inicial
        velocity = 0;
    }

    public int getX() {
        return x;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}