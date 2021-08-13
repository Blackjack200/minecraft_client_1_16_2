package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DaylightDetectorBlock;

public class DaylightDetectorBlockEntity extends BlockEntity implements TickableBlockEntity {
    public DaylightDetectorBlockEntity() {
        super(BlockEntityType.DAYLIGHT_DETECTOR);
    }
    
    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide && this.level.getGameTime() % 20L == 0L) {
            final BlockState cee2 = this.getBlockState();
            final Block bul3 = cee2.getBlock();
            if (bul3 instanceof DaylightDetectorBlock) {
                DaylightDetectorBlock.updateSignalStrength(cee2, this.level, this.worldPosition);
            }
        }
    }
}
