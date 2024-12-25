package com.yourpackage.filed;

import org.bukkit.plugin.java.JavaPlugin;
import com.yourpackage.filed.commands.FileCommand;

public class FileDownloaderPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("filed").setExecutor(new FileCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadPlugin() {
        reloadConfig();
    }
}