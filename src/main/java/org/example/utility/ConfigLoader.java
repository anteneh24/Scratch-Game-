package org.example.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Configuration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    public static Configuration loadConfig(String configFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(configFilePath), Configuration.class);
    }
}
