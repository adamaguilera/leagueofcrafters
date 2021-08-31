package som.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import som.chat.Chat;
import som.spells.SpellName;
import som.spells.basic.Crate;
import som.spells.basic.Eat;
import som.spells.helper.SpellEffect;
import som.team.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class Mage {
    @Getter
    final Team team;
    @Getter
    final UUID playerID;
    @Getter
    final SpellManager spellManager = new SpellManager(this);
    @Getter
    final SelectionManager selectionManager = new SelectionManager(this);
    @Getter
    final DamageManager damageManager = new DamageManager(this);
    @Setter
    final List<SpellEffect> spellEffects = new ArrayList<>();
    final Chat chat = new Chat();
    @Getter
    int kills = 0;

    public Optional<Player> getPlayer () {
        return Optional.ofNullable(Bukkit.getPlayer(this.playerID));
    }


    public void clearInventory () {
        getPlayer().ifPresent(player -> player.getInventory().clear());
    }
    public void giveStartingItems () {
        getPlayer().ifPresent(player -> spellManager.getSpells().forEach(spell -> {
            if (spell instanceof Crate) {
                for(int i = 0; i < Crate.getSTARTING_AMOUNT(); i ++) {
                    spell.givePlayerDisplay();
                }
            } else if (spell instanceof Eat) {
                for(int i = 0; i < Eat.getSTARTING_AMOUNT(); i ++) {
                    spell.givePlayerDisplay();
                }
            } else {
                spell.givePlayerDisplay();
            }
        }));
    }
    public void dropScroll () {

    }

    public void dropCrate () {
        getPlayer().ifPresent(player -> {
            getSpellManager().getSpell(SpellName.CRATE).ifPresent(spell -> {
                 Map<Integer, ItemStack> remaining = player.getInventory().addItem(spell.getDisplay());
                 if (player.getLocation().getWorld() != null) {
                     remaining.forEach((index, item) ->
                             player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item));
                 }
            });
        });

    }
    public boolean isDamaged () {
        return getPlayer().isPresent() &&
                getPlayer().get().getHealth() !=
                getPlayer().get().getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue();
    }
    public boolean isHungry () {
        return getPlayer().isPresent() &&
                getPlayer().get().getFoodLevel() != 20;
    }

    public void restoreHealth (final int amount) {
        getPlayer().ifPresent(player -> player.setHealth(Math.min(
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue(),
                player.getHealth() + amount)));
    }
    public void consumeMana (final int amount) {
        getPlayer().ifPresent(player -> player.setFoodLevel(Math.max(0,
                player.getFoodLevel() - amount)));
    }
    public void restoreMana (final int amount) {
        getPlayer().ifPresent(player -> player.setFoodLevel(Math.min(20,
                player.getFoodLevel() + amount)));
    }
    public void restoreSaturation (final int amount) {
        getPlayer().ifPresent(player -> player.setSaturation(player.getSaturation() + amount));
    }


    public void onKill (final Mage victim) {
        incKills();
        if (victim.getTier() == getTier()) {
            dropCrate();
        } else if (victim.getTier() >= getTier()) {
            dropScroll();
            dropCrate();
        }
    }
    public void onDeath (final Mage killer) {
        if (killer.getTier() > getTier()) {
            dropScroll();
            dropCrate();
        } else if (killer.getTier() == getTier()) {
            dropCrate();;
        }
    }

    public int getTier () {
        return getTeam().getTier();
    }
    public int incKills () { return ++this.kills; }
    public void sendAbilityMessage (@NotNull final String message) {
        this.chat.abilityMessage(getPlayerID(), message);
    }
    public boolean sameTeam (@NotNull final Mage mage) {
        return this.team.onTeam(mage);
    }
    public boolean equals (@NotNull final UUID playerID) {
        return this.playerID.equals(playerID);
    }
    public boolean equals (final Mage mage) {
        return this.playerID.equals(mage.getPlayerID());
    }

}
