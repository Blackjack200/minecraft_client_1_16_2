package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class OminousBannerBlockEntityRenameFix extends NamedEntityFix {
    public OminousBannerBlockEntityRenameFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "OminousBannerBlockEntityRenameFix", References.BLOCK_ENTITY, "minecraft:banner");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
    
    private Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final Optional<String> optional3 = (Optional<String>)dynamic.get("CustomName").asString().result();
        if (optional3.isPresent()) {
            String string4 = (String)optional3.get();
            string4 = string4.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
            return dynamic.set("CustomName", dynamic.createString(string4));
        }
        return dynamic;
    }
}
