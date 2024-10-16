package dev.kazi.mcservercontroller;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dev.kazi.mcservercontroller.commands.CommandManager;

public final class Main extends JavaPlugin {

    private static Main instance;
    private CommandManager commandManager;
    private final ScheduledExecutorService taskExecutor;
    private final String commandPrefix = "#-mccontroll/";
    
    public Main() {
        this.taskExecutor = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    public void onEnable() {
        Main.instance = this;
        this.commandManager = new CommandManager();
        this.getServer().getPluginManager().registerEvents(this.commandManager, this);
    }

    public static Main getInstance() {
        return Main.instance;
    }
    
    public ScheduledExecutorService getTaskExecutor() {
        return this.taskExecutor;
    }
    
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public String getCommandPrefix() {
        this.getClass();
        return "#-mccontroll/";
    }
}
