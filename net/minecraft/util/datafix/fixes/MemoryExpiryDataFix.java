package net.minecraft.util.datafix.fixes;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class MemoryExpiryDataFix extends NamedEntityFix {
    public MemoryExpiryDataFix(final Schema schema, final String string) {
        super(schema, false, "Memory expiry data fix (" + string + ")", References.ENTITY, string);
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        return dynamic.update("Brain", this::updateBrain);
    }
    
    private Dynamic<?> updateBrain(final Dynamic<?> dynamic) {
        return dynamic.update("memories", this::updateMemories);
    }
    
    private Dynamic<?> updateMemories(final Dynamic<?> dynamic) {
        return dynamic.updateMapValues(this::updateMemoryEntry);
    }
    
    private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(final Pair<Dynamic<?>, Dynamic<?>> pair) {
        return (Pair<Dynamic<?>, Dynamic<?>>)pair.mapSecond(this::wrapMemoryValue);
    }
    
    private Dynamic<?> wrapMemoryValue(final Dynamic<?> dynamic) {
        return dynamic.createMap((Map)ImmutableMap.of(dynamic.createString("value"), dynamic));
    }
}
