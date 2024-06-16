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

        applyBonusSymbols();
        return reward;
    }

    private Map<String, Integer> getSymbolCounts() {
        Map<String, Integer> symbolCounts = new HashMap<>();

        for (String[] row : matrix) {
            for (String symbol : row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }

        return symbolCounts;
    }

    private void applyBonusSymbols() {
        Random random = new Random();
        Map<String, Integer> bonusSymbols = config.getProbabilities().bonus_symbols.getSymbols();
        int totalWeight = bonusSymbols.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (Map.Entry<String, Integer> entry : bonusSymbols.entrySet()) {
            currentWeight += entry.getValue();
            if (randomValue < currentWeight) {
                String bonusSymbol = entry.getKey();
                appliedBonusSymbol = bonusSymbol;
                switch (config.getSymbols().get(bonusSymbol).impact) {
                    case "multiply_reward":
                        reward *= config.getSymbols().get(bonusSymbol).reward_multiplier;
                        break;
                    case "extra_bonus":
                        reward += config.getSymbols().get(bonusSymbol).extra;
                        break;
                    case "miss":
                        // Do nothing
                        break;
                }
                break;
            }
        }
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }
}
