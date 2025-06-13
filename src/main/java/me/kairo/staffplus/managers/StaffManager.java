package me.kairo.staffplus.managers;

import me.kairo.staffplus.StaffPlus;
import me.kairo.staffplus.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class StaffManager {
    
    private final StaffPlus plugin;
    private final Set<UUID> staffMode;
    private final Map<UUID, ItemStack[]> savedInventories;
    private final Map<UUID, GameMode> savedGameModes;
    
    public StaffManager(StaffPlus plugin) {
        this.plugin = plugin;
        this.staffMode = new HashSet<>();
        this.savedInventories = new HashMap<>();
        this.savedGameModes = new HashMap<>();
    }
    
    public void enableStaffMode(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (isInStaffMode(player)) {
            return;
        }
        
        savedInventories.put(uuid, player.getInventory().getContents().clone());
        savedGameModes.put(uuid, player.getGameMode());
        
        player.getInventory().clear();
        
        giveStaffItems(player);
        
        if (plugin.getConfigManager().getBoolean("general.staff-mode-fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        
        if (plugin.getConfigManager().getBoolean("general.vanish-on-staff-join")) {
            plugin.getVanishManager().setVanished(player, true);
        }
        
        player.setGameMode(GameMode.CREATIVE);
        
        staffMode.add(uuid);
        

        String message = plugin.getConfigManager().getMessage("staff-mode-enabled");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    public void disableStaffMode(Player player) {
        UUID uuid = player.getUniqueId();
        
        if (!isInStaffMode(player)) {
            return;
        }
        
        if (savedInventories.containsKey(uuid)) {
            player.getInventory().setContents(savedInventories.get(uuid));
            savedInventories.remove(uuid);
        }
        
        if (savedGameModes.containsKey(uuid)) {
            player.setGameMode(savedGameModes.get(uuid));
            savedGameModes.remove(uuid);
        }
        
        player.setAllowFlight(false);
        player.setFlying(false);
        
        plugin.getVanishManager().setVanished(player, false);
        
        staffMode.remove(uuid);
        

        String message = plugin.getConfigManager().getMessage("staff-mode-disabled");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    private void giveStaffItems(Player player) {
        ItemStack examiner = new ItemBuilder(Material.COMPASS)
                .setName(plugin.getConfigManager().getString("staff-mode-items.examiner.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.examiner.lore"))
                .build();
        player.getInventory().setItem(0, examiner);
        

        ItemStack inventory = new ItemBuilder(Material.CHEST)
                .setName(plugin.getConfigManager().getString("staff-mode-items.inventory.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.inventory.lore"))
                .build();
        player.getInventory().setItem(1, inventory);
        

        ItemStack freeze = new ItemBuilder(Material.PACKED_ICE)
                .setName(plugin.getConfigManager().getString("staff-mode-items.freeze.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.freeze.lore"))
                .build();
        player.getInventory().setItem(2, freeze);
        
        ItemStack randomTp = new ItemBuilder(Material.ENDER_PEARL)
                .setName(plugin.getConfigManager().getString("staff-mode-items.randomtp.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.randomtp.lore"))
                .build();
        player.getInventory().setItem(3, randomTp);
        

        ItemStack vanish = new ItemBuilder(Material.GLASS)
                .setName(plugin.getConfigManager().getString("staff-mode-items.vanish.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.vanish.lore"))
                .build();
        player.getInventory().setItem(4, vanish);
        

        ItemStack moderation = new ItemBuilder(Material.BARRIER)
                .setName(plugin.getConfigManager().getString("staff-mode-items.moderation.name"))
                .setLore(plugin.getConfigManager().getStringList("staff-mode-items.moderation.lore"))
                .build();
        player.getInventory().setItem(8, moderation);
    }
    
    public boolean isInStaffMode(Player player) {
        return staffMode.contains(player.getUniqueId());
    }
    
    public void disableAllStaffMode() {
        for (UUID uuid : new HashSet<>(staffMode)) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                disableStaffMode(player);
            }
        }
    }
    
    public Set<UUID> getStaffModeUsers() {
        return new HashSet<>(staffMode);
    }
}