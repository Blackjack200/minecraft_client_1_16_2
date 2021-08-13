package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

public abstract class ScatteredFeaturePiece extends StructurePiece {
    protected final int width;
    protected final int height;
    protected final int depth;
    protected int heightPosition;
    
    protected ScatteredFeaturePiece(final StructurePieceType cky, final Random random, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8) {
        super(cky, 0);
        this.heightPosition = -1;
        this.width = integer6;
        this.height = integer7;
        this.depth = integer8;
        this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(random));
        if (this.getOrientation().getAxis() == Direction.Axis.Z) {
            this.boundingBox = new BoundingBox(integer3, integer4, integer5, integer3 + integer6 - 1, integer4 + integer7 - 1, integer5 + integer8 - 1);
        }
        else {
            this.boundingBox = new BoundingBox(integer3, integer4, integer5, integer3 + integer8 - 1, integer4 + integer7 - 1, integer5 + integer6 - 1);
        }
    }
    
    protected ScatteredFeaturePiece(final StructurePieceType cky, final CompoundTag md) {
        super(cky, md);
        this.heightPosition = -1;
        this.width = md.getInt("Width");
        this.height = md.getInt("Height");
        this.depth = md.getInt("Depth");
        this.heightPosition = md.getInt("HPos");
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.putInt("Width", this.width);
        md.putInt("Height", this.height);
        md.putInt("Depth", this.depth);
        md.putInt("HPos", this.heightPosition);
    }
    
    protected boolean updateAverageGroundHeight(final LevelAccessor brv, final BoundingBox cqx, final int integer) {
        if (this.heightPosition >= 0) {
            return true;
        }
        int integer2 = 0;
        int integer3 = 0;
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        for (int integer4 = this.boundingBox.z0; integer4 <= this.boundingBox.z1; ++integer4) {
            for (int integer5 = this.boundingBox.x0; integer5 <= this.boundingBox.x1; ++integer5) {
                a7.set(integer5, 64, integer4);
                if (cqx.isInside(a7)) {
                    integer2 += brv.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, a7).getY();
                    ++integer3;
                }
            }
        }
        if (integer3 == 0) {
            return false;
        }
        this.heightPosition = integer2 / integer3;
        this.boundingBox.move(0, this.heightPosition - this.boundingBox.y0 + integer, 0);
        return true;
    }
}
