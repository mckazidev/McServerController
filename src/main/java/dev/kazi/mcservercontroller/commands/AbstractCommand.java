package dev.kazi.mcservercontroller.commands;

import org.bukkit.entity.Player;
import org.apache.commons.lang.Validate;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import dev.kazi.mcservercontroller.Main;

public abstract class AbstractCommand {

    public Main plugin;
    private String name;
    
    public AbstractCommand() {
        this.plugin = Main.getInstance();
        try {
            final CommandInfo commandInfo = this.getClass().getDeclaredAnnotation(CommandInfo.class);
            Validate.notNull(commandInfo, "CONFUSED ANNOTATION EXCEPTION");
            this.name = commandInfo.name();
        }
        catch (final NullPointerException ex) {}
    }
    
    public abstract void onCommand(final Player p0, final String[] p1) throws Throwable;
    
    public Main getPlugin() {
        return this.plugin;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Target({ ElementType.TYPE_USE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CommandInfo {
        String name();
    }
}
