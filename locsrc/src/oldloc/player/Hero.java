package oldloc.player;

import oldloc.player.managers.DamageManager;
import oldloc.player.managers.LobbyManager;
import oldloc.player.managers.SelectionManager;
import oldloc.player.managers.SpellManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public abstract class Hero {
    @Getter
    final User user;
    @Getter
    final DamageManager damageManager;
    @Getter
    final SpellManager spellManager;
    @Getter
    final LobbyManager lobbyManager;
    @Getter
    final SelectionManager selectionManager;

    public Hero (@NotNull final User user) {
        this.user = user;
        this.spellManager = new SpellManager(this.user);
        this.damageManager = new DamageManager(this.user);
        this.lobbyManager = new LobbyManager(this.user);
        this.selectionManager = new SelectionManager(this.user);
    }

    public static HeroName getName () { return HeroName.RECRUIT; }

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
