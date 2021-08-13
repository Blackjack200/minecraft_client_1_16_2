package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityJukeboxFix extends NamedEntityFix {
    public BlockEntityJukeboxFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntityJukeboxFix", References.BLOCK_ENTITY, "minecraft:jukebox");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final Type<?> type3 = this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:jukebox");
        final Type<?> type4 = type3.findFieldType("RecordItem");
        final OpticFinder<?> opticFinder5 = DSL.fieldFinder("RecordItem", (Type)type4);
        final Dynamic<?> dynamic6 = typed.get(DSL.remainderFinder());
        final int integer7 = dynamic6.get("Record").asInt(0);
        if (integer7 > 0) {
            dynamic6.remove("Record");
            final String string8 = ItemStackTheFlatteningFix.updateItem(ItemIdFix.getItem(integer7), 0);
            if (string8 != null) {
                Dynamic<?> dynamic7 = dynamic6.emptyMap();
                dynamic7 = dynamic7.set("id", dynamic7.createString(string8));
                dynamic7 = dynamic7.set("Count", dynamic7.createByte((byte)1));
                return typed.set((OpticFinder)opticFinder5, (Typed)((Pair)type4.readTyped((Dynamic)dynamic7).result().orElseThrow(() -> new IllegalStateException("Could not create record item stack."))).getFirst()).set(DSL.remainderFinder(), dynamic6);
            }
        }
        return typed;
    }
}
