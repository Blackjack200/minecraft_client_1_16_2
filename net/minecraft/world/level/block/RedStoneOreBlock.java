package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedStoneOreBlock extends Block {
    public static final BooleanProperty LIT;
    
    public RedStoneOreBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)RedStoneOreBlock.LIT, false));
    }
    
    @Override
    public void attack(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        interact(cee, bru, fx);
        super.attack(cee, bru, fx, bft);
    }
    
    @Override
    public void stepOn(final Level bru, final BlockPos fx, final Entity apx) {
        interact(bru.getBlockState(fx), bru, fx);
        super.stepOn(bru, fx, apx);
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            spawnParticles(bru, fx);
        }
        else {
            interact(cee, bru, fx);
        }
        final ItemStack bly8 = bft.getItemInHand(aoq);
        if (bly8.getItem() instanceof BlockItem && new BlockPlaceContext(bft, aoq, bly8, dcg).canPlace()) {
            return InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
    
    private static void interact(final BlockState cee, final Level bru, final BlockPos fx) {
        spawnParticles(bru, fx);
        if (!cee.<Boolean>getValue((Property<Boolean>)RedStoneOreBlock.LIT)) {
            bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)RedStoneOreBlock.LIT, true), 3);
        }
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return cee.<Boolean>getValue((Property<Boolean>)RedStoneOreBlock.LIT);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedStoneOreBlock.LIT)) {
            aag.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)RedStoneOreBlock.LIT, false), 3);
        }
    }
    
    @Override
    public void spawnAfterBreak(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
        super.spawnAfterBreak(cee, aag, fx, bly);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, bly) == 0) {
            final int integer6 = 1 + aag.random.nextInt(5);
            this.popExperience(aag, fx, integer6);
        }
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (cee.<Boolean>getValue((Property<Boolean>)RedStoneOreBlock.LIT)) {
            spawnParticles(bru, fx);
        }
    }
    
    private static void spawnParticles(final Level bru, final BlockPos fx) {
        final double double3 = 0.5625;
        final Random random5 = bru.random;
        for (final Direction gc9 : Direction.values()) {
            final BlockPos fx2 = fx.relative(gc9);
            if (!bru.getBlockState(fx2).isSolidRender(bru, fx2)) {
                final Direction.Axis a11 = gc9.getAxis();
                final double double4 = (a11 == Direction.Axis.X) ? (0.5 + 0.5625 * gc9.getStepX()) : random5.nextFloat();
                final double double5 = (a11 == Direction.Axis.Y) ? (0.5 + 0.5625 * gc9.getStepY()) : random5.nextFloat();
                final double double6 = (a11 == Direction.Axis.Z) ? (0.5 + 0.5625 * gc9.getStepZ()) : random5.nextFloat();
                bru.addParticle(DustParticleOptions.REDSTONE, fx.getX() + double4, fx.getY() + double5, fx.getZ() + double6, 0.0, 0.0, 0.0);
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(RedStoneOreBlock.LIT);
    }
    
    static {
        LIT = RedstoneTorchBlock.LIT;
    }
}
