package net.minecraft.world.level.block.state.predicate;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Map;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;

public class BlockStatePredicate implements Predicate<BlockState> {
    public static final Predicate<BlockState> ANY;
    private final StateDefinition<Block, BlockState> definition;
    private final Map<Property<?>, Predicate<Object>> properties;
    
    private BlockStatePredicate(final StateDefinition<Block, BlockState> cef) {
        this.properties = (Map<Property<?>, Predicate<Object>>)Maps.newHashMap();
        this.definition = cef;
    }
    
    public static BlockStatePredicate forBlock(final Block bul) {
        return new BlockStatePredicate(bul.getStateDefinition());
    }
    
    public boolean test(@Nullable final BlockState cee) {
        if (cee == null || !cee.getBlock().equals(this.definition.getOwner())) {
            return false;
        }
        if (this.properties.isEmpty()) {
            return true;
        }
        for (final Map.Entry<Property<?>, Predicate<Object>> entry4 : this.properties.entrySet()) {
            if (!this.<Comparable>applies(cee, (Property<Comparable>)entry4.getKey(), (Predicate<Object>)entry4.getValue())) {
                return false;
            }
        }
        return true;
    }
    
    protected <T extends Comparable<T>> boolean applies(final BlockState cee, final Property<T> cfg, final Predicate<Object> predicate) {
        final T comparable5 = cee.<T>getValue(cfg);
        return predicate.test(comparable5);
    }
    
    public <V extends Comparable<V>> BlockStatePredicate where(final Property<V> cfg, final Predicate<Object> predicate) {
        if (!this.definition.getProperties().contains(cfg)) {
            throw new IllegalArgumentException(new StringBuilder().append(this.definition).append(" cannot support property ").append(cfg).toString());
        }
        this.properties.put(cfg, predicate);
        return this;
    }
    
    static {
        ANY = (cee -> true);
    }
}
