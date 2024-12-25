package com.yourpackage.filed.commands;

import com.yourpackage.filed.FileDownloaderPlugin;
import com.yourpackage.filed.utils.FileDownloader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FileCommand implements CommandExecutor {

    private final FileDownloaderPlugin plugin;

    public FileCommand(FileDownloaderPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /filed <download|reload|setspeed>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "download":
                if (args.length == 2) {
                    String url = args[1];
                    String saveDir = plugin.getConfig().getString("download-directory", "downloads");
                    FileDownloader.downloadFile(url, saveDir);
                    sender.sendMessage("Downloading file from: " + url);
                } else {
                    sender.sendMessage("Usage: /filed download <url>");
                }
                break;

            case "reload":
                plugin.reloadPlugin();
                sender.sendMessage("Plugin configuration reloaded.");
                break;

            case "setspeed":
                if (args.length == 2) {
                    try {
                        double speed = Double.parseDouble(args[1]);
                        plugin.setDownloadSpeed(speed);
                        sender.sendMessage("Kecepatan unduh diatur ke " + speed + " MBps");
                    } catch (NumberFormatException e) {
                        sender.sendMessage("Kecepatan unduh harus berupa angka.");
                    }
                } else {
                    sender.sendMessage("Usage: /filed setspeed <speed>");
                }
                break;

            default:
                sender.sendMessage("Usage: /filed <download|reload|setspeed>");
        }
        return true;
    }
}