package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class EntityCodSalmonFix extends SimplestEntityRenameFix {
    public static final Map<String, String> RENAMED_IDS;
    public static final Map<String, String> RENAMED_EGG_IDS;
    
    public EntityCodSalmonFix(final Schema schema, final boolean boolean2) {
        super("EntityCodSalmonFix", schema, boolean2);
    }
    
    @Override
    protected String rename(final String string) {
        return (String)EntityCodSalmonFix.RENAMED_IDS.getOrDefault(string, string);
    }
    
    static {
        RENAMED_IDS = (Map)ImmutableMap.builder().put("minecraft:salmon_mob", "minecraft:salmon").put("minecraft:cod_mob", "minecraft:cod").build();
        RENAMED_EGG_IDS = (Map)ImmutableMap.builder().put("minecraft:salmon_mob_spawn_egg", "minecraft:salmon_spawn_egg").put("minecraft:cod_mob_spawn_egg", "minecraft:cod_spawn_egg").build();
    }
}
