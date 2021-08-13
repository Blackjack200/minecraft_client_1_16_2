package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import com.google.common.collect.Maps;
import java.util.HashMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import java.util.Locale;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;

public class EntityPaintingMotiveFix extends NamedEntityFix {
    private static final Map<String, String> MAP;
    
    public EntityPaintingMotiveFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityPaintingMotiveFix", References.ENTITY, "minecraft:painting");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final Optional<String> optional3 = (Optional<String>)dynamic.get("Motive").asString().result();
        if (optional3.isPresent()) {
            final String string4 = ((String)optional3.get()).toLowerCase(Locale.ROOT);
            return dynamic.set("Motive", dynamic.createString(new ResourceLocation((String)EntityPaintingMotiveFix.MAP.getOrDefault(string4, string4)).toString()));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
    
    static {
        MAP = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            hashMap.put("donkeykong", "donkey_kong");
            hashMap.put("burningskull", "burning_skull");
            hashMap.put("skullandroses", "skull_and_roses");
        });
    }
}
