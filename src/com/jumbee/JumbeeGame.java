package com.jumbee;

import javax.swing.*;
import java.awt.*;

public class JumbeeGame extends JFrame {
    private final GamePanel gamePanel;
    private final CardLayout cardLayout;

    public JumbeeGame() {
        setTitle("Jumbee");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //navegacao entre menu e jogo
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        //criando o painel do menu
        MainMenuPanel mainMenuPanel = new MainMenuPanel(this);
        add(mainMenuPanel, "Menu");

        //criando o painel do jogo
        gamePanel = new GamePanel();
        add(gamePanel, "Game");

        pack();
        setLocationRelativeTo(null);

        setVisible(true);
    }

    //inicia o jogo
    public void startGame() {
        cardLayout.show(getContentPane(), "Game");
        gamePanel.startGame();
    }

    //voltar ao menu
    public void showMainMenu() {
        cardLayout.show(getContentPane(), "Menu");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(JumbeeGame::new);
    }
}
