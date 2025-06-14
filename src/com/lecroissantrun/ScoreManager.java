package com.lecroissantrun;

import java.io.*;
import java.io.File;


public class ScoreManager {
    private static final String HIGHSCORE_FILE = "highscore.txt";

    public static int loadHighScore() {
    File file = new File(HIGHSCORE_FILE);
    if (!file.exists()) {
        saveHighScore(0); // create file with 0
        return 0;
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(HIGHSCORE_FILE))) {
        return Integer.parseInt(reader.readLine());
    } catch (IOException | NumberFormatException e) {
        return 0;
    }
}


    public static void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            System.err.println("Failed to save high score.");
        }
    }
}
