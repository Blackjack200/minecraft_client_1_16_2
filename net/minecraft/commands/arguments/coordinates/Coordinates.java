package net.minecraft.commands.arguments.coordinates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSourceStack;

public interface Coordinates {
    Vec3 getPosition(final CommandSourceStack db);
    
    Vec2 getRotation(final CommandSourceStack db);
    
    default BlockPos getBlockPos(final CommandSourceStack db) {
        return new BlockPos(this.getPosition(db));
    }
    
    boolean isXRelative();
    
    boolean isYRelative();
    
    boolean isZRelative();
}
