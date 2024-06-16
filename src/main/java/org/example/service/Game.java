package org.example.service;

import org.example.model.Configuration;
import org.example.model.Symbol;
import org.example.model.CellProbability;
import org.example.model.WinningCombination;

import java.util.*;

public class Game {
    private final Configuration config;
    private final int bettingAmount;

    public Game(Configuration config, int bettingAmount) {
        this.config = config;
        this.bettingAmount = bettingAmount;
    }

    public void play() {
        MatrixGenerator generator = new MatrixGenerator(config);
        String[][] matrix = generator.generateMatrix();

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        // Print the results
        System.out.println("Matrix:");
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        System.out.println("Reward: " + reward);
        System.out.println("Applied Winning Combinations: " + calculator.getAppliedWinningCombinations());
        System.out.println("Applied Bonus Symbol: " + calculator.getAppliedBonusSymbol());
    }
}
