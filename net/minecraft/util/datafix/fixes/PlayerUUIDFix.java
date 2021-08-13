package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class PlayerUUIDFix extends AbstractUUIDFix {
    public PlayerUUIDFix(final Schema schema) {
        super(schema, References.PLAYER);
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("PlayerUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            final OpticFinder<?> opticFinder2 = typed.getType().findField("RootVehicle");
            return typed.updateTyped((OpticFinder)opticFinder2, opticFinder2.type(), typed -> typed.update(DSL.remainderFinder(), dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "Attach", "Attach").orElse(dynamic))).update(DSL.remainderFinder(), dynamic -> EntityUUIDFix.updateEntityUUID(EntityUUIDFix.updateLivingEntity(dynamic)));
        });
    }
}
