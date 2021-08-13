package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.BedBlock;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.DyeColor;

public class BedBlockEntity extends BlockEntity {
    private DyeColor color;
    
    public BedBlockEntity() {
        super(BlockEntityType.BED);
    }
    
    public BedBlockEntity(final DyeColor bku) {
        this();
        this.setColor(bku);
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 11, this.getUpdateTag());
    }
    
    public DyeColor getColor() {
        if (this.color == null) {
            this.color = ((BedBlock)this.getBlockState().getBlock()).getColor();
        }
        return this.color;
    }
    
    public void setColor(final DyeColor bku) {
        this.color = bku;
    }
}
