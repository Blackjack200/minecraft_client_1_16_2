package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class SavedDataUUIDFix extends AbstractUUIDFix {
    public SavedDataUUIDFix(final Schema schema) {
        super(schema, References.SAVED_DATA);
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("SavedDataUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> typed.updateTyped(typed.getType().findField("data"), typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("Raids", dynamic -> dynamic.createList(dynamic.asStream().map(dynamic -> dynamic.update("HeroesOfTheVillage", dynamic -> dynamic.createList(dynamic.asStream().map(dynamic -> (Dynamic)AbstractUUIDFix.createUUIDFromLongs(dynamic, "UUIDMost", "UUIDLeast").orElseGet(() -> {
            SavedDataUUIDFix.LOGGER.warn("HeroesOfTheVillage contained invalid UUIDs.");
            return dynamic;
        }))))))))));
    }
}
