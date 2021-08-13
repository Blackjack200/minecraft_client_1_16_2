package net.minecraft.world.level.block;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.Nameable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnchantmentTableBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE;
    
    protected EnchantmentTableBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return EnchantmentTableBlock.SHAPE;
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        super.animateTick(cee, bru, fx, random);
        for (int integer6 = -2; integer6 <= 2; ++integer6) {
            for (int integer7 = -2; integer7 <= 2; ++integer7) {
                if (integer6 > -2 && integer6 < 2 && integer7 == -1) {
                    integer7 = 2;
                }
                if (random.nextInt(16) == 0) {
                    for (int integer8 = 0; integer8 <= 1; ++integer8) {
                        final BlockPos fx2 = fx.offset(integer6, integer8, integer7);
                        if (bru.getBlockState(fx2).is(Blocks.BOOKSHELF)) {
                            if (!bru.isEmptyBlock(fx.offset(integer6 / 2, 0, integer7 / 2))) {
                                break;
                            }
                            bru.addParticle(ParticleTypes.ENCHANT, fx.getX() + 0.5, fx.getY() + 2.0, fx.getZ() + 0.5, integer6 + random.nextFloat() - 0.5, integer8 - random.nextFloat() - 1.0f, integer7 + random.nextFloat() - 0.5);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new EnchantmentTableBlockEntity();
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof EnchantmentTableBlockEntity) {
            final Component nr6 = ((Nameable)ccg5).getDisplayName();
            return new SimpleMenuProvider((integer, bfs, bft) -> new EnchantmentMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), nr6);
        }
        return null;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof EnchantmentTableBlockEntity) {
                ((EnchantmentTableBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    }
}
