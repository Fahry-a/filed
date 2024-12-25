package com.filed;

import org.bukkit.plugin.java.JavaPlugin;
import com.filed.commands.FileCommand;

public class FileDownloaderPlugin extends JavaPlugin {
    private double downloadSpeed;

    @Override
    public void onEnable() {
        // Memuat konfigurasi default
        saveDefaultConfig();
        
        // Membaca kecepatan unduh dari konfigurasi
        downloadSpeed = getConfig().getDouble("speed", 1.0);
        
        // Mendaftarkan executor untuk perintah "/filed"
        this.getCommand("filed").setExecutor(new FileCommand(this));
    }
    
    // Mendapatkan kecepatan unduh
    public double getDownloadSpeed() {
        return downloadSpeed;
    }

    // Mengatur kecepatan unduh dan menyimpan ke konfigurasi
    public void setDownloadSpeed(double speed) {
        this.downloadSpeed = speed;
        getConfig().set("speed", speed);
        saveConfig();
    }

    // Memuat ulang konfigurasi
    public void reloadPlugin() {
        reloadConfig();
        downloadSpeed = getConfig().getDouble("speed", 1.0);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}