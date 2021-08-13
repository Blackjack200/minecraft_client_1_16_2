package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.base.MoreObjects;
import java.util.Optional;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.stream.Collectors;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.common.base.Splitter;

public class KeyValueCondition implements Condition {
    private static final Splitter PIPE_SPLITTER;
    private final String key;
    private final String value;
    
    public KeyValueCondition(final String string1, final String string2) {
        this.key = string1;
        this.value = string2;
    }
    
    public Predicate<BlockState> getPredicate(final StateDefinition<Block, BlockState> cef) {
        final Property<?> cfg3 = cef.getProperty(this.key);
        if (cfg3 == null) {
            throw new RuntimeException(String.format("Unknown property '%s' on '%s'", new Object[] { this.key, cef.getOwner().toString() }));
        }
        String string4 = this.value;
        final boolean boolean5 = !string4.isEmpty() && string4.charAt(0) == '!';
        if (boolean5) {
            string4 = string4.substring(1);
        }
        final List<String> list6 = (List<String>)KeyValueCondition.PIPE_SPLITTER.splitToList((CharSequence)string4);
        if (list6.isEmpty()) {
            throw new RuntimeException(String.format("Empty value '%s' for property '%s' on '%s'", new Object[] { this.value, this.key, cef.getOwner().toString() }));
        }
        Predicate<BlockState> predicate7;
        if (list6.size() == 1) {
            predicate7 = this.getBlockStatePredicate(cef, cfg3, string4);
        }
        else {
            final List<Predicate<BlockState>> list7 = (List<Predicate<BlockState>>)list6.stream().map(string -> this.getBlockStatePredicate(cef, cfg3, string)).collect(Collectors.toList());
            predicate7 = (Predicate<BlockState>)(cee -> list7.stream().anyMatch(predicate -> predicate.test(cee)));
        }
        return (Predicate<BlockState>)(boolean5 ? predicate7.negate() : predicate7);
    }
    
    private Predicate<BlockState> getBlockStatePredicate(final StateDefinition<Block, BlockState> cef, final Property<?> cfg, final String string) {
        final Optional<?> optional5 = cfg.getValue(string);
        if (!optional5.isPresent()) {
            throw new RuntimeException(String.format("Unknown value '%s' for property '%s' on '%s' in '%s'", new Object[] { string, this.key, cef.getOwner().toString(), this.value }));
        }
        return (Predicate<BlockState>)(cee -> cee.getValue(cfg).equals(optional5.get()));
    }
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("key", this.key).add("value", this.value).toString();
    }
    
    static {
        PIPE_SPLITTER = Splitter.on('|').omitEmptyStrings();
    }
}
