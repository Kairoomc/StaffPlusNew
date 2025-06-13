package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    
    public StaffChatCommand(StaffPlus plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String message = plugin.getConfigManager().getMessage("player-only");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("staffplus.staffchat")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /staffchat <message>");
            return true;
        }
        
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            messageBuilder.append(args[i]);
            if (i < args.length - 1) {
                messageBuilder.append(" ");
            }
        }
        String message = messageBuilder.toString();
        
        String format = plugin.getConfigManager().getString("staff-chat.format");
        String formattedMessage = ChatColor.translateAlternateColorCodes('&', 
            format.replace("{player}", player.getName()).replace("{message}", message));
        

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("staffplus.staffchat")) {
                online.sendMessage(formattedMessage);
            }
        }
        

        if (plugin.getConfigManager().getBoolean("staff-chat.log-to-console")) {
            plugin.getLogger().info("[STAFF CHAT] " + player.getName() + ": " + message);
        }
        
        return true;
    }
}