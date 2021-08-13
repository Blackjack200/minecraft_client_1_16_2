package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class EntityRavagerRenameFix extends SimplestEntityRenameFix {
    public static final Map<String, String> RENAMED_IDS;
    
    public EntityRavagerRenameFix(final Schema schema, final boolean boolean2) {
        super("EntityRavagerRenameFix", schema, boolean2);
    }
    
    @Override
    protected String rename(final String string) {
        return Objects.equals("minecraft:illager_beast", string) ? "minecraft:ravager" : string;
    }
    
    static {
        RENAMED_IDS = (Map)ImmutableMap.builder().put("minecraft:illager_beast_spawn_egg", "minecraft:ravager_spawn_egg").build();
    }
}
