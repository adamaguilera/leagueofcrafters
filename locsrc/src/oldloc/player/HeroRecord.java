package oldloc.player;

import org.bukkit.Material;

import java.util.List;

public record HeroRecord(HeroName hero, Material type, List<String> lore) {
}
