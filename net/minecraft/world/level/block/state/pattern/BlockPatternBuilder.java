package net.minecraft.world.level.block.state.pattern;

import java.util.Iterator;
import java.lang.reflect.Array;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.base.Predicates;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.function.Predicate;
import java.util.Map;
import java.util.List;
import com.google.common.base.Joiner;

public class BlockPatternBuilder {
    private static final Joiner COMMA_JOINED;
    private final List<String[]> pattern;
    private final Map<Character, Predicate<BlockInWorld>> lookup;
    private int height;
    private int width;
    
    private BlockPatternBuilder() {
        this.pattern = (List<String[]>)Lists.newArrayList();
        (this.lookup = (Map<Character, Predicate<BlockInWorld>>)Maps.newHashMap()).put(' ', Predicates.alwaysTrue());
    }
    
    public BlockPatternBuilder aisle(final String... arr) {
        if (ArrayUtils.isEmpty((Object[])arr) || StringUtils.isEmpty((CharSequence)arr[0])) {
            throw new IllegalArgumentException("Empty pattern for aisle");
        }
        if (this.pattern.isEmpty()) {
            this.height = arr.length;
            this.width = arr[0].length();
        }
        if (arr.length != this.height) {
            throw new IllegalArgumentException(new StringBuilder().append("Expected aisle with height of ").append(this.height).append(", but was given one with a height of ").append(arr.length).append(")").toString());
        }
        for (final String string6 : arr) {
            if (string6.length() != this.width) {
                throw new IllegalArgumentException(new StringBuilder().append("Not all rows in the given aisle are the correct width (expected ").append(this.width).append(", found one with ").append(string6.length()).append(")").toString());
            }
            for (final char character10 : string6.toCharArray()) {
                if (!this.lookup.containsKey(character10)) {
                    this.lookup.put(character10, null);
                }
            }
        }
        this.pattern.add(arr);
        return this;
    }
    
    public static BlockPatternBuilder start() {
        return new BlockPatternBuilder();
    }
    
    public BlockPatternBuilder where(final char character, final Predicate<BlockInWorld> predicate) {
        this.lookup.put(character, predicate);
        return this;
    }
    
    public BlockPattern build() {
        return new BlockPattern(this.createPattern());
    }
    
    private Predicate<BlockInWorld>[][][] createPattern() {
        this.ensureAllCharactersMatched();
        final Predicate<BlockInWorld>[][][] arr2 = (Predicate<BlockInWorld>[][][])Array.newInstance((Class)Predicate.class, new int[] { this.pattern.size(), this.height, this.width });
        for (int integer3 = 0; integer3 < this.pattern.size(); ++integer3) {
            for (int integer4 = 0; integer4 < this.height; ++integer4) {
                for (int integer5 = 0; integer5 < this.width; ++integer5) {
                    arr2[integer3][integer4][integer5] = (Predicate<BlockInWorld>)this.lookup.get(((String[])this.pattern.get(integer3))[integer4].charAt(integer5));
                }
            }
        }
        return arr2;
    }
    
    private void ensureAllCharactersMatched() {
        final List<Character> list2 = (List<Character>)Lists.newArrayList();
        for (final Map.Entry<Character, Predicate<BlockInWorld>> entry4 : this.lookup.entrySet()) {
            if (entry4.getValue() == null) {
                list2.add(entry4.getKey());
            }
        }
        if (!list2.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + BlockPatternBuilder.COMMA_JOINED.join((Iterable)list2) + " are missing");
        }
    }
    
    static {
        COMMA_JOINED = Joiner.on(",");
    }
}
