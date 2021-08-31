package loc;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import som.Game;
import som.GameListener;
import som.chat.Log;
import som.player.Mage;

import java.util.UUID;

public class Main extends JavaPlugin {
    static Main instance;

    final String ENABLED_PLUGIN = "School of Mages has been enabled!";
    final String DISABLED_PLUGIN = "School of Mages has been disabled!";
    final String TEAM_COMMAND = "team";
    @Getter
    private Game game;
    private GameListener gameListener;

    public static Main GET_INSTANCE () {
        return instance;
    }

    public static Mage GET_MAGE (@NotNull final UUID playerID) {
        return Main.GET_INSTANCE().getGame().getOrCreateMage(playerID);
    }

    @Override
    public void onEnable () {
        instance = this;
        /*
        this.game = new Game();
        this.gameListener = new GameListener(game);
        this.getServer().getPluginManager().registerEvents(gameListener, this);
         */
        Log.INFO(ENABLED_PLUGIN);
    }

    @Override
    public void onDisable () {
        Log.INFO(DISABLED_PLUGIN);
    }


    @Override
    public boolean onCommand (@NotNull CommandSender sender,
                              @NotNull Command command,
                              @NotNull String label, String[] args) {
        return true;
    }


}
