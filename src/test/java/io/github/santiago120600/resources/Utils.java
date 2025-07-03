package io.github.santiago120600.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    public static String getGlobalValue(String key) throws IOException {
        Properties properties = new Properties();
        FileInputStream file = new FileInputStream("src\\test\\java\\io\\github\\santiago120600\\resources\\global.properties");
        properties.load(file);
        return properties.getProperty(key);
    }
}
