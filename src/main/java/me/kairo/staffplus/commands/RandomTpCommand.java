package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomTpCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    private final Random random;
    
    public RandomTpCommand(StaffPlus plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            String message = plugin.getConfigManager().getMessage("player-only");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("staffplus.randomtp")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        List<Player> availablePlayers = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!online.equals(player) && !plugin.getVanishManager().isVanished(online)) {
                availablePlayers.add(online);
            }
        }
        
        if (availablePlayers.isEmpty()) {
            String message = plugin.getConfigManager().getMessage("no-players-online");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        Player target = availablePlayers.get(random.nextInt(availablePlayers.size()));
        player.teleport(target.getLocation());
        
        String message = plugin.getConfigManager().getMessage("teleport-success");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            message.replace("{player}", target.getName())));
        
        return true;
    }
}