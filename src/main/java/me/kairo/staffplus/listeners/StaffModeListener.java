package me.kairo.staffplus.listeners;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class StaffModeListener implements Listener {
    
    private final StaffPlus plugin;
    
    public StaffModeListener(StaffPlus plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getStaffManager().isInStaffMode(player)) {
            return;
        }
        
        if (!(event.getRightClicked() instanceof Player)) {
            return;
        }
        
        Player target = (Player) event.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item.getType() == Material.COMPASS) {
            plugin.getServer().dispatchCommand(player, "examine " + target.getName());
            event.setCancelled(true);
        } else if (item.getType() == Material.CHEST) {
            player.openInventory(target.getInventory());
            event.setCancelled(true);
        } else if (item.getType() == Material.PACKED_ICE) {
            plugin.getServer().dispatchCommand(player, "freeze " + target.getName());
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getStaffManager().isInStaffMode(player)) {
            return;
        }
        
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        
        if (item.getType() == Material.ENDER_PEARL) {
            // Random TP
            plugin.getServer().dispatchCommand(player, "randomtp");
            event.setCancelled(true);
        } else if (item.getType() == Material.GLASS) {
            // Toggle vanish
            plugin.getVanishManager().toggleVanish(player);
            event.setCancelled(true);
        } else if (item.getType() == Material.BARRIER) {
            // Menu de modération
            openModerationMenu(player);
            event.setCancelled(true);
        }
    }
    
    private void openModerationMenu(Player player) {
        // Ouvrir un menu de modération personnalisé
        player.sendMessage(ChatColor.YELLOW + "Menu de modération - Fonctionnalité à implémenter");
    }
}