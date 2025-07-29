package io.github.santiago120600.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);


    public static String getGlobalValue(String key) throws IOException {
        String env = System.getProperty("config", "docker");
        logger.info("Loading properties for environment: {}", env);

        String propFile = env.equals("local") ? "global-local.properties" : "global.properties";
        Properties properties = new Properties();
        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream(propFile)) {
            if (inputStream == null) {
                throw new IOException(propFile + " file not found in classpath");
            }
            properties.load(inputStream);
        }
        return properties.getProperty(key);
    }
}
