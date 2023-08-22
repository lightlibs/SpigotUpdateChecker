package com.tcoded.updatechecker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleUpdateChecker {

    public static void checkUpdate(JavaPlugin plugin, String prefix, int resourceId) {
        Server server = plugin.getServer();
        ConsoleCommandSender consoleSender = server.getConsoleSender();
        PluginDescriptionFile pluginDesc = plugin.getDescription();
        String pluginVersion = pluginDesc.getVersion();
        String pluginName = pluginDesc.getName();

        server.getScheduler().runTaskAsynchronously(plugin, () -> {
            consoleSender.sendMessage(prefix + "Checking for updates...");

            final UpdateResult result = new UpdateChecker(pluginVersion, resourceId).getResult();

            int prioLevel = 0;
            String prioColor = ChatColor.AQUA.toString();
            String prioLevelName = "null";

            switch (result.getType()) {
                case FAIL_SPIGOT:
                    consoleSender.sendMessage(prefix + ChatColor.GOLD + "Warning: Could not contact Spigot to check if an update is available.");
                    break;
                case UPDATE_LOW:
                    prioLevel = 1;
                    prioLevelName = "minor";
                    break;
                case UPDATE_MEDIUM:
                    prioLevel = 2;
                    prioLevelName = "feature";
                    prioColor = ChatColor.GOLD.toString();
                    break;
                case UPDATE_HIGH:
                    prioLevel = 3;
                    prioLevelName = "MAJOR";
                    prioColor = ChatColor.RED.toString();
                    break;
                case DEV_BUILD:
                    consoleSender.sendMessage(prefix + ChatColor.GOLD + "Warning: You are running an experimental/development build! Proceed with caution.");
                    break;
                case NO_UPDATE:
                    consoleSender.sendMessage(prefix + ChatColor.RESET + "You are running the latest version.");
                    break;
                default:
                    break;
            }

            if (prioLevel > 0) {
                consoleSender.sendMessage( "\n" + prioColor +
                        "===============================================================================\n" +
                        "A " + prioLevelName + " update to " + pluginName + " is available!\n" +
                        "Download at https://www.spigotmc.org/resources/" + pluginName.toLowerCase() + "." + resourceId + "/\n" +
                        "(current: " + result.getCurrentVer() + ", latest: " + result.getLatestVer() + ")\n" +
                        "===============================================================================\n");
            }

        });
    }

}
