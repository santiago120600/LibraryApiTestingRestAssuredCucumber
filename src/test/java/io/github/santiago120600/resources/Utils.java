package io.github.santiago120600.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

    public static String getGlobalValue(String key) throws IOException {
        Properties properties = new Properties();
        try (InputStream inputStream = Utils.class.getClassLoader().getResourceAsStream("io/github/santiago120600/resources/global.properties")) {
            if (inputStream == null) {
                throw new IOException("global.properties file not found in classpath");
            }
            properties.load(inputStream);
        }
        return properties.getProperty(key);
    }
}
