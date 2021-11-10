package leagueofcrafters;

import leagueofcrafters.game.Game;
import leagueofcrafters.game.GameListener;
import leagueofcrafters.team.Team;
import leagueofcrafters.team.TeamManager;
import leagueofcrafters.team.TeamName;
import leagueofcrafters.utils.chat.Log;
import leagueofcrafters.user.User;
import leagueofcrafters.utils.config.ConfigLoader;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class Main extends JavaPlugin {
    static Main instance;
    final String ENABLED_PLUGIN = "League of Crafters has been enabled!!";
    final String DISABLED_PLUGIN = "League of Crafters has been disabled!!";


    public static Main GET () {
        return instance;
    }
    public static World GET_WORLD () { return Bukkit.getWorld("world"); }
    public static Optional<Team> GET_TEAM (@NotNull final TeamName teamName) { return instance.game.getTeamManager().getTeam(teamName);}

    GameListener gameListener;
    Game game;
    ConfigLoader config;

    @Override
    public void onEnable () {
        instance = this;
        config = new ConfigLoader();
        config.loadAll();
        this.game = new Game(config.getGameConfig());
        this.gameListener = new GameListener(game);
        this.getServer().getPluginManager().registerEvents(gameListener, this);
        Log.INFO(ENABLED_PLUGIN);
    }

    @Override
    public void onDisable () {
        this.game.onDisable();
        Log.INFO(DISABLED_PLUGIN);
    }


    @Override
    public boolean onCommand (@NotNull CommandSender sender,
                              @NotNull Command command,
                              @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("loc") &&
                this.game.getGameState() == Game.GameState.LOBBY &&
                args.length == 1 &&
                args[0].equalsIgnoreCase("ready") &&
                sender instanceof Player) {
            UUID playerID = ((Player) sender).getUniqueId();
            User user = this.game.getUserManager().getUser(playerID);
            this.game.ready(user.ready());
        }
        return true;
    }


}
