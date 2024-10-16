package dev.kazi.mcservercontroller.utils;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {

    private static final Random RANDOM;
    
    @NotNull
    public static String color(@NotNull final String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
    
    public static String getString(final Locale locale, final int length) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            builder.append(locale.getChar());
        }
        return builder.toString();
    }
    
    static {
        RANDOM = new Random();
    }
    
    public enum Locale {

        EN_NUM("1234567890qwertyuiopasdfghjklzxcvbnm"), 
        EN("qwertyuiopasdfghjklzxcvbnm"), 
        RU("ёйцукенгшщзхъфывапролджэячсмитьбю"), 
        NUM("1234567890");
        
        private final char[] table;
        
        private Locale(final String table) {
            this.table = (table.toLowerCase() + table.toUpperCase()).toCharArray();
        }
        
        public char getChar() {
            return this.table[StringUtils.RANDOM.nextInt(this.table.length)];
        }
    }
}
