package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityBlockStateFix extends NamedEntityFix {
    public BlockEntityBlockStateFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntityBlockStateFix", References.BLOCK_ENTITY, "minecraft:piston");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final Type<?> type3 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:piston");
        final Type<?> type4 = type3.findFieldType("blockState");
        final OpticFinder<?> opticFinder5 = DSL.fieldFinder("blockState", (Type)type4);
        Dynamic<?> dynamic6 = typed.get(DSL.remainderFinder());
        final int integer7 = dynamic6.get("blockId").asInt(0);
        dynamic6 = dynamic6.remove("blockId");
        final int integer8 = dynamic6.get("blockData").asInt(0) & 0xF;
        dynamic6 = dynamic6.remove("blockData");
        final Dynamic<?> dynamic7 = BlockStateData.getTag(integer7 << 4 | integer8);
        final Typed<?> typed2 = type3.pointTyped(typed.getOps()).orElseThrow(() -> new IllegalStateException("Could not create new piston block entity."));
        return typed2.set(DSL.remainderFinder(), dynamic6).set((OpticFinder)opticFinder5, (Typed)((Pair)type4.readTyped((Dynamic)dynamic7).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created block state tag."))).getFirst());
    }
}
