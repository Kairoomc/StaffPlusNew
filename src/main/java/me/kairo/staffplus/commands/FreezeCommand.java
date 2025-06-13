package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    
    public FreezeCommand(StaffPlus plugin) {
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
        
        if (!player.hasPermission("staffplus.freeze")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /freeze <joueur>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String message = plugin.getConfigManager().getMessage("player-not-found");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        if (plugin.getFreezeManager().isFrozen(target)) {
            plugin.getFreezeManager().unfreezePlayer(target, player);
        } else {
            plugin.getFreezeManager().freezePlayer(target, player);
        }
        
        return true;
    }
}