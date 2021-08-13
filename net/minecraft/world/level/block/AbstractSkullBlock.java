package net.minecraft.world.level.block;

import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.Wearable;

public abstract class AbstractSkullBlock extends BaseEntityBlock implements Wearable {
    private final SkullBlock.Type type;
    
    public AbstractSkullBlock(final SkullBlock.Type a, final Properties c) {
        super(c);
        this.type = a;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new SkullBlockEntity();
    }
    
    public SkullBlock.Type getType() {
        return this.type;
    }
    
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
}
