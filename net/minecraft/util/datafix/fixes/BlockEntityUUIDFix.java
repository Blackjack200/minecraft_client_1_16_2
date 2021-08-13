package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityUUIDFix extends AbstractUUIDFix {
    public BlockEntityUUIDFix(final Schema schema) {
        super(schema, References.BLOCK_ENTITY);
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("BlockEntityUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            typed = this.updateNamedChoice(typed, "minecraft:conduit", (Function<Dynamic<?>, Dynamic<?>>)this::updateConduit);
            typed = this.updateNamedChoice(typed, "minecraft:skull", (Function<Dynamic<?>, Dynamic<?>>)this::updateSkull);
            return typed;
        });
    }
    
    private Dynamic<?> updateSkull(final Dynamic<?> dynamic) {
        return dynamic.get("Owner").get().map(dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDString(dynamic, "Id", "Id").orElse(dynamic)).map(dynamic2 -> dynamic.remove("Owner").set("SkullOwner", dynamic2)).result().orElse(dynamic);
    }
    
    private Dynamic<?> updateConduit(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDMLTag(dynamic, "target_uuid", "Target").orElse(dynamic);
    }
}
