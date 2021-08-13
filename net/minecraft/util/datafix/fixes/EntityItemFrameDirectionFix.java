package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityItemFrameDirectionFix extends NamedEntityFix {
    public EntityItemFrameDirectionFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityItemFrameDirectionFix", References.ENTITY, "minecraft:item_frame");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        return dynamic.set("Facing", dynamic.createByte(direction2dTo3d(dynamic.get("Facing").asByte((byte)0))));
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
    
    private static byte direction2dTo3d(final byte byte1) {
        switch (byte1) {
            default: {
                return 2;
            }
            case 0: {
                return 3;
            }
            case 1: {
                return 4;
            }
            case 3: {
                return 5;
            }
        }
    }
}
