package io.github.santiago120600.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getGlobalValue(String key) throws IOException {
        String env = System.getProperty("config", "docker");
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
