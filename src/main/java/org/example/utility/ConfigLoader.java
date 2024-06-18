package org.example.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Configuration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {

    /**
     * Loads a Configuration object from a JSON file located at the specified path.
     *
     * @param configFilePath The path to the JSON configuration file.
     * @return A Configuration object parsed from the JSON file.
     * @throws IOException If there is an error reading or parsing the JSON file.
     */
    public static Configuration loadConfig(String configFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(configFilePath), Configuration.class);
    }
}
