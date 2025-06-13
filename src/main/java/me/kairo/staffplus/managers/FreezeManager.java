package me.kairo.staffplus.managers;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FreezeManager {
    
    private final StaffPlus plugin;
    private final Set<UUID> frozenPlayers;
    
    public FreezeManager(StaffPlus plugin) {
        this.plugin = plugin;
        this.frozenPlayers = new HashSet<>();
    }
    
    public void freezePlayer(Player target, Player staff) {
        UUID uuid = target.getUniqueId();
        
        if (isFrozen(target)) {
            String message = plugin.getConfigManager().getMessage("freeze-already");
            staff.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                message.replace("{player}", target.getName())));
            return;
        }
        
        frozenPlayers.add(uuid);
        

        String freezeMessage = plugin.getConfigManager().getString("freeze.freeze-message");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', freezeMessage));

        String successMessage = plugin.getConfigManager().getMessage("freeze-success");
        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            successMessage.replace("{player}", target.getName())));

        plugin.getLogger().info(staff.getName() + " a gelé " + target.getName());
    }
    
    public void unfreezePlayer(Player target, Player staff) {
        UUID uuid = target.getUniqueId();
        
        if (!isFrozen(target)) {
            return;
        }
        
        frozenPlayers.remove(uuid);
        

        String unfreezeMessage = plugin.getConfigManager().getString("freeze.unfreeze-message");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', unfreezeMessage));
        
        String successMessage = plugin.getConfigManager().getMessage("unfreeze-success");
        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            successMessage.replace("{player}", target.getName())));

        plugin.getLogger().info(staff.getName() + " a dégelé " + target.getName());
    }
    
    public boolean isFrozen(Player player) {
        return frozenPlayers.contains(player.getUniqueId());
    }
    
    public void unfreezeAll() {
        for (UUID uuid : new HashSet<>(frozenPlayers)) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                frozenPlayers.remove(uuid);
                String unfreezeMessage = plugin.getConfigManager().getString("freeze.unfreeze-message");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', unfreezeMessage));
            }
        }
        frozenPlayers.clear();
    }
    
    public Set<UUID> getFrozenPlayers() {
        return new HashSet<>(frozenPlayers);
    }
}