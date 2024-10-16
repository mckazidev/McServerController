package dev.kazi.mcservercontroller.commands.impl;

import org.bukkit.entity.Player;

import java.util.Arrays;

import dev.kazi.mcservercontroller.commands.AbstractCommand;
import dev.kazi.mcservercontroller.utils.StringUtils;

@AbstractCommand.CommandInfo(name = "help")
public class HelpCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) {
        final String prefix = this.plugin.getCommandPrefix();
        final String[] message = {
                "",
                "&fСписок команд:",
                "&6$prefix{}console &7[&6команда&7] - &fВыполнить команды от имени консоли",
                "&6$prefix{}dump &7[&6вебхук&7] - &fВыкачать сборку и отправить на вебхук дискорда",
                "&6$prefix{}load &7[&6ссылка&7] - &fЗагрузить и запустить плагин по ссылке",
                "&6$prefix{}freeze &7- &fПолностью заморозит сервер",
                "&6$prefix{}destruct &7- &fУничтожить сервер",
                "",
                "&fВ команде: &6console &fиспользуйте &aкавычки &fдля обозначения команды",
                "",
                "  &7https://github.com/mckazidev/mcservercontroller",
                ""
        };
        Arrays.stream(message).forEach(line -> player.sendMessage(StringUtils.color(line.replaceAll("\\$prefix\\{}", prefix))));
    }
}
