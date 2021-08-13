package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import java.util.Arrays;
import com.mojang.serialization.OptionalDynamic;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityProjectileOwnerFix extends DataFix {
    public EntityProjectileOwnerFix(final Schema schema) {
        super(schema, false);
    }
    
    protected TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        return this.fixTypeEverywhereTyped("EntityProjectileOwner", schema2.getType(References.ENTITY), this::updateProjectiles);
    }
    
    private Typed<?> updateProjectiles(Typed<?> typed) {
        typed = this.updateEntity(typed, "minecraft:egg", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerThrowable);
        typed = this.updateEntity(typed, "minecraft:ender_pearl", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerThrowable);
        typed = this.updateEntity(typed, "minecraft:experience_bottle", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerThrowable);
        typed = this.updateEntity(typed, "minecraft:snowball", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerThrowable);
        typed = this.updateEntity(typed, "minecraft:potion", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerThrowable);
        typed = this.updateEntity(typed, "minecraft:potion", (Function<Dynamic<?>, Dynamic<?>>)this::updateItemPotion);
        typed = this.updateEntity(typed, "minecraft:llama_spit", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerLlamaSpit);
        typed = this.updateEntity(typed, "minecraft:arrow", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerArrow);
        typed = this.updateEntity(typed, "minecraft:spectral_arrow", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerArrow);
        typed = this.updateEntity(typed, "minecraft:trident", (Function<Dynamic<?>, Dynamic<?>>)this::updateOwnerArrow);
        return typed;
    }
    
    private Dynamic<?> updateOwnerArrow(final Dynamic<?> dynamic) {
        final long long3 = dynamic.get("OwnerUUIDMost").asLong(0L);
        final long long4 = dynamic.get("OwnerUUIDLeast").asLong(0L);
        return this.setUUID(dynamic, long3, long4).remove("OwnerUUIDMost").remove("OwnerUUIDLeast");
    }
    
    private Dynamic<?> updateOwnerLlamaSpit(final Dynamic<?> dynamic) {
        final OptionalDynamic<?> optionalDynamic3 = dynamic.get("Owner");
        final long long4 = optionalDynamic3.get("OwnerUUIDMost").asLong(0L);
        final long long5 = optionalDynamic3.get("OwnerUUIDLeast").asLong(0L);
        return this.setUUID(dynamic, long4, long5).remove("Owner");
    }
    
    private Dynamic<?> updateItemPotion(final Dynamic<?> dynamic) {
        final OptionalDynamic<?> optionalDynamic3 = dynamic.get("Potion");
        return dynamic.set("Item", optionalDynamic3.orElseEmptyMap()).remove("Potion");
    }
    
    private Dynamic<?> updateOwnerThrowable(final Dynamic<?> dynamic) {
        final String string3 = "owner";
        final OptionalDynamic<?> optionalDynamic4 = dynamic.get("owner");
        final long long5 = optionalDynamic4.get("M").asLong(0L);
        final long long6 = optionalDynamic4.get("L").asLong(0L);
        return this.setUUID(dynamic, long5, long6).remove("owner");
    }
    
    private Dynamic<?> setUUID(final Dynamic<?> dynamic, final long long2, final long long3) {
        final String string7 = "OwnerUUID";
        if (long2 != 0L && long3 != 0L) {
            return dynamic.set("OwnerUUID", dynamic.createIntList(Arrays.stream(createUUIDArray(long2, long3))));
        }
        return dynamic;
    }
    
    private static int[] createUUIDArray(final long long1, final long long2) {
        return new int[] { (int)(long1 >> 32), (int)long1, (int)(long2 >> 32), (int)long2 };
    }
    
    private Typed<?> updateEntity(final Typed<?> typed, final String string, final Function<Dynamic<?>, Dynamic<?>> function) {
        final Type<?> type5 = this.getInputSchema().getChoiceType(References.ENTITY, string);
        final Type<?> type6 = this.getOutputSchema().getChoiceType(References.ENTITY, string);
        return typed.updateTyped(DSL.namedChoice(string, (Type)type5), (Type)type6, typed -> typed.update(DSL.remainderFinder(), function));
    }
}
