package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Configuration {

    private int columns;
    private int rows;
    private Map<String, Symbol> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinningCombination> winCombinations;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(Map<String, Symbol> symbols) {
        this.symbols = symbols;
    }

    public Probabilities getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(Probabilities probabilities) {
        this.probabilities = probabilities;
    }

    public Map<String, WinningCombination> getWinCombinations() {
        return winCombinations;
    }

    public void setWinCombinations(Map<String, WinningCombination> winCombinations) {
        this.winCombinations = winCombinations;
    }
}
