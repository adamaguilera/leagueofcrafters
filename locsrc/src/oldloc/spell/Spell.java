package oldloc.spell;

import oldloc.messager.Chat;
import oldloc.player.Hero;
import oldloc.player.User;
import oldloc.spell.helper.Cooldown;
import oldloc.spell.helper.Display;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class Spell {
    protected Cooldown cooldown;
    protected Chat chat;

    public abstract SpellName getName ();
    public abstract User getUser ();
    public abstract int getCd();
    public abstract int getCost();
    public abstract Material getType();
    public abstract List<String> getLore();
    public abstract void cast ();

    public String getCdMessage () { return this + " is on cooldown for _ more seconds!"; }
    public boolean isBasic () { return false; }
    public boolean leftCasts () { return true; }
    public boolean rightCasts () { return true; }
    public boolean cancelEvent () { return true; }
    public List<Action> getActions () { return List.of(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK); }
    public boolean eventCasts (final PlayerInteractEvent event) {
        return event.getPlayer().getUniqueId() == getUser().getId() &&
                getActions().contains(event.getAction()) &&
                ((leftCasts() && typeMatches(event.getPlayer().getInventory().getItemInOffHand().getType())) ||
                        (rightCasts() && typeMatches(event.getPlayer().getInventory().getItemInMainHand().getType())));
    }

    public Cooldown getCooldown () {
        if (this.cooldown == null) {
            this.cooldown = Cooldown.builder()
                    .user(getUser())
                    .cdMillis(getCd())
                    .cdMessage(getCdMessage())
                    .build();
        }
        return this.cooldown;
    }
    public Chat getChat () {
        if (this.chat == null) {
            this.chat = new Chat();
        }
        return this.chat;
    }
    public void message (final String message) {
        getChat().ability(getUser(), message);
    }

    public boolean typeMatches (final Material material) {
        return material != null && getDisplay().type().equals(material);
    }
    public boolean metaMatches (final ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            return typeMatches(item.getType()) && nameMatches(meta.getDisplayName()) &&
                    loreMatches(meta.getLore());
        }
        return false;
    }
    public boolean nameMatches (@NotNull final String name) {
        return ChatColor.stripColor(getDisplay().name()).equals(ChatColor.stripColor(name));
    }
    public boolean loreMatches (@NotNull final List<String> lore) {
        for (int index = 0; index <= getDisplay().lore().size() &&
                index <= lore.size(); index ++) {
            if (!ChatColor.stripColor(getDisplay().lore().get(index)).equals(ChatColor.stripColor(lore.get(index)))) {
                return false;
            }
        }
        return getDisplay().lore().size() == lore.size();
    }

    public Optional<Player> getPlayer () { return getUser().getPlayer(); }
    public Hero getHero () { return getUser().getHero(); }

    public void giveDisplay () {
        getPlayer().ifPresent(player -> {
            Map<Integer, ItemStack> remaining = player.getInventory().addItem(getDisplay().toItem());
            remaining.forEach((index, item) -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        });
    }
    public int getAmount () { return 1; }
    public Display getDisplay () {
        return new Display(getType(), getAmount(), toString(), getLore());
    }

    public void consumeCost () { getHero().getDamageManager().consume(getCost()); }
    public boolean hasFood () { return getPlayer().isPresent() && getPlayer().get().getFoodLevel() >= getCost(); }

    public boolean onCooldown () { return getCooldown().onCooldown(); }
    public void putOnCooldown () { getCooldown().putOnCooldown(); }

    public boolean hasAbility () { return getHero().getSpellManager().hasSpell(getName()); }
    public boolean canUseAbility () { return (hasAbility() && hasFood() && !onCooldown()); }

    public boolean safeCast () {
        if (canUseAbility()) {
            putOnCooldown();
            consumeCost();
            cast();
            return true;
        }
        return false;
    }

    @Override
    public String toString () {
        StringBuilder name = new StringBuilder();
        for (String word : getName().toString().split("_")) {
            if (name.length() > 0) {
                name.append(" ");
            }
            if (word.length() > 0) {
                name.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
                name.append(word.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return name.toString();
    }
}
