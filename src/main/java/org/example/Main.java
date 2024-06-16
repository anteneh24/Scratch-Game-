package org.example;

import org.example.model.Configuration;
import org.example.service.Game;
import org.example.utility.ConfigLoader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        if (args.length != 4 || !"--config".equals(args[0]) || !"--betting-amount".equals(args[2])) {
//            System.out.println("Usage: java -jar Main.java --config config.json --betting-amount 100");
//            return;
//        }

        String configFilePath = args[1];
        int bettingAmount = Integer.parseInt(args[3]);

//        String configFilePath = "src/main/resources/config.json";
//        int bettingAmount = 100;

        try {
            Configuration config = ConfigLoader.loadConfig(configFilePath);
            Game game = new Game(config, bettingAmount);
            game.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}