package me.kairo.staffplus.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {
    
    private final ItemStack item;
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
    }
    
    public ItemBuilder setName(String name) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> coloredLore = lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
            meta.setLore(coloredLore);
            item.setItemMeta(meta);
        }
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemStack build() {
        return item;
    }
}