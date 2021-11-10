package oldloc;


import lombok.Getter;
import oldloc.game.Game;
import oldloc.game.GameListener;
import oldloc.messager.Log;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Main extends JavaPlugin {
    static Main instance;
    final String ENABLED_PLUGIN = "League of Crafters has been enabled!";
    final String DISABLED_PLUGIN = "League of Crafters has been disabled!";

    @Getter
    private Game game;
    private GameListener gameListener;

    public static Main GET () {
        return instance;
    }
    public static Game GET_GAME () { return instance.getGame(); }
    public static Game.GameState GET_GAME_STATE () { return instance.getGame().getState(); }
    public static World GET_WORLD () { return Bukkit.getWorld("world"); }


    @Override
    public void onEnable () {
        Main.instance = this;
        this.game = new Game();
        this.gameListener = new GameListener(this.game);
        this.getServer().getPluginManager().registerEvents(gameListener, this);
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
