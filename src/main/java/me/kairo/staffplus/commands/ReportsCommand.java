package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
import me.kairo.staffplus.models.Report;
import me.kairo.staffplus.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ReportsCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    
    public ReportsCommand(StaffPlus plugin) {
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
        
        if (!player.hasPermission("staffplus.reports")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        openReportsMenu(player);
        
        return true;
    }
    
    private void openReportsMenu(Player player) {
        List<Report> reports = plugin.getReportManager().getRecentReports(45);
        
        if (reports.isEmpty()) {
            String message = plugin.getConfigManager().getMessage("no-reports");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }
        
        Inventory inventory = Bukkit.createInventory(null, 54, 
            ChatColor.translateAlternateColorCodes('&', "&c&lSignalements"));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (int i = 0; i < Math.min(reports.size(), 45); i++) {
            Report report = reports.get(i);
            
            ItemStack item = new ItemBuilder(Material.PAPER)
                .setName("&c📋 Signalement #" + (i + 1))
                .setLore(Arrays.asList(
                    "&7Signalé par: &f" + report.getReporter(),
                    "&7Joueur signalé: &f" + report.getTarget(),
                    "&7Raison: &e" + report.getReason(),
                    "&7Date: &f" + dateFormat.format(new Date(report.getTimestamp())),
                    "",
                    "&aCliquez pour plus d'actions"
                ))
                .build();
            
            inventory.setItem(i, item);
        }
        
        player.openInventory(inventory);
    }
}