package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.predicate.BlockMaterialPredicate;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.context.BlockPlaceContext;
import java.util.Iterator;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.LevelReader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.item.Wearable;

public class CarvedPumpkinBlock extends HorizontalDirectionalBlock implements Wearable {
    public static final DirectionProperty FACING;
    @Nullable
    private BlockPattern snowGolemBase;
    @Nullable
    private BlockPattern snowGolemFull;
    @Nullable
    private BlockPattern ironGolemBase;
    @Nullable
    private BlockPattern ironGolemFull;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE;
    
    protected CarvedPumpkinBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Direction>setValue((Property<Comparable>)CarvedPumpkinBlock.FACING, Direction.NORTH));
    }
    
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee4.is(cee1.getBlock())) {
            return;
        }
        this.trySpawnGolem(bru, fx);
    }
    
    public boolean canSpawnGolem(final LevelReader brw, final BlockPos fx) {
        return this.getOrCreateSnowGolemBase().find(brw, fx) != null || this.getOrCreateIronGolemBase().find(brw, fx) != null;
    }
    
    private void trySpawnGolem(final Level bru, final BlockPos fx) {
        BlockPattern.BlockPatternMatch b4 = this.getOrCreateSnowGolemFull().find(bru, fx);
        if (b4 != null) {
            for (int integer5 = 0; integer5 < this.getOrCreateSnowGolemFull().getHeight(); ++integer5) {
                final BlockInWorld cei6 = b4.getBlock(0, integer5, 0);
                bru.setBlock(cei6.getPos(), Blocks.AIR.defaultBlockState(), 2);
                bru.levelEvent(2001, cei6.getPos(), Block.getId(cei6.getState()));
            }
            final SnowGolem bar5 = EntityType.SNOW_GOLEM.create(bru);
            final BlockPos fx2 = b4.getBlock(0, 2, 0).getPos();
            bar5.moveTo(fx2.getX() + 0.5, fx2.getY() + 0.05, fx2.getZ() + 0.5, 0.0f, 0.0f);
            bru.addFreshEntity(bar5);
            for (final ServerPlayer aah8 : bru.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)ServerPlayer.class, bar5.getBoundingBox().inflate(5.0))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(aah8, bar5);
            }
            for (int integer6 = 0; integer6 < this.getOrCreateSnowGolemFull().getHeight(); ++integer6) {
                final BlockInWorld cei7 = b4.getBlock(0, integer6, 0);
                bru.blockUpdated(cei7.getPos(), Blocks.AIR);
            }
        }
        else {
            b4 = this.getOrCreateIronGolemFull().find(bru, fx);
            if (b4 != null) {
                for (int integer5 = 0; integer5 < this.getOrCreateIronGolemFull().getWidth(); ++integer5) {
                    for (int integer7 = 0; integer7 < this.getOrCreateIronGolemFull().getHeight(); ++integer7) {
                        final BlockInWorld cei8 = b4.getBlock(integer5, integer7, 0);
                        bru.setBlock(cei8.getPos(), Blocks.AIR.defaultBlockState(), 2);
                        bru.levelEvent(2001, cei8.getPos(), Block.getId(cei8.getState()));
                    }
                }
                final BlockPos fx3 = b4.getBlock(1, 2, 0).getPos();
                final IronGolem baf6 = EntityType.IRON_GOLEM.create(bru);
                baf6.setPlayerCreated(true);
                baf6.moveTo(fx3.getX() + 0.5, fx3.getY() + 0.05, fx3.getZ() + 0.5, 0.0f, 0.0f);
                bru.addFreshEntity(baf6);
                for (final ServerPlayer aah8 : bru.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)ServerPlayer.class, baf6.getBoundingBox().inflate(5.0))) {
                    CriteriaTriggers.SUMMONED_ENTITY.trigger(aah8, baf6);
                }
                for (int integer6 = 0; integer6 < this.getOrCreateIronGolemFull().getWidth(); ++integer6) {
                    for (int integer8 = 0; integer8 < this.getOrCreateIronGolemFull().getHeight(); ++integer8) {
                        final BlockInWorld cei9 = b4.getBlock(integer6, integer8, 0);
                        bru.blockUpdated(cei9.getPos(), Blocks.AIR);
                    }
                }
            }
        }
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)CarvedPumpkinBlock.FACING, bnv.getHorizontalDirection().getOpposite());
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CarvedPumpkinBlock.FACING);
    }
    
    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start().aisle(" ", "#", "#").where('#', BlockInWorld.hasState((Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemBase;
    }
    
    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle("^", "#", "#").where('^', BlockInWorld.hasState(CarvedPumpkinBlock.PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.SNOW_BLOCK))).build();
        }
        return this.snowGolemFull;
    }
    
    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', BlockInWorld.hasState((Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState((Predicate<BlockState>)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.ironGolemBase;
    }
    
    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', BlockInWorld.hasState(CarvedPumpkinBlock.PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState((Predicate<BlockState>)BlockStatePredicate.forBlock(Blocks.IRON_BLOCK))).where('~', BlockInWorld.hasState((Predicate<BlockState>)BlockMaterialPredicate.forMaterial(Material.AIR))).build();
        }
        return this.ironGolemFull;
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        PUMPKINS_PREDICATE = (cee -> cee != null && (cee.is(Blocks.CARVED_PUMPKIN) || cee.is(Blocks.JACK_O_LANTERN)));
    }
}
