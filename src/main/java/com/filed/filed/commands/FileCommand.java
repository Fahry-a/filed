package com.filed.filed.commands;

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
            sender.sendMessage("Usage: /filed <download|reload>");
            return true;
        }

        if (args[0].equalsIgnoreCase("download") && args.length == 2) {
            String url = args[1];
            FileDownloader.downloadFile(url, plugin.getConfig().getString("download-directory"));
            sender.sendMessage("Downloading file from: " + url);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPlugin();
            sender.sendMessage("Plugin configuration reloaded.");
            return true;
        }

        sender.sendMessage("Usage: /filed <download|reload>");
        return true;
    }
}