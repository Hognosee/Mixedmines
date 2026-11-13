package br.com.mixedmines.platform;

import org.bukkit.Bukkit;

/**
 * Detects the server implementation without linking against Paper or Purpur
 * classes. The plugin therefore remains loadable on plain Spigot.
 */
public final class ServerPlatform {

    public enum Kind {
        PAPER, PURPUR, SPIGOT, UNKNOWN
    }

    private ServerPlatform() {
    }

    public static Kind detect() {
        String name = Bukkit.getName();
        if (name == null) {
            return Kind.UNKNOWN;
        }

        String normalized = name.toLowerCase();
        if (normalized.contains("purpur")) {
            return Kind.PURPUR;
        }
        if (normalized.contains("paper")) {
            return Kind.PAPER;
        }
        if (normalized.contains("spigot")) {
            return Kind.SPIGOT;
        }
        return Kind.UNKNOWN;
    }
}
