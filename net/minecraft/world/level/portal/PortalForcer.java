package net.minecraft.world.level.portal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.server.level.TicketType;
import java.util.Iterator;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import java.util.Comparator;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.function.Predicate;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.LevelReader;
import net.minecraft.BlockUtil;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class PortalForcer {
    private final ServerLevel level;
    
    public PortalForcer(final ServerLevel aag) {
        this.level = aag;
    }
    
    public Optional<BlockUtil.FoundRectangle> findPortalAround(final BlockPos fx, final boolean boolean2) {
        final PoiManager azl4 = this.level.getPoiManager();
        final int integer5 = boolean2 ? 16 : 128;
        azl4.ensureLoadedAndValid(this.level, fx, integer5);
        final Optional<PoiRecord> optional6 = (Optional<PoiRecord>)azl4.getInSquare((Predicate<PoiType>)(azo -> azo == PoiType.NETHER_PORTAL), fx, integer5, PoiManager.Occupancy.ANY).sorted(Comparator.comparingDouble(azm -> azm.getPos().distSqr(fx)).thenComparingInt(azm -> azm.getPos().getY())).filter(azm -> this.level.getBlockState(azm.getPos()).<Direction.Axis>hasProperty(BlockStateProperties.HORIZONTAL_AXIS)).findFirst();
        return (Optional<BlockUtil.FoundRectangle>)optional6.map(azm -> {
            final BlockPos fx3 = azm.getPos();
            this.level.getChunkSource().<BlockPos>addRegionTicket(TicketType.PORTAL, new ChunkPos(fx3), 3, fx3);
            final BlockState cee4 = this.level.getBlockState(fx3);
            return BlockUtil.getLargestRectangleAround(fx3, cee4.<Direction.Axis>getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (Predicate<BlockPos>)(fx -> this.level.getBlockState(fx) == cee4));
        });
    }
    
    public Optional<BlockUtil.FoundRectangle> createPortal(final BlockPos fx, final Direction.Axis a) {
        final Direction gc4 = Direction.get(Direction.AxisDirection.POSITIVE, a);
        double double5 = -1.0;
        BlockPos fx2 = null;
        double double6 = -1.0;
        BlockPos fx3 = null;
        final WorldBorder cfr11 = this.level.getWorldBorder();
        final int integer12 = this.level.getHeight() - 1;
        final BlockPos.MutableBlockPos a2 = fx.mutable();
        for (final BlockPos.MutableBlockPos a3 : BlockPos.spiralAround(fx, 16, Direction.EAST, Direction.SOUTH)) {
            final int integer13 = Math.min(integer12, this.level.getHeight(Heightmap.Types.MOTION_BLOCKING, a3.getX(), a3.getZ()));
            final int integer14 = 1;
            if (cfr11.isWithinBounds(a3)) {
                if (!cfr11.isWithinBounds(a3.move(gc4, 1))) {
                    continue;
                }
                a3.move(gc4.getOpposite(), 1);
                for (int integer15 = integer13; integer15 >= 0; --integer15) {
                    a3.setY(integer15);
                    if (this.level.isEmptyBlock(a3)) {
                        final int integer16 = integer15;
                        while (integer15 > 0 && this.level.isEmptyBlock(a3.move(Direction.DOWN))) {
                            --integer15;
                        }
                        if (integer15 + 4 <= integer12) {
                            final int integer17 = integer16 - integer15;
                            if (integer17 <= 0 || integer17 >= 3) {
                                a3.setY(integer15);
                                if (this.canHostFrame(a3, a2, gc4, 0)) {
                                    final double double7 = fx.distSqr(a3);
                                    if (this.canHostFrame(a3, a2, gc4, -1) && this.canHostFrame(a3, a2, gc4, 1) && (double5 == -1.0 || double5 > double7)) {
                                        double5 = double7;
                                        fx2 = a3.immutable();
                                    }
                                    if (double5 == -1.0 && (double6 == -1.0 || double6 > double7)) {
                                        double6 = double7;
                                        fx3 = a3.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (double5 == -1.0 && double6 != -1.0) {
            fx2 = fx3;
            double5 = double6;
        }
        if (double5 == -1.0) {
            fx2 = new BlockPos(fx.getX(), Mth.clamp(fx.getY(), 70, this.level.getHeight() - 10), fx.getZ()).immutable();
            final Direction gc5 = gc4.getClockWise();
            if (!cfr11.isWithinBounds(fx2)) {
                return (Optional<BlockUtil.FoundRectangle>)Optional.empty();
            }
            for (int integer18 = -1; integer18 < 2; ++integer18) {
                for (int integer13 = 0; integer13 < 2; ++integer13) {
                    for (int integer14 = -1; integer14 < 3; ++integer14) {
                        final BlockState cee18 = (integer14 < 0) ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        a2.setWithOffset(fx2, integer13 * gc4.getStepX() + integer18 * gc5.getStepX(), integer14, integer13 * gc4.getStepZ() + integer18 * gc5.getStepZ());
                        this.level.setBlockAndUpdate(a2, cee18);
                    }
                }
            }
        }
        for (int integer19 = -1; integer19 < 3; ++integer19) {
            for (int integer18 = -1; integer18 < 4; ++integer18) {
                if (integer19 == -1 || integer19 == 2 || integer18 == -1 || integer18 == 3) {
                    a2.setWithOffset(fx2, integer19 * gc4.getStepX(), integer18, integer19 * gc4.getStepZ());
                    this.level.setBlock(a2, Blocks.OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }
        final BlockState cee19 = ((StateHolder<O, BlockState>)Blocks.NETHER_PORTAL.defaultBlockState()).<Direction.Axis, Direction.Axis>setValue(NetherPortalBlock.AXIS, a);
        for (int integer18 = 0; integer18 < 2; ++integer18) {
            for (int integer13 = 0; integer13 < 3; ++integer13) {
                a2.setWithOffset(fx2, integer18 * gc4.getStepX(), integer13, integer18 * gc4.getStepZ());
                this.level.setBlock(a2, cee19, 18);
            }
        }
        return (Optional<BlockUtil.FoundRectangle>)Optional.of(new BlockUtil.FoundRectangle(fx2.immutable(), 2, 3));
    }
    
    private boolean canHostFrame(final BlockPos fx, final BlockPos.MutableBlockPos a, final Direction gc, final int integer) {
        final Direction gc2 = gc.getClockWise();
        for (int integer2 = -1; integer2 < 3; ++integer2) {
            for (int integer3 = -1; integer3 < 4; ++integer3) {
                a.setWithOffset(fx, gc.getStepX() * integer2 + gc2.getStepX() * integer, integer3, gc.getStepZ() * integer2 + gc2.getStepZ() * integer);
                if (integer3 < 0 && !this.level.getBlockState(a).getMaterial().isSolid()) {
                    return false;
                }
                if (integer3 >= 0 && !this.level.isEmptyBlock(a)) {
                    return false;
                }
            }
        }
        return true;
    }
}
