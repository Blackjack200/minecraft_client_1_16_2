package net.minecraft.world.level.portal;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;
import net.minecraft.BlockUtil;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Vec3i;
import java.util.function.Predicate;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PortalShape {
    private static final BlockBehaviour.StatePredicate FRAME;
    private final LevelAccessor level;
    private final Direction.Axis axis;
    private final Direction rightDir;
    private int numPortalBlocks;
    @Nullable
    private BlockPos bottomLeft;
    private int height;
    private int width;
    
    public static Optional<PortalShape> findEmptyPortalShape(final LevelAccessor brv, final BlockPos fx, final Direction.Axis a) {
        return findPortalShape(brv, fx, (Predicate<PortalShape>)(cxk -> cxk.isValid() && cxk.numPortalBlocks == 0), a);
    }
    
    public static Optional<PortalShape> findPortalShape(final LevelAccessor brv, final BlockPos fx, final Predicate<PortalShape> predicate, final Direction.Axis a) {
        final Optional<PortalShape> optional5 = (Optional<PortalShape>)Optional.of(new PortalShape(brv, fx, a)).filter((Predicate)predicate);
        if (optional5.isPresent()) {
            return optional5;
        }
        final Direction.Axis a2 = (a == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        return (Optional<PortalShape>)Optional.of(new PortalShape(brv, fx, a2)).filter((Predicate)predicate);
    }
    
    public PortalShape(final LevelAccessor brv, final BlockPos fx, final Direction.Axis a) {
        this.level = brv;
        this.axis = a;
        this.rightDir = ((a == Direction.Axis.X) ? Direction.WEST : Direction.SOUTH);
        this.bottomLeft = this.calculateBottomLeft(fx);
        if (this.bottomLeft == null) {
            this.bottomLeft = fx;
            this.width = 1;
            this.height = 1;
        }
        else {
            this.width = this.calculateWidth();
            if (this.width > 0) {
                this.height = this.calculateHeight();
            }
        }
    }
    
    @Nullable
    private BlockPos calculateBottomLeft(BlockPos fx) {
        for (int integer3 = Math.max(0, fx.getY() - 21); fx.getY() > integer3 && isEmpty(this.level.getBlockState(fx.below())); fx = fx.below()) {}
        final Direction gc4 = this.rightDir.getOpposite();
        final int integer4 = this.getDistanceUntilEdgeAboveFrame(fx, gc4) - 1;
        if (integer4 < 0) {
            return null;
        }
        return fx.relative(gc4, integer4);
    }
    
    private int calculateWidth() {
        final int integer2 = this.getDistanceUntilEdgeAboveFrame(this.bottomLeft, this.rightDir);
        if (integer2 < 2 || integer2 > 21) {
            return 0;
        }
        return integer2;
    }
    
    private int getDistanceUntilEdgeAboveFrame(final BlockPos fx, final Direction gc) {
        final BlockPos.MutableBlockPos a4 = new BlockPos.MutableBlockPos();
        int integer5 = 0;
        while (integer5 <= 21) {
            a4.set(fx).move(gc, integer5);
            final BlockState cee6 = this.level.getBlockState(a4);
            if (!isEmpty(cee6)) {
                if (PortalShape.FRAME.test(cee6, this.level, a4)) {
                    return integer5;
                }
                break;
            }
            else {
                final BlockState cee7 = this.level.getBlockState(a4.move(Direction.DOWN));
                if (!PortalShape.FRAME.test(cee7, this.level, a4)) {
                    break;
                }
                ++integer5;
            }
        }
        return 0;
    }
    
    private int calculateHeight() {
        final BlockPos.MutableBlockPos a2 = new BlockPos.MutableBlockPos();
        final int integer3 = this.getDistanceUntilTop(a2);
        if (integer3 < 3 || integer3 > 21 || !this.hasTopFrame(a2, integer3)) {
            return 0;
        }
        return integer3;
    }
    
    private boolean hasTopFrame(final BlockPos.MutableBlockPos a, final int integer) {
        for (int integer2 = 0; integer2 < this.width; ++integer2) {
            final BlockPos.MutableBlockPos a2 = a.set(this.bottomLeft).move(Direction.UP, integer).move(this.rightDir, integer2);
            if (!PortalShape.FRAME.test(this.level.getBlockState(a2), this.level, a2)) {
                return false;
            }
        }
        return true;
    }
    
    private int getDistanceUntilTop(final BlockPos.MutableBlockPos a) {
        for (int integer3 = 0; integer3 < 21; ++integer3) {
            a.set(this.bottomLeft).move(Direction.UP, integer3).move(this.rightDir, -1);
            if (!PortalShape.FRAME.test(this.level.getBlockState(a), this.level, a)) {
                return integer3;
            }
            a.set(this.bottomLeft).move(Direction.UP, integer3).move(this.rightDir, this.width);
            if (!PortalShape.FRAME.test(this.level.getBlockState(a), this.level, a)) {
                return integer3;
            }
            for (int integer4 = 0; integer4 < this.width; ++integer4) {
                a.set(this.bottomLeft).move(Direction.UP, integer3).move(this.rightDir, integer4);
                final BlockState cee5 = this.level.getBlockState(a);
                if (!isEmpty(cee5)) {
                    return integer3;
                }
                if (cee5.is(Blocks.NETHER_PORTAL)) {
                    ++this.numPortalBlocks;
                }
            }
        }
        return 21;
    }
    
    private static boolean isEmpty(final BlockState cee) {
        return cee.isAir() || cee.is(BlockTags.FIRE) || cee.is(Blocks.NETHER_PORTAL);
    }
    
    public boolean isValid() {
        return this.bottomLeft != null && this.width >= 2 && this.width <= 21 && this.height >= 3 && this.height <= 21;
    }
    
    public void createPortalBlocks() {
        final BlockState cee2 = ((StateHolder<O, BlockState>)Blocks.NETHER_PORTAL.defaultBlockState()).<Direction.Axis, Direction.Axis>setValue(NetherPortalBlock.AXIS, this.axis);
        BlockPos.betweenClosed(this.bottomLeft, this.bottomLeft.relative(Direction.UP, this.height - 1).relative(this.rightDir, this.width - 1)).forEach(fx -> this.level.setBlock(fx, cee2, 18));
    }
    
    public boolean isComplete() {
        return this.isValid() && this.numPortalBlocks == this.width * this.height;
    }
    
    public static Vec3 getRelativePosition(final BlockUtil.FoundRectangle a, final Direction.Axis a, final Vec3 dck, final EntityDimensions apy) {
        final double double5 = a.axis1Size - (double)apy.width;
        final double double6 = a.axis2Size - (double)apy.height;
        final BlockPos fx9 = a.minCorner;
        double double7;
        if (double5 > 0.0) {
            final float float12 = fx9.get(a) + apy.width / 2.0f;
            double7 = Mth.clamp(Mth.inverseLerp(dck.get(a) - float12, 0.0, double5), 0.0, 1.0);
        }
        else {
            double7 = 0.5;
        }
        double double8;
        if (double6 > 0.0) {
            final Direction.Axis a2 = Direction.Axis.Y;
            double8 = Mth.clamp(Mth.inverseLerp(dck.get(a2) - fx9.get(a2), 0.0, double6), 0.0, 1.0);
        }
        else {
            double8 = 0.0;
        }
        final Direction.Axis a2 = (a == Direction.Axis.X) ? Direction.Axis.Z : Direction.Axis.X;
        final double double9 = dck.get(a2) - (fx9.get(a2) + 0.5);
        return new Vec3(double7, double8, double9);
    }
    
    public static PortalInfo createPortalInfo(final ServerLevel aag, final BlockUtil.FoundRectangle a, final Direction.Axis a, final Vec3 dck4, final EntityDimensions apy, final Vec3 dck6, final float float7, final float float8) {
        final BlockPos fx9 = a.minCorner;
        final BlockState cee10 = aag.getBlockState(fx9);
        final Direction.Axis a2 = cee10.<Direction.Axis>getValue(BlockStateProperties.HORIZONTAL_AXIS);
        final double double12 = a.axis1Size;
        final double double13 = a.axis2Size;
        final int integer16 = (a == a2) ? 0 : 90;
        final Vec3 dck7 = (a == a2) ? dck6 : new Vec3(dck6.z, dck6.y, -dck6.x);
        final double double14 = apy.width / 2.0 + (double12 - apy.width) * dck4.x();
        final double double15 = (double13 - apy.height) * dck4.y();
        final double double16 = 0.5 + dck4.z();
        final boolean boolean24 = a2 == Direction.Axis.X;
        final Vec3 dck8 = new Vec3(fx9.getX() + (boolean24 ? double14 : double16), fx9.getY() + double15, fx9.getZ() + (boolean24 ? double16 : double14));
        return new PortalInfo(dck8, dck7, float7 + integer16, float8);
    }
    
    static {
        FRAME = ((cee, bqz, fx) -> cee.is(Blocks.OBSIDIAN));
    }
}
