package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Iterator;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.RailShape;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class RailState {
    private final Level level;
    private final BlockPos pos;
    private final BaseRailBlock block;
    private BlockState state;
    private final boolean isStraight;
    private final List<BlockPos> connections;
    
    public RailState(final Level bru, final BlockPos fx, final BlockState cee) {
        this.connections = (List<BlockPos>)Lists.newArrayList();
        this.level = bru;
        this.pos = fx;
        this.state = cee;
        this.block = (BaseRailBlock)cee.getBlock();
        final RailShape cfh5 = cee.<RailShape>getValue(this.block.getShapeProperty());
        this.isStraight = this.block.isStraight();
        this.updateConnections(cfh5);
    }
    
    public List<BlockPos> getConnections() {
        return this.connections;
    }
    
    private void updateConnections(final RailShape cfh) {
        this.connections.clear();
        switch (cfh) {
            case NORTH_SOUTH: {
                this.connections.add(this.pos.north());
                this.connections.add(this.pos.south());
                break;
            }
            case EAST_WEST: {
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.east());
                break;
            }
            case ASCENDING_EAST: {
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.east().above());
                break;
            }
            case ASCENDING_WEST: {
                this.connections.add(this.pos.west().above());
                this.connections.add(this.pos.east());
                break;
            }
            case ASCENDING_NORTH: {
                this.connections.add(this.pos.north().above());
                this.connections.add(this.pos.south());
                break;
            }
            case ASCENDING_SOUTH: {
                this.connections.add(this.pos.north());
                this.connections.add(this.pos.south().above());
                break;
            }
            case SOUTH_EAST: {
                this.connections.add(this.pos.east());
                this.connections.add(this.pos.south());
                break;
            }
            case SOUTH_WEST: {
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.south());
                break;
            }
            case NORTH_WEST: {
                this.connections.add(this.pos.west());
                this.connections.add(this.pos.north());
                break;
            }
            case NORTH_EAST: {
                this.connections.add(this.pos.east());
                this.connections.add(this.pos.north());
                break;
            }
        }
    }
    
    private void removeSoftConnections() {
        for (int integer2 = 0; integer2 < this.connections.size(); ++integer2) {
            final RailState byy3 = this.getRail((BlockPos)this.connections.get(integer2));
            if (byy3 == null || !byy3.connectsTo(this)) {
                this.connections.remove(integer2--);
            }
            else {
                this.connections.set(integer2, byy3.pos);
            }
        }
    }
    
    private boolean hasRail(final BlockPos fx) {
        return BaseRailBlock.isRail(this.level, fx) || BaseRailBlock.isRail(this.level, fx.above()) || BaseRailBlock.isRail(this.level, fx.below());
    }
    
    @Nullable
    private RailState getRail(final BlockPos fx) {
        BlockPos fx2 = fx;
        BlockState cee4 = this.level.getBlockState(fx2);
        if (BaseRailBlock.isRail(cee4)) {
            return new RailState(this.level, fx2, cee4);
        }
        fx2 = fx.above();
        cee4 = this.level.getBlockState(fx2);
        if (BaseRailBlock.isRail(cee4)) {
            return new RailState(this.level, fx2, cee4);
        }
        fx2 = fx.below();
        cee4 = this.level.getBlockState(fx2);
        if (BaseRailBlock.isRail(cee4)) {
            return new RailState(this.level, fx2, cee4);
        }
        return null;
    }
    
    private boolean connectsTo(final RailState byy) {
        return this.hasConnection(byy.pos);
    }
    
    private boolean hasConnection(final BlockPos fx) {
        for (int integer3 = 0; integer3 < this.connections.size(); ++integer3) {
            final BlockPos fx2 = (BlockPos)this.connections.get(integer3);
            if (fx2.getX() == fx.getX() && fx2.getZ() == fx.getZ()) {
                return true;
            }
        }
        return false;
    }
    
    protected int countPotentialConnections() {
        int integer2 = 0;
        for (final Direction gc4 : Direction.Plane.HORIZONTAL) {
            if (this.hasRail(this.pos.relative(gc4))) {
                ++integer2;
            }
        }
        return integer2;
    }
    
    private boolean canConnectTo(final RailState byy) {
        return this.connectsTo(byy) || this.connections.size() != 2;
    }
    
    private void connectTo(final RailState byy) {
        this.connections.add(byy.pos);
        final BlockPos fx3 = this.pos.north();
        final BlockPos fx4 = this.pos.south();
        final BlockPos fx5 = this.pos.west();
        final BlockPos fx6 = this.pos.east();
        final boolean boolean7 = this.hasConnection(fx3);
        final boolean boolean8 = this.hasConnection(fx4);
        final boolean boolean9 = this.hasConnection(fx5);
        final boolean boolean10 = this.hasConnection(fx6);
        RailShape cfh11 = null;
        if (boolean7 || boolean8) {
            cfh11 = RailShape.NORTH_SOUTH;
        }
        if (boolean9 || boolean10) {
            cfh11 = RailShape.EAST_WEST;
        }
        if (!this.isStraight) {
            if (boolean8 && boolean10 && !boolean7 && !boolean9) {
                cfh11 = RailShape.SOUTH_EAST;
            }
            if (boolean8 && boolean9 && !boolean7 && !boolean10) {
                cfh11 = RailShape.SOUTH_WEST;
            }
            if (boolean7 && boolean9 && !boolean8 && !boolean10) {
                cfh11 = RailShape.NORTH_WEST;
            }
            if (boolean7 && boolean10 && !boolean8 && !boolean9) {
                cfh11 = RailShape.NORTH_EAST;
            }
        }
        if (cfh11 == RailShape.NORTH_SOUTH) {
            if (BaseRailBlock.isRail(this.level, fx3.above())) {
                cfh11 = RailShape.ASCENDING_NORTH;
            }
            if (BaseRailBlock.isRail(this.level, fx4.above())) {
                cfh11 = RailShape.ASCENDING_SOUTH;
            }
        }
        if (cfh11 == RailShape.EAST_WEST) {
            if (BaseRailBlock.isRail(this.level, fx6.above())) {
                cfh11 = RailShape.ASCENDING_EAST;
            }
            if (BaseRailBlock.isRail(this.level, fx5.above())) {
                cfh11 = RailShape.ASCENDING_WEST;
            }
        }
        if (cfh11 == null) {
            cfh11 = RailShape.NORTH_SOUTH;
        }
        this.state = ((StateHolder<O, BlockState>)this.state).<RailShape, RailShape>setValue(this.block.getShapeProperty(), cfh11);
        this.level.setBlock(this.pos, this.state, 3);
    }
    
    private boolean hasNeighborRail(final BlockPos fx) {
        final RailState byy3 = this.getRail(fx);
        if (byy3 == null) {
            return false;
        }
        byy3.removeSoftConnections();
        return byy3.canConnectTo(this);
    }
    
    public RailState place(final boolean boolean1, final boolean boolean2, final RailShape cfh) {
        final BlockPos fx5 = this.pos.north();
        final BlockPos fx6 = this.pos.south();
        final BlockPos fx7 = this.pos.west();
        final BlockPos fx8 = this.pos.east();
        final boolean boolean3 = this.hasNeighborRail(fx5);
        final boolean boolean4 = this.hasNeighborRail(fx6);
        final boolean boolean5 = this.hasNeighborRail(fx7);
        final boolean boolean6 = this.hasNeighborRail(fx8);
        RailShape cfh2 = null;
        final boolean boolean7 = boolean3 || boolean4;
        final boolean boolean8 = boolean5 || boolean6;
        if (boolean7 && !boolean8) {
            cfh2 = RailShape.NORTH_SOUTH;
        }
        if (boolean8 && !boolean7) {
            cfh2 = RailShape.EAST_WEST;
        }
        final boolean boolean9 = boolean4 && boolean6;
        final boolean boolean10 = boolean4 && boolean5;
        final boolean boolean11 = boolean3 && boolean6;
        final boolean boolean12 = boolean3 && boolean5;
        if (!this.isStraight) {
            if (boolean9 && !boolean3 && !boolean5) {
                cfh2 = RailShape.SOUTH_EAST;
            }
            if (boolean10 && !boolean3 && !boolean6) {
                cfh2 = RailShape.SOUTH_WEST;
            }
            if (boolean12 && !boolean4 && !boolean6) {
                cfh2 = RailShape.NORTH_WEST;
            }
            if (boolean11 && !boolean4 && !boolean5) {
                cfh2 = RailShape.NORTH_EAST;
            }
        }
        if (cfh2 == null) {
            if (boolean7 && boolean8) {
                cfh2 = cfh;
            }
            else if (boolean7) {
                cfh2 = RailShape.NORTH_SOUTH;
            }
            else if (boolean8) {
                cfh2 = RailShape.EAST_WEST;
            }
            if (!this.isStraight) {
                if (boolean1) {
                    if (boolean9) {
                        cfh2 = RailShape.SOUTH_EAST;
                    }
                    if (boolean10) {
                        cfh2 = RailShape.SOUTH_WEST;
                    }
                    if (boolean11) {
                        cfh2 = RailShape.NORTH_EAST;
                    }
                    if (boolean12) {
                        cfh2 = RailShape.NORTH_WEST;
                    }
                }
                else {
                    if (boolean12) {
                        cfh2 = RailShape.NORTH_WEST;
                    }
                    if (boolean11) {
                        cfh2 = RailShape.NORTH_EAST;
                    }
                    if (boolean10) {
                        cfh2 = RailShape.SOUTH_WEST;
                    }
                    if (boolean9) {
                        cfh2 = RailShape.SOUTH_EAST;
                    }
                }
            }
        }
        if (cfh2 == RailShape.NORTH_SOUTH) {
            if (BaseRailBlock.isRail(this.level, fx5.above())) {
                cfh2 = RailShape.ASCENDING_NORTH;
            }
            if (BaseRailBlock.isRail(this.level, fx6.above())) {
                cfh2 = RailShape.ASCENDING_SOUTH;
            }
        }
        if (cfh2 == RailShape.EAST_WEST) {
            if (BaseRailBlock.isRail(this.level, fx8.above())) {
                cfh2 = RailShape.ASCENDING_EAST;
            }
            if (BaseRailBlock.isRail(this.level, fx7.above())) {
                cfh2 = RailShape.ASCENDING_WEST;
            }
        }
        if (cfh2 == null) {
            cfh2 = cfh;
        }
        this.updateConnections(cfh2);
        this.state = ((StateHolder<O, BlockState>)this.state).<RailShape, RailShape>setValue(this.block.getShapeProperty(), cfh2);
        if (boolean2 || this.level.getBlockState(this.pos) != this.state) {
            this.level.setBlock(this.pos, this.state, 3);
            for (int integer20 = 0; integer20 < this.connections.size(); ++integer20) {
                final RailState byy21 = this.getRail((BlockPos)this.connections.get(integer20));
                if (byy21 != null) {
                    byy21.removeSoftConnections();
                    if (byy21.canConnectTo(this)) {
                        byy21.connectTo(this);
                    }
                }
            }
        }
        return this;
    }
    
    public BlockState getState() {
        return this.state;
    }
}
