package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class EntityPufferfishRenameFix extends SimplestEntityRenameFix {
    public static final Map<String, String> RENAMED_IDS;
    
    public EntityPufferfishRenameFix(final Schema schema, final boolean boolean2) {
        super("EntityPufferfishRenameFix", schema, boolean2);
    }
    
    @Override
    protected String rename(final String string) {
        return Objects.equals("minecraft:puffer_fish", string) ? "minecraft:pufferfish" : string;
    }
    
    static {
        RENAMED_IDS = (Map)ImmutableMap.builder().put("minecraft:puffer_fish_spawn_egg", "minecraft:pufferfish_spawn_egg").build();
    }
}
