package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    
    public StaffCommand(StaffPlus plugin) {
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
        
        if (!player.hasPermission("staffplus.staff")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        if (plugin.getStaffManager().isInStaffMode(player)) {
            plugin.getStaffManager().disableStaffMode(player);
        } else {
            plugin.getStaffManager().enableStaffMode(player);
        }
        
        return true;
    }
}