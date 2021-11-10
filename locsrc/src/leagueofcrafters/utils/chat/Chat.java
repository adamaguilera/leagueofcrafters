package leagueofcrafters.utils.chat;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class Chat {
    final UUID id;

    Optional<Player> getPlayer () { return Optional.ofNullable(Bukkit.getPlayer(id)); }
}
