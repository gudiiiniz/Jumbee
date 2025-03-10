package com.jumbee;

import javax.sound.sampled.*;
import java.io.*;
import java.util.logging.*;

public class SoundManager {

    private static final Logger logger = Logger.getLogger(SoundManager.class.getName());
    private FloatControl gainControl;
    private float currentVolume = 0.3f;

    //tocar o som
    public void playSound(String soundName) {
        try {
            String resourcePath = "/sounds/" + soundName + ".wav";
            System.out.println("Procurando o arquivo de som: " + resourcePath);

            InputStream soundInputStream = SoundManager.class.getResourceAsStream(resourcePath);

            if (soundInputStream == null) {
                System.err.println("Arquivo de som não encontrado: " + resourcePath);
                return;
            }

            BufferedInputStream bufferedIn = new BufferedInputStream(soundInputStream);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(currentVolume);

            clip.start();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao carregar ou reproduzir o som", e);
        }
    }

    //ajustar o volume no codigo
    public void setVolume(float volume) {
        this.currentVolume = volume;
        if (gainControl != null) {
            gainControl.setValue(20f * (float) Math.log10(volume));
        }
    }
}