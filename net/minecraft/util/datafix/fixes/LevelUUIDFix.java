package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class LevelUUIDFix extends AbstractUUIDFix {
    public LevelUUIDFix(final Schema schema) {
        super(schema, References.LEVEL);
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("LevelUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> typed.updateTyped(DSL.remainderFinder(), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            dynamic = this.updateCustomBossEvents(dynamic);
            dynamic = this.updateDragonFight(dynamic);
            dynamic = this.updateWanderingTrader(dynamic);
            return dynamic;
        })));
    }
    
    private Dynamic<?> updateWanderingTrader(final Dynamic<?> dynamic) {
        return AbstractUUIDFix.replaceUUIDString(dynamic, "WanderingTraderId", "WanderingTraderId").orElse(dynamic);
    }
    
    private Dynamic<?> updateDragonFight(final Dynamic<?> dynamic) {
        return dynamic.update("DimensionData", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.update("DragonFight", dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "DragonUUID", "Dragon").orElse(dynamic)))));
    }
    
    private Dynamic<?> updateCustomBossEvents(final Dynamic<?> dynamic) {
        return dynamic.update("CustomBossEvents", dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.update("Players", dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> (Dynamic)AbstractUUIDFix.createUUIDFromML(dynamic).orElseGet(() -> {
            LevelUUIDFix.LOGGER.warn("CustomBossEvents contains invalid UUIDs.");
            return dynamic;
        })))))));
    }
}
