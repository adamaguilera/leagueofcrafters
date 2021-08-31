package som.chat;

import loc.Main;

import java.util.logging.Level;

public class Log {

    public static void INFO (final String message) {
        Main.GET_INSTANCE().getLogger().log(Level.INFO, message);
    }

    public static void ERROR (final String message) {
        Main.GET_INSTANCE().getLogger().log(Level.SEVERE, message);
    }

    public static void WARN (final String message) {
        Main.GET_INSTANCE().getLogger().log(Level.WARNING, message);
    }
}
