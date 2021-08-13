package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.tags.Tag;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ChorusFlowerBlock extends Block {
    public static final IntegerProperty AGE;
    private final ChorusPlantBlock plant;
    
    protected ChorusFlowerBlock(final ChorusPlantBlock bvd, final Properties c) {
        super(c);
        this.plant = bvd;
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)ChorusFlowerBlock.AGE, 0));
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!cee.canSurvive(aag, fx)) {
            aag.destroyBlock(fx, true);
        }
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)ChorusFlowerBlock.AGE) < 5;
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        final BlockPos fx2 = fx.above();
        if (!aag.isEmptyBlock(fx2) || fx2.getY() >= 256) {
            return;
        }
        final int integer7 = cee.<Integer>getValue((Property<Integer>)ChorusFlowerBlock.AGE);
        if (integer7 >= 5) {
            return;
        }
        boolean boolean8 = false;
        boolean boolean9 = false;
        final BlockState cee2 = aag.getBlockState(fx.below());
        final Block bul11 = cee2.getBlock();
        if (bul11 == Blocks.END_STONE) {
            boolean8 = true;
        }
        else if (bul11 == this.plant) {
            int integer8 = 1;
            int integer9 = 0;
            while (integer9 < 4) {
                final Block bul12 = aag.getBlockState(fx.below(integer8 + 1)).getBlock();
                if (bul12 == this.plant) {
                    ++integer8;
                    ++integer9;
                }
                else {
                    if (bul12 == Blocks.END_STONE) {
                        boolean9 = true;
                        break;
                    }
                    break;
                }
            }
            if (integer8 < 2 || integer8 <= random.nextInt(boolean9 ? 5 : 4)) {
                boolean8 = true;
            }
        }
        else if (cee2.isAir()) {
            boolean8 = true;
        }
        if (boolean8 && allNeighborsEmpty(aag, fx2, null) && aag.isEmptyBlock(fx.above(2))) {
            aag.setBlock(fx, this.plant.getStateForPlacement(aag, fx), 2);
            this.placeGrownFlower(aag, fx2, integer7);
        }
        else if (integer7 < 4) {
            int integer8 = random.nextInt(4);
            if (boolean9) {
                ++integer8;
            }
            boolean boolean10 = false;
            for (int integer10 = 0; integer10 < integer8; ++integer10) {
                final Direction gc15 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                final BlockPos fx3 = fx.relative(gc15);
                if (aag.isEmptyBlock(fx3) && aag.isEmptyBlock(fx3.below()) && allNeighborsEmpty(aag, fx3, gc15.getOpposite())) {
                    this.placeGrownFlower(aag, fx3, integer7 + 1);
                    boolean10 = true;
                }
            }
            if (boolean10) {
                aag.setBlock(fx, this.plant.getStateForPlacement(aag, fx), 2);
            }
            else {
                this.placeDeadFlower(aag, fx);
            }
        }
        else {
            this.placeDeadFlower(aag, fx);
        }
    }
    
    private void placeGrownFlower(final Level bru, final BlockPos fx, final int integer) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)ChorusFlowerBlock.AGE, integer), 2);
        bru.levelEvent(1033, fx, 0);
    }
    
    private void placeDeadFlower(final Level bru, final BlockPos fx) {
        bru.setBlock(fx, ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)ChorusFlowerBlock.AGE, 5), 2);
        bru.levelEvent(1034, fx, 0);
    }
    
    private static boolean allNeighborsEmpty(final LevelReader brw, final BlockPos fx, @Nullable final Direction gc) {
        for (final Direction gc2 : Direction.Plane.HORIZONTAL) {
            if (gc2 != gc && !brw.isEmptyBlock(fx.relative(gc2))) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc != Direction.UP && !cee1.canSurvive(brv, fx5)) {
            brv.getBlockTicks().scheduleTick(fx5, this, 1);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final BlockState cee2 = brw.getBlockState(fx.below());
        if (cee2.getBlock() == this.plant || cee2.is(Blocks.END_STONE)) {
            return true;
        }
        if (!cee2.isAir()) {
            return false;
        }
        boolean boolean6 = false;
        for (final Direction gc8 : Direction.Plane.HORIZONTAL) {
            final BlockState cee3 = brw.getBlockState(fx.relative(gc8));
            if (cee3.is(this.plant)) {
                if (boolean6) {
                    return false;
                }
                boolean6 = true;
            }
            else {
                if (!cee3.isAir()) {
                    return false;
                }
                continue;
            }
        }
        return boolean6;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ChorusFlowerBlock.AGE);
    }
    
    public static void generatePlant(final LevelAccessor brv, final BlockPos fx, final Random random, final int integer) {
        brv.setBlock(fx, ((ChorusPlantBlock)Blocks.CHORUS_PLANT).getStateForPlacement(brv, fx), 2);
        growTreeRecursive(brv, fx, random, fx, integer, 0);
    }
    
    private static void growTreeRecursive(final LevelAccessor brv, final BlockPos fx2, final Random random, final BlockPos fx4, final int integer5, final int integer6) {
        final ChorusPlantBlock bvd7 = (ChorusPlantBlock)Blocks.CHORUS_PLANT;
        int integer7 = random.nextInt(4) + 1;
        if (integer6 == 0) {
            ++integer7;
        }
        for (int integer8 = 0; integer8 < integer7; ++integer8) {
            final BlockPos fx5 = fx2.above(integer8 + 1);
            if (!allNeighborsEmpty(brv, fx5, null)) {
                return;
            }
            brv.setBlock(fx5, bvd7.getStateForPlacement(brv, fx5), 2);
            brv.setBlock(fx5.below(), bvd7.getStateForPlacement(brv, fx5.below()), 2);
        }
        boolean boolean9 = false;
        if (integer6 < 4) {
            int integer9 = random.nextInt(4);
            if (integer6 == 0) {
                ++integer9;
            }
            for (int integer10 = 0; integer10 < integer9; ++integer10) {
                final Direction gc12 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                final BlockPos fx6 = fx2.above(integer7).relative(gc12);
                if (Math.abs(fx6.getX() - fx4.getX()) < integer5) {
                    if (Math.abs(fx6.getZ() - fx4.getZ()) < integer5) {
                        if (brv.isEmptyBlock(fx6) && brv.isEmptyBlock(fx6.below()) && allNeighborsEmpty(brv, fx6, gc12.getOpposite())) {
                            boolean9 = true;
                            brv.setBlock(fx6, bvd7.getStateForPlacement(brv, fx6), 2);
                            brv.setBlock(fx6.relative(gc12.getOpposite()), bvd7.getStateForPlacement(brv, fx6.relative(gc12.getOpposite())), 2);
                            growTreeRecursive(brv, fx6, random, fx4, integer5, integer6 + 1);
                        }
                    }
                }
            }
        }
        if (!boolean9) {
            brv.setBlock(fx2.above(integer7), ((StateHolder<O, BlockState>)Blocks.CHORUS_FLOWER.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)ChorusFlowerBlock.AGE, 5), 2);
        }
    }
    
    @Override
    public void onProjectileHit(final Level bru, final BlockState cee, final BlockHitResult dcg, final Projectile bgj) {
        if (bgj.getType().is(EntityTypeTags.IMPACT_PROJECTILES)) {
            final BlockPos fx6 = dcg.getBlockPos();
            bru.destroyBlock(fx6, true, bgj);
        }
    }
    
    static {
        AGE = BlockStateProperties.AGE_5;
    }
}
