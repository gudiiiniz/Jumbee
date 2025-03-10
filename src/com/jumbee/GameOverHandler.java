package com.jumbee;

import java.awt.*;

public class GameOverHandler {

    private final Rectangle restartButton;
    private final Rectangle menuButton;

    public GameOverHandler() {
        //posicionamento dos botoes
        restartButton = new Rectangle(325, 400, 150, 40);
        menuButton = new Rectangle(325, 450, 150, 40);
    }

    public void draw(Graphics2D g, Dimension dimension, int score) {
        Font gameOverFont = new Font("JetBrains Mono", Font.BOLD, 50);
        Font scoreFont = new Font("JetBrains Mono", Font.BOLD, 40);

        String gameOverText = "GAME OVER!";
        String scoreText = "Score: " + score;

        //posicoes para centralizar o texto
        FontMetrics metrics = g.getFontMetrics(gameOverFont);
        int x = (dimension.width - metrics.stringWidth(gameOverText)) / 2;
        int y = (dimension.height - metrics.getHeight()) / 2;

        //calculos para largura e altura do fundo semitransparente
        int padding = 20;
        int width = Math.max(metrics.stringWidth(gameOverText), g.getFontMetrics(scoreFont).stringWidth(scoreText)) + 2 * padding;
        int height = (metrics.getHeight() * 2) + (2 * padding) + 240; //aumentando a altura do fundo

        //desenhando o fundo semitransparente
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect((dimension.width - width) / 2, (dimension.height - height) / 2, width, height, 30, 30);

        //desenhando o texto "GAME OVER"
        g.setFont(gameOverFont);
        g.setColor(Color.WHITE);
        g.drawString(gameOverText, x, y);

        //desenhando a pontuacao
        g.setFont(scoreFont);
        metrics = g.getFontMetrics(scoreFont);
        x = (dimension.width - metrics.stringWidth(scoreText)) / 2;
        y += metrics.getHeight() + 20; //ajusta a posição do texto da pontuação
        g.drawString(scoreText, x, y);

        //desenhando os botoes
        g.setColor(Color.WHITE);
        g.fillRoundRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height, 20, 20);
        g.fillRoundRect(menuButton.x, menuButton.y, menuButton.width, menuButton.height, 20, 20);

        //desenhando o texto nos botoes
        g.setColor(Color.BLACK);
        g.setFont(new Font("JetBrains Mono-Bold", Font.BOLD, 20));
        g.drawString("Reiniciar", restartButton.x + 30, restartButton.y + 25);
            g.drawString("Menu", menuButton.x + 45, menuButton.y + 25);
    }

    public void handleClick(Point click, Runnable restartAction, Runnable menuAction) {
        //verifica se o clique foi nos botoes
        if (restartButton.contains(click)) {
            restartAction.run();  //reinicia o jogo
        } else if (menuButton.contains(click)) {
            menuAction.run();  //volta para o menu
        }
    }
}