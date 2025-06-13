package me.kairo.staffplus.listeners;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    
    private final StaffPlus plugin;
    
    public PlayerListener(StaffPlus plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getVanishManager().handlePlayerJoin(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerJoinEvent event) {
        if (plugin.getStaffManager().isInStaffMode(event.getPlayer())) {
            plugin.getStaffManager().disableStaffMode(event.getPlayer());
        }
    }
}