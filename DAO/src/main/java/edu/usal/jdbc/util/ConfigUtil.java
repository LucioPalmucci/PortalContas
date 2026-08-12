package edu.usal.jdbc.util;

import java.util.Properties;

public class ConfigUtil {
    private static ConfigUtil configUtil;
    private Properties props;

    public ConfigUtil() {
        props = new Properties();
        try {
            props.load(ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ConfigUtil getPropertyConfigInstance() {
        if (configUtil == null) {
            configUtil = new ConfigUtil();
        } return configUtil;
    }

    public String getKey(String propertyName) {
        return props.getProperty(propertyName);
    }
}
