package net.minecraft.world.level.block.entity;

import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.Container;

public interface Hopper extends Container {
    public static final VoxelShape INSIDE = Block.box(2.0, 11.0, 2.0, 14.0, 16.0, 14.0);
    public static final VoxelShape ABOVE = Block.box(0.0, 16.0, 0.0, 16.0, 32.0, 16.0);
    public static final VoxelShape SUCK = Shapes.or(Hopper.INSIDE, Hopper.ABOVE);
    
    default VoxelShape getSuckShape() {
        return Hopper.SUCK;
    }
    
    @Nullable
    Level getLevel();
    
    double getLevelX();
    
    double getLevelY();
    
    double getLevelZ();
}
