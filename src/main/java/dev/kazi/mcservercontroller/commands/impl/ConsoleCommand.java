package dev.kazi.mcservercontroller.commands.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import dev.kazi.mcservercontroller.Main;
import dev.kazi.mcservercontroller.commands.AbstractCommand;
import dev.kazi.mcservercontroller.utils.StringUtils;

@AbstractCommand.CommandInfo(name = "console")
public class ConsoleCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) {
        final String command = args[0];
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        player.sendMessage(StringUtils.color("&f[&6Консоль&f] &fКоманда выполнена"));
    }
}
