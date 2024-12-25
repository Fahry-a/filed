package com.yourpackage.filed.utils;

import com.yourpackage.filed.FileDownloaderPlugin;
import java.io.*;
import java.net.URL;

public class FileDownloader {
    private final FileDownloaderPlugin plugin;
    private int etaFrequency;
    private long lastETATime;

    public FileDownloader(FileDownloaderPlugin plugin) {
        this.plugin = plugin;
        this.etaFrequency = plugin.getConfig().getInt("eta_frequency", 1); // Default frequency 1 second
        this.lastETATime = 0;
    }

    public void downloadFile(String fileURL, String saveDir) {
        File dir = new File(saveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            plugin.getLogger().severe("Gagal membuat direktori: " + saveDir);
            return;
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     new File(dir, fileURL.substring(fileURL.lastIndexOf('/') + 1)))) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long fileSize = new URL(fileURL).openConnection().getContentLengthLong();
            long startTime = System.currentTimeMillis();

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastETATime >= etaFrequency * 1000) {
                    // Hitung kecepatan unduh (MB/s) dan ETA (detik)
                    double elapsedTime = (currentTime - startTime) / 1000.0; // dalam detik
                    double speedMBps = (totalBytesRead * 8) / (elapsedTime * 1024 * 1024); // kecepatan dalam MB/s
                    double remainingTime = ((fileSize - totalBytesRead) * 8) / (speedMBps * 1024 * 1024); // ETA dalam detik

                    plugin.getLogger().info(String.format("Kecepatan: %.2f MB/s, ETA: %.2f detik", speedMBps, remainingTime));
                    lastETATime = currentTime;
                }
            }

            plugin.getLogger().info("Unduhan selesai: " + fileURL);
        } catch (IOException e) {
            plugin.getLogger().severe("Gagal mengunduh file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}