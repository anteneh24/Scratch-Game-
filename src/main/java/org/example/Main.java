package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.Configuration;
import org.example.service.Game;
import org.example.utility.ConfigLoader;

import java.io.IOException;

public class Main {

    /**
     * Main entry point of the Scratch Game application.
     *
     * @param args Command-line arguments. Expected format: --config <config-file> --betting-amount <amount>
     */
    public static void main(String[] args) {
        if (args.length != 4 || !args[0].equals("--config") || !args[2].equals("--betting-amount")) {
            System.err.println("Usage: java -jar <your-jar-file> --config <config-file> --betting-amount <amount>");
            //java -jar ScratchGame-1.0-SNAPSHOT.jar --config config.json --betting-amount 100
            System.exit(1);
        }

        String configFile = args[1];
        int bettingAmount;
        try {
            bettingAmount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Invalid betting amount. Please provide a valid number.");
            System.exit(1);
            return;
        }

        try {
            // Load the configuration from the specified JSON file
            Configuration config = ConfigLoader.loadConfig(configFile);
            // Initialize the game with the loaded configuration and betting amount
            Game game = new Game(config, bettingAmount);
            // Play the game and get the result as a JSON string
            String resultJson = game.play();
            System.out.println(resultJson);
        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
            System.exit(1);
        }
    }

}