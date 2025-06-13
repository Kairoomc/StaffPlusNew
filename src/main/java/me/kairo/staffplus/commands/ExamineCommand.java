package me.kairo.staffplus.commands;

import me.kairo.staffplus.StaffPlus;
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

import java.util.Arrays;

public class ExamineCommand implements CommandExecutor {
    
    private final StaffPlus plugin;
    
    public ExamineCommand(StaffPlus plugin) {
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
        
        if (!player.hasPermission("staffplus.examine")) {
            String message = plugin.getConfigManager().getMessage("no-permission");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /examine <joueur>");
            return true;
        }
        
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String message = plugin.getConfigManager().getMessage("player-not-found");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        
        openExamineMenu(player, target);
        
        return true;
    }
    
    private void openExamineMenu(Player staff, Player target) {
        Inventory inventory = Bukkit.createInventory(null, 27, 
            ChatColor.translateAlternateColorCodes('&', "&6&lExamen: &f" + target.getName()));
        
        ItemStack info = new ItemBuilder(Material.PLAYER_HEAD)
            .setName("&bInformations")
            .setLore(Arrays.asList(
                "&7Joueur: &f" + target.getName(),
                "&7Ping: &f" + target.getPing() + "ms",
                "&7Mode de jeu: &f" + target.getGameMode().name(),
                "&7Monde: &f" + target.getWorld().getName(),
                "&7Position: &f" + (int)target.getLocation().getX() + ", " + 
                    (int)target.getLocation().getY() + ", " + (int)target.getLocation().getZ(),
                "&7Vie: &f" + (int)target.getHealth() + "/" + (int)target.getMaxHealth(),
                "&7Faim: &f" + target.getFoodLevel() + "/20"
            ))
            .build();
        inventory.setItem(4, info);
        
        ItemStack inventoryItem = new ItemBuilder(Material.CHEST)
            .setName("&eInventaire")
            .setLore(Arrays.asList(
                "&7Cliquez pour voir",
                "&7l'inventaire du joueur"
            ))
            .build();
        inventory.setItem(10, inventoryItem);
        
        ItemStack enderchest = new ItemBuilder(Material.ENDER_CHEST)
            .setName("&dEnderchest")
            .setLore(Arrays.asList(
                "&7Cliquez pour voir",
                "&7l'enderchest du joueur"
            ))
            .build();
        inventory.setItem(12, enderchest);
        
        ItemStack freeze = new ItemBuilder(Material.PACKED_ICE)
            .setName("&cFreeze")
            .setLore(Arrays.asList(
                "&7Cliquez pour geler",
                "&7ce joueur"
            ))
            .build();
        inventory.setItem(14, freeze);
        

        ItemStack teleport = new ItemBuilder(Material.ENDER_PEARL)
            .setName("§aTéléportation")
            .setLore(Arrays.asList(
                "&7Cliquez pour vous",
                "&7téléporter au joueur"
            ))
            .build();
        inventory.setItem(16, teleport);
        
        staff.openInventory(inventory);
    }
}