package org.example.service;

import org.example.model.CellProbability;
import org.example.model.Configuration;
import org.example.model.Probabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MatrixGenerator {
    private final Configuration config;
    private final Random random;

    public MatrixGenerator(Configuration config) {
        this.config = config;
        this.random = new Random();
    }

    /**
     * Generates a matrix based on the configuration provided.
     *
     * @return A 2D array representing the generated matrix of symbols.
     */
    public String[][] generateMatrix() {
        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] matrix = new String[rows][columns];
        List<CellProbability> specificProbabilities = config.getProbabilities().standard_symbols;

        // Fill in the matrix using specific probabilities
        for (CellProbability prob : specificProbabilities) {
            matrix[prob.getRow()][prob.getColumn()] = getRandomSymbol(prob.getSymbols());
        }

        // Fill in the rest of the matrix using general probabilities
        Map<String, Integer> generalProbabilities = specificProbabilities.isEmpty() ? new HashMap<>() : specificProbabilities.get(0).getSymbols();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (matrix[row][col] == null) {
                    matrix[row][col] = getRandomSymbol(generalProbabilities);
                }
            }
        }

        // Optionally, add bonus symbols randomly in the matrix
        addBonusSymbols(matrix);

        return matrix;
    }

    /**
     * Adds bonus symbols randomly into the matrix based on the configuration.
     *
     * @param matrix The matrix to which bonus symbols will be added.
     */
    private void addBonusSymbols(String[][] matrix) {
        Map<String, Integer> bonusProbabilities = config.getProbabilities().bonus_symbols.getSymbols();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                // Randomly decide if we want to place a bonus symbol in this cell
                if (random.nextDouble() < 0.1) { //10% chance to place a bonus symbol
                    matrix[row][col] = getRandomSymbol(bonusProbabilities);
                }
            }
        }
    }

    /**
     * Gets a random symbol based on given probabilities.
     *
     * @param symbolProbabilities A map containing symbols and their respective probabilities.
     * @return A randomly selected symbol based on the probabilities.
     */
    private String getRandomSymbol(Map<String, Integer> symbolProbabilities) {
        int totalProbability = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalProbability);

        int cumulativeProbability = 0;
        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue < cumulativeProbability) {
                return entry.getKey();
            }
        }
        return "MISS"; // Should not reach here if probabilities are defined correctly
    }


}