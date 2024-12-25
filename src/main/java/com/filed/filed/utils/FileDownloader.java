package com.yourpackage.filed.utils;

import java.io.*;
import java.net.URL;

public class FileDownloader {

    public static void downloadFile(String fileURL, String saveDir) {
        File dir = new File(saveDir);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Gagal membuat direktori: " + saveDir);
            return;
        }

        try (BufferedInputStream in = new BufferedInputStream(new URL(fileURL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(
                     new File(dir, fileURL.substring(fileURL.lastIndexOf('/') + 1)))) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}