package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class EntityZombifiedPiglinRenameFix extends SimplestEntityRenameFix {
    public static final Map<String, String> RENAMED_IDS;
    
    public EntityZombifiedPiglinRenameFix(final Schema schema) {
        super("EntityZombifiedPiglinRenameFix", schema, true);
    }
    
    @Override
    protected String rename(final String string) {
        return Objects.equals("minecraft:zombie_pigman", string) ? "minecraft:zombified_piglin" : string;
    }
    
    static {
        RENAMED_IDS = (Map)ImmutableMap.builder().put("minecraft:zombie_pigman_spawn_egg", "minecraft:zombified_piglin_spawn_egg").build();
    }
}
