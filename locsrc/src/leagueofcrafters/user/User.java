package leagueofcrafters.user;

import leagueofcrafters.Main;
import leagueofcrafters.utils.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class User {
    final String READY = "You are now ready!";
    final String UNREADY = "You are no longer ready!";
    @Getter
    final UUID id;
    @Getter
    boolean isReady;
//    Hero hero;

    public Optional<Player> getPlayer () { return Optional.ofNullable(Bukkit.getPlayer(id)); }

    public boolean ready () {
        this.isReady = !isReady;
        this.command(isReady ? READY : UNREADY);
        return isReady;
    }

    public boolean isReadyAndValid () { return isReady; }

    public void gameDefault () {
        getPlayer().ifPresent(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealth(20.0);
            player.setFoodLevel(20);
            player.setSaturation(15.0f);
        });
    }

    public void spectatorDefault () {
        getPlayer().ifPresent(
            player -> {
              player.setGameMode(GameMode.SPECTATOR);
              player.teleport(
                  new Location(Main.GET_WORLD(), 0, 80, 0),
                  PlayerTeleportEvent.TeleportCause.SPECTATE);
        });
    }

    public void command (final String message) {
        send(ChatColor.RED + message + ChatColor.RESET);
    }

    public void ability (final String message) {
        send(ChatColor.RED + message + ChatColor.RESET);
    }

    public void log (final String message) {
        send(message);
    }

    void send (final String message) {
        getPlayer().ifPresent(player -> player.sendMessage(message));
    }

    @Override
    public String toString () {
        return getPlayer().isPresent() ? getPlayer().get().getDisplayName() : "Anonymous Player";
    }

    @Override
    public int hashCode () {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // same object reference
        if(this == obj)
            return true;
        if(obj == null || obj.getClass()!= this.getClass())
            return false;
        User user = (User) obj;
        return (user.id.equals(this.id));
    }
}
