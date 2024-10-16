package dev.kazi.mcservercontroller.commands.impl;

import dev.kazi.mcservercontroller.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.net.URL;
import java.io.File;

import dev.kazi.mcservercontroller.commands.AbstractCommand;

@AbstractCommand.CommandInfo(name = "load")
public class LoadCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) throws Throwable {
        final String pluginUrl = args[0];
        final File pluginsFolder = new File("plugins");
        final File pluginFile = this.downloadPlugin(pluginUrl, pluginsFolder);
        if (pluginFile != null) {
            final Plugin loadedPlugin = Bukkit.getServer().getPluginManager().loadPlugin(pluginFile);
            if (loadedPlugin != null) {
                loadedPlugin.onLoad();
                Bukkit.getServer().getPluginManager().enablePlugin(loadedPlugin);
                player.sendMessage(StringUtils.color("&f[&6Плагин менеджер&f] &fПлагин успешно загружен."));
            }
        }
    }
    
    private File downloadPlugin(final String pluginUrl, final File pluginsFolder) throws IOException {
        final URL url = new URL(pluginUrl);
        final ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        final String pluginFileName = pluginUrl.substring(pluginUrl.lastIndexOf(47) + 1);
        final File pluginFile = new File(pluginsFolder, pluginFileName);
        try (final FileOutputStream fos = new FileOutputStream(pluginFile)) {
            fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            return pluginFile;
        }
    }
}
