package som.spells.basic.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record CrateData (Material material, @NotNull List<Integer> tierAmount) {

    private int getAmount (final int tier) {
        if (tier - 1 >= 0 && tier - 1 < tierAmount.size()) {
            return tierAmount.get(tier - 1);
        }
        return 0;
    }

    public ItemStack toItemStack (final int tier) {
        return new ItemStack(this.material, getAmount(tier));
    }

    public boolean hasTier (final int tier) {
        return getAmount(tier) > 0;
    }
}