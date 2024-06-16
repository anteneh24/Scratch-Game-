import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Configuration;
import org.example.service.Game;
import org.example.service.MatrixGenerator;
import org.example.service.RewardCalculator;
import org.example.utility.ConfigLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GameTest {
    private Configuration config;
    private final int bettingAmount = 100;

    @Before
    public void setUp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.readValue(new File("src/main/resources/config.json"), Configuration.class);
        System.out.println("Started....");
    }

    @Test
    public void testMatrixGeneration() {
        MatrixGenerator generator = new MatrixGenerator(config);
        String[][] matrix = generator.generateMatrix();

        assertNotNull(matrix);
        assertEquals(config.getRows(), matrix.length);
        assertEquals(config.getColumns(), matrix[0].length);
    }

    @Test
    public void testRewardCalculation() {
        String[][] matrix = {
                {"A", "A", "A", "B"},
                {"B", "C", "D", "E"},
                {"F", "A", "A", "A"},
                {"B", "C", "D", "E"}
        };

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        assertEquals(500, reward);
    }

    @Test
    public void testAppliedWinningCombinations() {
        String[][] matrix = {
                {"A", "A", "A", "B"},
                {"B", "C", "D", "E"},
                {"F", "A", "A", "A"},
                {"B", "C", "D", "E"}
        };

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        calculator.calculateReward();
        Map<String, List<String>> appliedWinningCombinations = calculator.getAppliedWinningCombinations();

        assertTrue(appliedWinningCombinations.containsKey("A"));
        assertTrue(appliedWinningCombinations.get("A").contains("same_symbol_3_times"));
    }

    @Test
    public void testBonusSymbolApplication() {
        String[][] matrix = {
                {"A", "A", "A", "B"},
                {"B", "C", "D", "E"},
                {"F", "A", "A", "A"},
                {"B", "C", "D", "E"}
        };

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        calculator.calculateReward();
        String appliedBonusSymbol = calculator.getAppliedBonusSymbol();

        assertNotNull(appliedBonusSymbol);
        assertTrue(List.of("10x", "5x", "+1000", "+500", "MISS").contains(appliedBonusSymbol));
    }
}
