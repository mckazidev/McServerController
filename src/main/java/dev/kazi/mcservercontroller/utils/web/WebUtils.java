package dev.kazi.mcservercontroller.utils.web;

import java.net.URL;
import java.io.File;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;

import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;

import dev.kazi.mcservercontroller.utils.IOUtils;

public class WebUtils {

    public static String uploadFile(final File targetFile, final boolean deleteFile) throws Exception {
        final URL url = new URL("https://drop.xtrafrancyz.net/upload/");
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        try (final OutputStream outputStream = connection.getOutputStream()) {
            Files.copy(Paths.get(targetFile.getAbsolutePath(), new String[0]), outputStream);
            outputStream.flush();
            outputStream.close();
        }
        if (connection.getResponseCode() == 200) {
            if (deleteFile) {
                targetFile.delete();
            }
            try (final InputStream inputStream = connection.getInputStream()) {
                final String uploadedUrl = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                inputStream.close();
                connection.disconnect();
                return uploadedUrl;
            }
        }
        connection.disconnect();
        return null;
    }
    
    public static void sendWebhook(final String webhookUrl, @Nullable final String content, final List<JSONObject> embedList) throws Exception {
        if (content == null && embedList == null) {
            return;
        }
        final URL url = new URL(webhookUrl);
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("User-Agent", "McServerController");
        final JSONObject json = new JSONObject();
        if (content != null) {
            json.put("content", content);
        }
        if (embedList != null) {
            json.put("embeds", embedList);
        }
        try (final OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(json.toString().getBytes(StandardCharsets.UTF_8));
        }
        connection.getInputStream().close();
        connection.disconnect();
    }
}
