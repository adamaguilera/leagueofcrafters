package som.spells.helper;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import som.spells.SpellName;


@Builder
public class SpellEffect {
    @NotNull @Getter
    private final SpellName name;
    private final long start;
    private final long duration;
    private final boolean isInfinite;

    public boolean isEffect (final SpellName effectName) {
        return this.name == effectName;
    }

    public boolean isOver () {
        return !isInfinite && System.currentTimeMillis() >= duration + start;
    }
}
