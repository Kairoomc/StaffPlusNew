package me.kairo.staffplus.listeners;

import me.kairo.staffplus.StaffPlus;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {
    
    private final StaffPlus plugin;
    private final Map<UUID, String> lastMessages;
    
    public ChatListener(StaffPlus plugin) {
        this.plugin = plugin;
        this.lastMessages = new HashMap<>();
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        
        lastMessages.put(player.getUniqueId(), message);
        
        if (plugin.getConfigManager().getBoolean("staff-chat.log-to-console")) {
            plugin.getLogger().info("[CHAT] " + player.getName() + ": " + message);
        }
    }
    
    public String getLastMessage(UUID playerUuid) {
        return lastMessages.getOrDefault(playerUuid, "Aucun message récent");
    }
}