package com.yourpackage.filed.utils;

import com.yourpackage.filed.FileDownloaderPlugin;
import java.io.*;
import java.net.URL;

public class DownloadManager {
    private final FileDownloaderPlugin plugin;

    public DownloadManager(FileDownloaderPlugin plugin) {
        this.plugin = plugin;
    }

    public void startDownload(String fileUrl, File destination) {
        double speed = plugin.getDownloadSpeed(); // Kecepatan unduh dalam MBps
        long speedBytesPerSecond = (long) (speed * 1024 * 1024); // Byte per detik

        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long startTime = System.currentTimeMillis();

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                // Simulasi kecepatan unduh
                long sleepTime = (bytesRead * 1000L) / speedBytesPerSecond;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            }
        } catch (IOException | InterruptedException e) {
            plugin.getLogger().severe("Error downloading file: " + e.getMessage());
        }
    }
}