package org.example.service;

import org.example.model.CellProbability;
import org.example.model.Configuration;

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

    public String[][] generateMatrix() {
        String[][] matrix = new String[config.getRows()][config.getColumns()];
        List<CellProbability> probabilities = config.getProbabilities().standard_symbols;

        for (CellProbability prob : probabilities) {
            matrix[prob.getRow()][prob.getColumn()] = getRandomSymbol(prob.getSymbols());
        }

        return matrix;
    }

    private String getRandomSymbol(Map<String, Integer> symbols) {
        int totalWeight = symbols.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue < currentWeight) {
                return entry.getKey();
            }
        }

        return null;
    }
}