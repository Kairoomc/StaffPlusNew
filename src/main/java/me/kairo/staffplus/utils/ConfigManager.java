package me.kairo.staffplus.utils;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {
    
    private final StaffPlus plugin;
    private FileConfiguration config;
    
    public ConfigManager(StaffPlus plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
    
    public String getString(String path) {
        return config.getString(path, "");
    }
    
    public int getInt(String path) {
        return config.getInt(path, 0);
    }
    
    public boolean getBoolean(String path) {
        return config.getBoolean(path, false);
    }
    
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }
    
    public String getMessage(String key) {
        String prefix = getString("general.prefix");
        String message = getString("messages." + key);
        return prefix + message;
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}