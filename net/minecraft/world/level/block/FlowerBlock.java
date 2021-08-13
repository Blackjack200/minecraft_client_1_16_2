package net.minecraft.world.level.block;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlowerBlock extends BushBlock {
    protected static final VoxelShape SHAPE;
    private final MobEffect suspiciousStewEffect;
    private final int effectDuration;
    
    public FlowerBlock(final MobEffect app, final int integer, final Properties c) {
        super(c);
        this.suspiciousStewEffect = app;
        if (app.isInstantenous()) {
            this.effectDuration = integer;
        }
        else {
            this.effectDuration = integer * 20;
        }
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Vec3 dck6 = cee.getOffset(bqz, fx);
        return FlowerBlock.SHAPE.move(dck6.x, dck6.y, dck6.z);
    }
    
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
    
    public MobEffect getSuspiciousStewEffect() {
        return this.suspiciousStewEffect;
    }
    
    public int getEffectDuration() {
        return this.effectDuration;
    }
    
    static {
        SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 10.0, 11.0);
    }
}
