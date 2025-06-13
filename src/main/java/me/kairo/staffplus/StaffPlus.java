package me.kairo.staffplus;


import me.kairo.staffplus.commands.*;
import me.kairo.staffplus.listeners.ChatListener;
import me.kairo.staffplus.listeners.FreezeListener;
import me.kairo.staffplus.listeners.PlayerListener;
import me.kairo.staffplus.listeners.StaffModeListener;
import me.kairo.staffplus.managers.FreezeManager;
import me.kairo.staffplus.managers.ReportManager;
import me.kairo.staffplus.managers.StaffManager;
import me.kairo.staffplus.managers.VanishManager;
import me.kairo.staffplus.utils.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffPlus extends JavaPlugin {

    private static StaffPlus instance;
    private ConfigManager configManager;
    private StaffManager staffManager;
    private FreezeManager freezeManager;
    private ReportManager reportManager;
    private VanishManager vanishManager;

    @Override
    public void onEnable() {
        instance = this;

        this.configManager = new ConfigManager(this);
        this.staffManager = new StaffManager(this);
        this.freezeManager = new FreezeManager(this);
        this.reportManager = new ReportManager(this);
        this.vanishManager = new VanishManager(this);

        configManager.loadConfig();
        registerCommands();
        registerListeners();

        getLogger().info(ChatColor.GREEN + "StaffPlus v" + getDescription().getVersion() + " activé avec succès !");
        checkDependencies();
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "StaffPlus désactivé.");
    }

    private void registerCommands() {
        getCommand("freeze").setExecutor(new FreezeCommand(this));
        getCommand("staff").setExecutor(new StaffCommand(this));
        getCommand("report").setExecutor(new ReportCommand(this));
        getCommand("reports").setExecutor(new ReportsCommand(this));
        getCommand("examine").setExecutor(new ExamineCommand(this));
        getCommand("randomtp").setExecutor(new RandomTpCommand(this));
        getCommand("staffchat").setExecutor(new StaffChatCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new StaffModeListener(this), this);
        getServer().getPluginManager().registerEvents(new FreezeListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void checkDependencies() {

    }

    public static StaffPlus getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }

    public StaffManager getStaffManager() {
        return staffManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }
}
