package me.kairo.staffplus.managers;

import me.kairo.staffplus.StaffPlus;
import me.kairo.staffplus.models.Report;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {
    
    private final StaffPlus plugin;
    private final Map<UUID, Long> reportCooldowns;
    private final List<Report> reports;
    
    public ReportManager(StaffPlus plugin) {
        this.plugin = plugin;
        this.reportCooldowns = new ConcurrentHashMap<>();
        this.reports = new ArrayList<>();
    }
    
    public boolean createReport(Player reporter, String targetName, String reason) {
        if (isOnCooldown(reporter)) {
            String message = plugin.getConfigManager().getMessage("report-cooldown");
            reporter.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return false;
        }
        
        Report report = new Report(
            UUID.randomUUID(),
            reporter.getName(),
            targetName,
            reason,
            System.currentTimeMillis()
        );
        
        reports.add(report);
        
        int cooldown = plugin.getConfigManager().getInt("reports.cooldown-seconds");
        reportCooldowns.put(reporter.getUniqueId(), System.currentTimeMillis() + (cooldown * 1000L));
        
        if (plugin.getConfigManager().getBoolean("reports.notify-staff-on-report")) {
            notifyStaff(report);
        }
        
        String message = plugin.getConfigManager().getMessage("report-success");
        reporter.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        
        plugin.getLogger().info("Nouveau signalement: " + reporter.getName() +
            " a signalé " + targetName + " pour: " + reason);
        
        return true;
    }
    
    private void notifyStaff(Report report) {
        String notification = ChatColor.translateAlternateColorCodes('&',
            "&c[SIGNALEMENT] &f" + report.getReporter() + " &7a signalé &f" + 
            report.getTarget() + " &7pour: &e" + report.getReason());
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("staffplus.notify")) {
                player.sendMessage(notification);
                
                if (plugin.getConfigManager().getBoolean("reports.sound-notification")) {
                    String soundName = plugin.getConfigManager().getString("reports.notification-sound");
                    try {
                        Sound sound = Sound.valueOf(soundName);
                        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                    } catch (IllegalArgumentException e) {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
                    }
                }
            }
        }
    }
    
    private boolean isOnCooldown(Player player) {
        Long cooldownEnd = reportCooldowns.get(player.getUniqueId());
        if (cooldownEnd == null) {
            return false;
        }
        
        if (System.currentTimeMillis() >= cooldownEnd) {
            reportCooldowns.remove(player.getUniqueId());
            return false;
        }
        
        return true;
    }
    
    public List<Report> getReports() {
        return new ArrayList<>(reports);
    }
    
    public List<Report> getRecentReports(int limit) {
        return reports.stream()
            .sorted((r1, r2) -> Long.compare(r2.getTimestamp(), r1.getTimestamp()))
            .limit(limit)
            .toList();
    }
    
    public void removeReport(UUID reportId) {
        reports.removeIf(report -> report.getId().equals(reportId));
    }
    
    public void clearOldReports() {
        int maxDays = plugin.getConfigManager().getInt("reports.auto-delete-days");
        long cutoffTime = System.currentTimeMillis() - (maxDays * 24L * 60L * 60L * 1000L);
        
        reports.removeIf(report -> report.getTimestamp() < cutoffTime);
    }
}