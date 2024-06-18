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
    }

    @Test
    public void testLostGame() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "5x"},
                {"F", "D", "C"}
        };
        int bettingAmount = 100;

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        assertEquals(0, reward);
        assertEquals(0, calculator.getAppliedWinningCombinations().size());
        assertNull(calculator.getAppliedBonusSymbol());
    }

    @Test
    public void testWonGameWithBonus() {
        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "10x"},
                {"F", "D", "B"}
        };
        int bettingAmount = 100;

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        assertEquals(3000, reward);
        Map<String, List<String>> appliedWinningCombinations = calculator.getAppliedWinningCombinations();
        assertEquals(1, appliedWinningCombinations.size());
        assertEquals("same_symbol_3_times", appliedWinningCombinations.get("B").get(0));
        assertEquals("10x", calculator.getAppliedBonusSymbol());
    }

    @Test
    public void testLinearPatternWin() {
        String[][] matrix = {
                {"A", "A", "A"},
                {"B", "B", "B"},
                {"C", "C", "C"}
        };
        int bettingAmount = 100;

        RewardCalculator calculator = new RewardCalculator(config, matrix, bettingAmount);
        int reward = calculator.calculateReward();

        // Assuming the configuration has linear pattern win setup for the provided matrix
        // Update expected reward and winning combinations based on your config
        assertEquals(3150, reward);
        Map<String, List<String>> appliedWinningCombinations = calculator.getAppliedWinningCombinations();
        assertEquals(3, appliedWinningCombinations.size());
        assertEquals("same_symbols_horizontally", appliedWinningCombinations.get("A").get(1));
    }
}
