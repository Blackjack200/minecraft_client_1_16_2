package net.minecraft.stats;

import com.google.common.collect.ImmutableMap;
import java.util.EnumMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.inventory.RecipeBookType;
import java.util.Map;

public final class RecipeBookSettings {
    private static final Map<RecipeBookType, Pair<String, String>> TAG_FIELDS;
    private final Map<RecipeBookType, TypeSettings> states;
    
    private RecipeBookSettings(final Map<RecipeBookType, TypeSettings> map) {
        this.states = map;
    }
    
    public RecipeBookSettings() {
        this(Util.make((Map)Maps.newEnumMap((Class)RecipeBookType.class), (java.util.function.Consumer<Map>)(enumMap -> {
            for (final RecipeBookType bjh5 : RecipeBookType.values()) {
                enumMap.put((Enum)bjh5, new TypeSettings(false, false));
            }
        })));
    }
    
    public boolean isOpen(final RecipeBookType bjh) {
        return ((TypeSettings)this.states.get(bjh)).open;
    }
    
    public void setOpen(final RecipeBookType bjh, final boolean boolean2) {
        ((TypeSettings)this.states.get(bjh)).open = boolean2;
    }
    
    public boolean isFiltering(final RecipeBookType bjh) {
        return ((TypeSettings)this.states.get(bjh)).filtering;
    }
    
    public void setFiltering(final RecipeBookType bjh, final boolean boolean2) {
        ((TypeSettings)this.states.get(bjh)).filtering = boolean2;
    }
    
    public static RecipeBookSettings read(final FriendlyByteBuf nf) {
        final Map<RecipeBookType, TypeSettings> map2 = (Map<RecipeBookType, TypeSettings>)Maps.newEnumMap((Class)RecipeBookType.class);
        for (final RecipeBookType bjh6 : RecipeBookType.values()) {
            final boolean boolean7 = nf.readBoolean();
            final boolean boolean8 = nf.readBoolean();
            map2.put(bjh6, new TypeSettings(boolean7, boolean8));
        }
        return new RecipeBookSettings(map2);
    }
    
    public void write(final FriendlyByteBuf nf) {
        for (final RecipeBookType bjh6 : RecipeBookType.values()) {
            final TypeSettings a7 = (TypeSettings)this.states.get(bjh6);
            if (a7 == null) {
                nf.writeBoolean(false);
                nf.writeBoolean(false);
            }
            else {
                nf.writeBoolean(a7.open);
                nf.writeBoolean(a7.filtering);
            }
        }
    }
    
    public static RecipeBookSettings read(final CompoundTag md) {
        final Map<RecipeBookType, TypeSettings> map2 = (Map<RecipeBookType, TypeSettings>)Maps.newEnumMap((Class)RecipeBookType.class);
        RecipeBookSettings.TAG_FIELDS.forEach((bjh, pair) -> {
            final boolean boolean5 = md.getBoolean((String)pair.getFirst());
            final boolean boolean6 = md.getBoolean((String)pair.getSecond());
            map2.put(bjh, new TypeSettings(boolean5, boolean6));
        });
        return new RecipeBookSettings(map2);
    }
    
    public void write(final CompoundTag md) {
        RecipeBookSettings.TAG_FIELDS.forEach((bjh, pair) -> {
            final TypeSettings a5 = (TypeSettings)this.states.get(bjh);
            md.putBoolean((String)pair.getFirst(), a5.open);
            md.putBoolean((String)pair.getSecond(), a5.filtering);
        });
    }
    
    public RecipeBookSettings copy() {
        final Map<RecipeBookType, TypeSettings> map2 = (Map<RecipeBookType, TypeSettings>)Maps.newEnumMap((Class)RecipeBookType.class);
        for (final RecipeBookType bjh6 : RecipeBookType.values()) {
            final TypeSettings a7 = (TypeSettings)this.states.get(bjh6);
            map2.put(bjh6, a7.copy());
        }
        return new RecipeBookSettings(map2);
    }
    
    public void replaceFrom(final RecipeBookSettings ads) {
        this.states.clear();
        for (final RecipeBookType bjh6 : RecipeBookType.values()) {
            final TypeSettings a7 = (TypeSettings)ads.states.get(bjh6);
            this.states.put(bjh6, a7.copy());
        }
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof RecipeBookSettings && this.states.equals(((RecipeBookSettings)object).states));
    }
    
    public int hashCode() {
        return this.states.hashCode();
    }
    
    static {
        TAG_FIELDS = (Map)ImmutableMap.of(RecipeBookType.CRAFTING, Pair.of("isGuiOpen", "isFilteringCraftable"), RecipeBookType.FURNACE, Pair.of("isFurnaceGuiOpen", "isFurnaceFilteringCraftable"), RecipeBookType.BLAST_FURNACE, Pair.of("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable"), RecipeBookType.SMOKER, Pair.of("isSmokerGuiOpen", "isSmokerFilteringCraftable"));
    }
    
    static final class TypeSettings {
        private boolean open;
        private boolean filtering;
        
        public TypeSettings(final boolean boolean1, final boolean boolean2) {
            this.open = boolean1;
            this.filtering = boolean2;
        }
        
        public TypeSettings copy() {
            return new TypeSettings(this.open, this.filtering);
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof TypeSettings) {
                final TypeSettings a3 = (TypeSettings)object;
                return this.open == a3.open && this.filtering == a3.filtering;
            }
            return false;
        }
        
        public int hashCode() {
            int integer2 = this.open ? 1 : 0;
            integer2 = 31 * integer2 + (this.filtering ? 1 : 0);
            return integer2;
        }
        
        public String toString() {
            return new StringBuilder().append("[open=").append(this.open).append(", filtering=").append(this.filtering).append(']').toString();
        }
    }
}
