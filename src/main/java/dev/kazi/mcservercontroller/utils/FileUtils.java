package dev.kazi.mcservercontroller.utils;

import java.io.File;

public class FileUtils {

    public static void deleteDirectory(final File directory) {
        if (directory.exists()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    }
                    else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    
    public static int countFiles(final File directory) {
        int count = 0;
        if (directory.isDirectory()) {
            final File[] files = directory.listFiles();
            if (files != null) {
                for (final File file : files) {
                    count += countFiles(file);
                }
            }
        }
        else {
            ++count;
        }
        return count;
    }
}
