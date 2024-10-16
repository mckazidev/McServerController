package dev.kazi.mcservercontroller.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.io.InputStream;

public class IOUtils {

    public static String toString(final InputStream inputStream, Charset charset) throws IOException {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        final StringBuilder sb = new StringBuilder();
        try (final Reader reader = new InputStreamReader(inputStream, charset)) {
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char)c);
            }
        }
        return sb.toString();
    }
}
