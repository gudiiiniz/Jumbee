package com.jumbee;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(JumbeeGame game) {

        setLayout(new GridBagLayout()); //centraliza os botoes
        setOpaque(false); //fundo transparente para exibir a imagem

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0); // Espaçamento entre os botões
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //titulo
        JLabel titleLabel = new JLabel("JUMBEE");
        titleLabel.setFont(new Font("JetBrains Mono-Bold", Font.BOLD, 60));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JButton startButton = createButton("Jogar");
        startButton.addActionListener(_-> game.startGame());
        gbc.gridy = 1;
        add(startButton, gbc);

        JButton quitButton = createButton("Sair");
        quitButton.addActionListener(_ -> System.exit(0));
        gbc.gridy = 2;
        add(quitButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new RoundedButton(text);
        button.setBackground(new Color(50, 150, 250)); // Cor azul vibrante
        button.setForeground(Color.WHITE);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/background.png")));
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }

    // Classe para botões arredondados
    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("JetBrains Mono", Font.BOLD, 30));
            setPreferredSize(new Dimension(250, 70));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            super.paintComponent(g);
            g2.dispose();
        }
    }
}