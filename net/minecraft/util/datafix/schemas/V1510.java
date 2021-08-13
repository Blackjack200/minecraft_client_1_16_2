package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1510 extends NamespacedSchema {
    public V1510(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerEntities(schema);
        map3.put("minecraft:command_block_minecart", map3.remove("minecraft:commandblock_minecart"));
        map3.put("minecraft:end_crystal", map3.remove("minecraft:ender_crystal"));
        map3.put("minecraft:snow_golem", map3.remove("minecraft:snowman"));
        map3.put("minecraft:evoker", map3.remove("minecraft:evocation_illager"));
        map3.put("minecraft:evoker_fangs", map3.remove("minecraft:evocation_fangs"));
        map3.put("minecraft:illusioner", map3.remove("minecraft:illusion_illager"));
        map3.put("minecraft:vindicator", map3.remove("minecraft:vindication_illager"));
        map3.put("minecraft:iron_golem", map3.remove("minecraft:villager_golem"));
        map3.put("minecraft:experience_orb", map3.remove("minecraft:xp_orb"));
        map3.put("minecraft:experience_bottle", map3.remove("minecraft:xp_bottle"));
        map3.put("minecraft:eye_of_ender", map3.remove("minecraft:eye_of_ender_signal"));
        map3.put("minecraft:firework_rocket", map3.remove("minecraft:fireworks_rocket"));
        return map3;
    }
}
