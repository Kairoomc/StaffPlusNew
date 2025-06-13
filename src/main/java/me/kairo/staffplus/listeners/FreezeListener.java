package me.kairo.staffplus.listeners;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FreezeListener implements Listener {
    
    private final StaffPlus plugin;
    
    public FreezeListener(StaffPlus plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getFreezeManager().isFrozen(player)) {
            return;
        }
        
        if (plugin.getConfigManager().getBoolean("freeze.prevent-movement")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getFreezeManager().isFrozen(player)) {
            return;
        }
        
        if (plugin.getConfigManager().getBoolean("freeze.prevent-chat")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas parler pendant que vous êtes gelé !");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getFreezeManager().isFrozen(player)) {
            return;
        }
        
        if (plugin.getConfigManager().getBoolean("freeze.prevent-commands")) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser de commandes pendant que vous êtes gelé !");
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        if (!plugin.getFreezeManager().isFrozen(player)) {
            return;
        }
        
        if (plugin.getConfigManager().getBoolean("freeze.prevent-quit")) {
            if (plugin.getConfigManager().getBoolean("freeze.kick-on-quit-attempt")) {
                String kickMessage = plugin.getConfigManager().getString("freeze.quit-kick-message");
                player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
            }
            
            // Log de la tentative de déconnexion
            plugin.getLogger().warning(player.getName() + " a tenté de se déconnecter pendant un freeze !");
        }
    }
}