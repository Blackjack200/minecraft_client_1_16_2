package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class BlockEntityCustomNameToComponentFix extends DataFix {
    public BlockEntityCustomNameToComponentFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final OpticFinder<String> opticFinder2 = (OpticFinder<String>)DSL.fieldFinder("id", (Type)NamespacedSchema.namespacedString());
        return this.fixTypeEverywhereTyped("BlockEntityCustomNameToComponentFix", this.getInputSchema().getType(References.BLOCK_ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            final Optional<String> optional4 = (Optional<String>)typed.getOptional(opticFinder2);
            if (optional4.isPresent() && Objects.equals(optional4.get(), "minecraft:command_block")) {
                return dynamic;
            }
            return EntityCustomNameToComponentFix.fixTagCustomName(dynamic);
        }));
    }
}
