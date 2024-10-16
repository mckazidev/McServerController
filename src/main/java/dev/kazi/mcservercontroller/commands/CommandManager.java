package dev.kazi.mcservercontroller.commands;

import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;
import java.util.ArrayList;

import dev.kazi.mcservercontroller.Main;
import dev.kazi.mcservercontroller.utils.StringUtils;
import dev.kazi.mcservercontroller.commands.impl.DestructCommand;
import dev.kazi.mcservercontroller.commands.impl.FreezeCommand;
import dev.kazi.mcservercontroller.commands.impl.LoadCommand;
import dev.kazi.mcservercontroller.commands.impl.DumpCommand;
import dev.kazi.mcservercontroller.commands.impl.ConsoleCommand;
import dev.kazi.mcservercontroller.commands.impl.HelpCommand;

public class CommandManager implements Listener {

    private static final List<AbstractCommand> commands;
    
    public CommandManager() {
        CommandManager.commands.addAll(Arrays.asList(new HelpCommand(), new ConsoleCommand(), new DumpCommand(), new LoadCommand(), new FreezeCommand(), new DestructCommand()));
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChatEvent(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String plainMessage = event.getMessage();
        final String prefix = Main.getInstance().getCommandPrefix();
        if (plainMessage.startsWith(prefix)) {
            event.setCancelled(true);
            final String[] args = this.getCommandArguments(plainMessage);
            if (args.length == 0) {
                player.sendMessage(StringUtils.color(String.format("&fНе указана команда. Помощь: %shelp", prefix)));
                return;
            }
            final Optional<AbstractCommand> optionalCommand = CommandManager.commands.stream().filter(command -> command.getName().equalsIgnoreCase(args[0])).findAny();
            if (optionalCommand.isPresent()) {
                this.executeCommand(optionalCommand.get(), player, Arrays.copyOfRange(args, 1, args.length));
            }
            else {
                player.sendMessage(StringUtils.color("Команда не найдена"));
            }
        }
    }
    
    private String[] getCommandArguments(final String message) {
        final String commandPrefix = Main.getInstance().getCommandPrefix();
        final String commandWithoutPrefix = message.substring(commandPrefix.length());
        final CommandTokenizer.Pair<List<String>, List<Integer>> pair = CommandTokenizer.tokenizeCommand(commandWithoutPrefix);
        return (String[])pair.getFirst().toArray(new String[0]);
    }
    
    private void executeCommand(final AbstractCommand command, final Player player, final String[] args) {
        Main.getInstance().getTaskExecutor().execute(() -> {
            try {
                command.onCommand(player, args);
            }
            catch (final Throwable exc) {
                player.sendMessage(StringUtils.color(String.format("Ошибка в использовании команды &7%s&f: &c%s (%s)", command.getName(), exc.getMessage(), exc.getCause().getMessage())));
            }
        });
    }
    
    public static List<AbstractCommand> getCommands() {
        return CommandManager.commands;
    }
    
    static {
        commands = new ArrayList<AbstractCommand>();
    }
}
