package leagueofcrafters.creeps;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@RequiredArgsConstructor
public class Creep {
    final EntityType type;
    final ItemStack[] armor;
    final ItemStack weapon;

    UUID target;
    UUID goal;
}
