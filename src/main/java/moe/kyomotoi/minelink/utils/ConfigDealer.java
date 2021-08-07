package moe.kyomotoi.minelink.utils;

import net.md_5.bungee.config.Configuration;

public class ConfigDealer {

    public static Configuration config = null;

    public ConfigDealer(Configuration config) {
        this.config = config;
    }

    public static String getString(String path) {
        return config.getString(path);
    }

    public static Integer getInt(String path) {
        return config.getInt(path);
    }
}
