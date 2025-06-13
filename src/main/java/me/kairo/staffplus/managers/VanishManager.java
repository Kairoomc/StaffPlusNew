package me.kairo.staffplus.managers;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {
    
    private final StaffPlus plugin;
    private final Set<UUID> vanishedPlayers;
    
    public VanishManager(StaffPlus plugin) {
        this.plugin = plugin;
        this.vanishedPlayers = new HashSet<>();
    }
    
    public void setVanished(Player player, boolean vanished) {
        UUID uuid = player.getUniqueId();
        
        if (vanished) {
            if (vanishedPlayers.contains(uuid)) {
                return;
            }
            
            vanishedPlayers.add(uuid);
            hidePlayerFromAll(player);
            
            String message = plugin.getConfigManager().getMessage("vanish-enabled");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            if (!vanishedPlayers.contains(uuid)) {
                return;
            }
            
            vanishedPlayers.remove(uuid);
            showPlayerToAll(player);
            
            String message = plugin.getConfigManager().getMessage("vanish-disabled");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
    
    public void toggleVanish(Player player) {
        setVanished(player, !isVanished(player));
    }
    
    public boolean isVanished(Player player) {
        return vanishedPlayers.contains(player.getUniqueId());
    }
    
    private void hidePlayerFromAll(Player player) {
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (!online.hasPermission("staffplus.staff")) {
                online.hidePlayer(plugin, player);
            }
        }
    }
    
    private void showPlayerToAll(Player player) {
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            online.showPlayer(plugin, player);
        }
    }
    
    public void handlePlayerJoin(Player player) {
        if (!player.hasPermission("staffplus.staff")) {
            for (UUID vanishedUuid : vanishedPlayers) {
                Player vanishedPlayer = plugin.getServer().getPlayer(vanishedUuid);
                if (vanishedPlayer != null && vanishedPlayer.isOnline()) {
                    player.hidePlayer(plugin, vanishedPlayer);
                }
            }
        }
    }
    
    public Set<UUID> getVanishedPlayers() {
        return new HashSet<>(vanishedPlayers);
    }
}