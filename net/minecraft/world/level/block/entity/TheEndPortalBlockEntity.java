package net.minecraft.world.level.block.entity;

import net.minecraft.core.Direction;

public class TheEndPortalBlockEntity extends BlockEntity {
    public TheEndPortalBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
    }
    
    public TheEndPortalBlockEntity() {
        this(BlockEntityType.END_PORTAL);
    }
    
    public boolean shouldRenderFace(final Direction gc) {
        return gc == Direction.UP;
    }
}
