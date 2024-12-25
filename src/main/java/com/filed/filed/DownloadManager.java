package com.filed.utils;

import com.filed.FileDownloaderPlugin;
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
        long fileSize = 0; // Deklarasi dan inisialisasi fileSize

        try {
            // Inisialisasi fileSize dengan ukuran file yang akan diunduh
            fileSize = new URL(fileUrl).openConnection().getContentLengthLong();

            try (InputStream in = new URL(fileUrl).openStream();
                 FileOutputStream out = new FileOutputStream(destination)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;
                long startTime = System.currentTimeMillis();

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    // Pembatasan kecepatan unduh
                    long sleepTime = (bytesRead * 1000L) / speedBytesPerSecond;
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            plugin.getLogger().severe("Download interrupted: " + e.getMessage());
                            return;
                        }
                    }

                    long currentTime = System.currentTimeMillis();
                    if (currentTime - startTime >= 1000) {
                        // Hitung kecepatan unduh (MB/s) dan ETA (detik)
                        double elapsedTime = (currentTime - startTime) / 1000.0; // dalam detik
                        double speedMBps = (totalBytesRead / elapsedTime) / (1024 * 1024); // kecepatan dalam MB/s
                        double remainingTime = ((fileSize - totalBytesRead) / speedBytesPerSecond); // ETA dalam detik

                        // Log tambahan untuk debugging
                        plugin.getLogger().info(String.format("totalBytesRead: %d, elapsedTime: %.2f, speedMBps: %.2f", totalBytesRead, elapsedTime, speedMBps));

                        plugin.getLogger().info(String.format("Kecepatan: %.2f MB/s, ETA: %.2f detik", speedMBps, remainingTime));
                        startTime = currentTime;
                    }
                }

                plugin.getLogger().info("Unduhan selesai: " + fileUrl);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Gagal mengunduh file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}