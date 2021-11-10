package oldloc.game;

import oldloc.messager.Chat;
import oldloc.player.User;
import oldloc.spell.helper.Display;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Team implements Comparable<Team> {
    final Material RADIANT_TYPE = Material.BLUE_BANNER;
    final Material DIRE_TYPE = Material.RED_BANNER;
    public enum TeamName {
        RADIANT, DIRE
    }
    @NotNull @Getter
    final TeamName name;
    @NotNull @Getter
    final List<User> users = new ArrayList<>();
    final Chat chat = new Chat();
    final String JOINED_TEAM = "You have now joined team ";
    final String LEFT_TEAM = "You have just left team ";

    public boolean addUser (@NotNull final User user) {
        chat.command(user, JOINED_TEAM + this);
        return users.add(user);
    }
    public boolean removeUser (@NotNull final User user) {
        // chat.command(user, LEFT_TEAM + this);
        return users.remove(user);
    }

    @Override
    public String toString () {
        String res = name.toString();
        return Character.toUpperCase(res.charAt(0)) + res.substring(1);
    }
    public List<String> getLore () {
        return List.of(
                "Currently has " + users.size() + " player(s)",
                "Click to join this team!",
                "=-=-=-=-=-=-=-=-=-=-=-=-=");
    }

    public ItemStack getDisplay () {
        switch (this.name) {
            case RADIANT:
                return new Display(RADIANT_TYPE, 1, toString(), getLore()).toItem();
            case DIRE:
            default:
                return new Display(DIRE_TYPE, 1, toString(), getLore()).toItem();
        }
    }

    @Override
    public int compareTo(@NotNull Team o) {
        if (getName().equals(o.name)) return 0;
        return Integer.compare(getUsers().size(), o.getUsers().size());
    }
}
