package net.minecraft.world.level.block.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

public class EnderChestBlockEntity extends BlockEntity implements LidBlockEntity, TickableBlockEntity {
    public float openness;
    public float oOpenness;
    public int openCount;
    private int tickInterval;
    
    public EnderChestBlockEntity() {
        super(BlockEntityType.ENDER_CHEST);
    }
    
    @Override
    public void tick() {
        if (++this.tickInterval % 20 * 4 == 0) {
            this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
        }
        this.oOpenness = this.openness;
        final int integer2 = this.worldPosition.getX();
        final int integer3 = this.worldPosition.getY();
        final int integer4 = this.worldPosition.getZ();
        final float float5 = 0.1f;
        if (this.openCount > 0 && this.openness == 0.0f) {
            final double double6 = integer2 + 0.5;
            final double double7 = integer4 + 0.5;
            this.level.playSound(null, double6, integer3 + 0.5, double7, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
        }
        if ((this.openCount == 0 && this.openness > 0.0f) || (this.openCount > 0 && this.openness < 1.0f)) {
            final float float6 = this.openness;
            if (this.openCount > 0) {
                this.openness += 0.1f;
            }
            else {
                this.openness -= 0.1f;
            }
            if (this.openness > 1.0f) {
                this.openness = 1.0f;
            }
            final float float7 = 0.5f;
            if (this.openness < 0.5f && float6 >= 0.5f) {
                final double double7 = integer2 + 0.5;
                final double double8 = integer4 + 0.5;
                this.level.playSound(null, double7, integer3 + 0.5, double8, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5f, this.level.random.nextFloat() * 0.1f + 0.9f);
            }
            if (this.openness < 0.0f) {
                this.openness = 0.0f;
            }
        }
    }
    
    @Override
    public boolean triggerEvent(final int integer1, final int integer2) {
        if (integer1 == 1) {
            this.openCount = integer2;
            return true;
        }
        return super.triggerEvent(integer1, integer2);
    }
    
    @Override
    public void setRemoved() {
        this.clearCache();
        super.setRemoved();
    }
    
    public void startOpen() {
        ++this.openCount;
        this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
    }
    
    public void stopOpen() {
        --this.openCount;
        this.level.blockEvent(this.worldPosition, Blocks.ENDER_CHEST, 1, this.openCount);
    }
    
    public boolean stillValid(final Player bft) {
        return this.level.getBlockEntity(this.worldPosition) == this && bft.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public float getOpenNess(final float float1) {
        return Mth.lerp(float1, this.oOpenness, this.openness);
    }
}
