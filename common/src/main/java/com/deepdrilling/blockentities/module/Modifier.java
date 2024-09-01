package com.deepdrilling.blockentities.module;

import com.deepdrilling.Truple;
import com.deepdrilling.blockentities.drillcore.DrillCoreBE;
import com.deepdrilling.blockentities.drillhead.DrillHeadBE;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Comparator;
import java.util.Objects;

/**
 * A modifier applied by one or more modules that influences the drill in some way
 * @param <V> the data type that is modified
 */
public class Modifier<V, B> {
    public final Type<V> type;
    public final Modify<V, B> modifier;
    public final int priority;

    private Modifier(Type<V> type, Modify<V, B> modifier, int priority) {
        this.type = type;
        this.modifier = modifier;
        this.priority = priority;
    }

    public static Comparator<Truple<Integer, BlockEntity, Modifier>> modifierComparator = (lhs, rhs) -> {
        if (lhs.getC().getPriority() == rhs.getC().getPriority()) {
            // in tiebreaker, sort distance by ascending
            return lhs.getA() - rhs.getA();
        }
        // sort priority by descending
        return lhs.getC().getPriority() - rhs.getC().getPriority();
    };

    @FunctionalInterface
    public interface Modify<v, b> {
        v apply(DrillCoreBE core, DrillHeadBE head, b blockEntity, v base, v prev);
    }

    int getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public record Type<T>(Class<T> valueType, ResourceLocation name) {
        /**
         * Creates a modifier that is supplied by {@link Module#getModifiers()}
         * @param modifier The modifier applied to a value
         * @param priority The priority of the modifier (larger number goes first, in a tie the module closer to the drill goes first)
         */
        public <b> Modifier<T, b> create(Modify<T, b> modifier, int priority) {
                return new Modifier<>(this, modifier, priority);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Type<?> type = (Type<?>) o;
            return Objects.equals(valueType, type.valueType) && Objects.equals(name, type.name);
        }
    }
}
