package org.example.service;

import org.example.model.Configuration;
import org.example.model.WinningCombination;

import java.util.*;

public class RewardCalculator {
    private final Configuration config;
    private final String[][] matrix;
    private final int bettingAmount;
    private int reward;
    private final Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol;

    public RewardCalculator(Configuration config, String[][] matrix, int bettingAmount) {
        this.config = config;
        this.matrix = matrix;
        this.bettingAmount = bettingAmount;
        this.reward = 0;
        this.appliedWinningCombinations = new HashMap<>();
        this.appliedBonusSymbol = null;
    }

    /**
     * Calculates the total reward based on the matrix and configuration.
     *
     * @return The calculated reward.
     */
    public int calculateReward() {
        Map<String, Integer> symbolCounts = getSymbolCounts();

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();
            if (symbol != null)
                for (Map.Entry<String, WinningCombination> winEntry : config.getWinCombinations().entrySet()) {
                    WinningCombination winCombination = winEntry.getValue();
                    if (winCombination.getWhen().equals("same_symbols") && count >= winCombination.getCount()) {
                        reward += bettingAmount * config.getSymbols().get(symbol).getReward_multiplier() * winCombination.getReward_multiplier();
                        appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(winEntry.getKey());
                    }
                }
        }

        // Check for linear patterns
        checkLinearPatterns();

        // Apply bonus symbols
        applyBonusSymbols();
        return reward;
    }
    /**
     * Counts occurrences of each symbol in the matrix.
     *
     * @return A map where keys are symbols and values are their counts.
     */
    private Map<String, Integer> getSymbolCounts() {
        Map<String, Integer> symbolCounts = new HashMap<>();

        for (String[] row : matrix) {
            for (String symbol : row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }

        return symbolCounts;
    }

    /**
     * Applies bonus symbols' effects on the reward if present in the matrix.
     */
    private void applyBonusSymbols() {
        Map<String, Integer> bonusSymbols = config.getProbabilities().bonus_symbols.getSymbols();
        if (reward > 0)
            for (String[] row : matrix) {
                for (String symbol : row) {
                    if (bonusSymbols.containsKey(symbol)) {
                        appliedBonusSymbol = symbol;
                        switch (config.getSymbols().get(symbol).getImpact()) {
                            case "multiply_reward":
                                reward *= config.getSymbols().get(symbol).getReward_multiplier();
                                break;
                            case "extra_bonus":
                                reward += config.getSymbols().get(symbol).getExtra();
                                break;
                            case "miss":
                                // Do nothing
                                break;
                        }
                    }
                }
            }
    }

    /**
     * Checks for linear patterns in the matrix and adds to the reward accordingly.
     */
    private void checkLinearPatterns() {
        for (Map.Entry<String, WinningCombination> winEntry : config.getWinCombinations().entrySet()) {
            WinningCombination winCombination = winEntry.getValue();
            if ("linear_symbols".equals(winCombination.getWhen())) {
                for (List<String> coveredArea : winCombination.getCovered_areas()) {
                    if (isWinningPattern(coveredArea)) {
                        String symbol = matrix[Integer.parseInt(coveredArea.get(0).split(":")[0])][Integer.parseInt(coveredArea.get(0).split(":")[1])];
                        reward += bettingAmount * config.getSymbols().get(symbol).getReward_multiplier() * winCombination.getReward_multiplier();
                        appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(winEntry.getKey());
                    }
                }
            }
        }
    }

    /**
     * Checks if a given set of matrix positions forms a winning pattern.
     *
     * @param coveredArea The list of matrix positions to check.
     * @return True if the positions form a winning pattern, false otherwise.
     */
    private boolean isWinningPattern(List<String> coveredArea) {
        String firstSymbol = null;
        for (String position : coveredArea) {
            int row = Integer.parseInt(position.split(":")[0]);
            int col = Integer.parseInt(position.split(":")[1]);
            String currentSymbol = matrix[row][col];
            if (firstSymbol == null) {
                firstSymbol = currentSymbol;
            } else if (!firstSymbol.equals(currentSymbol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Retrieves the winning combinations and their associated symbols.
     *
     * @return A map where keys are symbols and values are lists of winning combination IDs.
     */
    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    /**
     * Retrieves the symbol of the bonus applied during reward calculation.
     *
     * @return The symbol of the applied bonus, or null if no bonus was applied.
     */
    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }
}
