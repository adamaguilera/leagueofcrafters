package som.spells.basic.data;

import org.bukkit.Material;
import som.player.Mage;

public record FoodData(Material material, int health, int mana, int saturation) {
    public void applyTo (final Mage mage) {
        mage.restoreHealth(health);
        mage.restoreMana(mana);
        mage.restoreSaturation(saturation);
    }
}
