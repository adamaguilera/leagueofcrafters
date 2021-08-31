package som.spells.basic.data;

import org.bukkit.Material;
import som.player.Mage;

public record RarityData<T> (T data, double rarity) {
}
