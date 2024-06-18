package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public String play() throws JsonProcessingException {
        MatrixGenerator generator = new MatrixGenerator(config);
        String[][] matrix = generator.generateMatrix();

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        // Create a result object to hold the output
        Map<String, Object> result = new HashMap<>();
        result.put("matrix", matrix);
        result.put("reward", reward);
        result.put("appliedWinningCombinations", calculator.getAppliedWinningCombinations());
        result.put("appliedBonusSymbol", calculator.getAppliedBonusSymbol());

        // Convert the result object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(result);
    }
}
