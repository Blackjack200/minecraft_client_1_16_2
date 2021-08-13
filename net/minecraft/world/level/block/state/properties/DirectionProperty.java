package net.minecraft.world.level.block.state.properties;

import com.google.common.collect.Lists;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.Collection;
import net.minecraft.core.Direction;

public class DirectionProperty extends EnumProperty<Direction> {
    protected DirectionProperty(final String string, final Collection<Direction> collection) {
        super(string, Direction.class, collection);
    }
    
    public static DirectionProperty create(final String string, final Predicate<Direction> predicate) {
        return create(string, (Collection<Direction>)Arrays.stream((Object[])Direction.values()).filter((Predicate)predicate).collect(Collectors.toList()));
    }
    
    public static DirectionProperty create(final String string, final Direction... arr) {
        return create(string, (Collection<Direction>)Lists.newArrayList((Object[])arr));
    }
    
    public static DirectionProperty create(final String string, final Collection<Direction> collection) {
        return new DirectionProperty(string, collection);
    }
}
