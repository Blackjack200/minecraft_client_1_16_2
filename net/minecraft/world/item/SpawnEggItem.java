package net.minecraft.world.item;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.AgableMob;
import java.util.Optional;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Mob;
import com.google.common.collect.Iterables;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.core.Direction;
import java.util.Objects;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.EntityType;
import java.util.Map;

public class SpawnEggItem extends Item {
    private static final Map<EntityType<?>, SpawnEggItem> BY_ID;
    private final int color1;
    private final int color2;
    private final EntityType<?> defaultType;
    
    public SpawnEggItem(final EntityType<?> aqb, final int integer2, final int integer3, final Properties a) {
        super(a);
        this.defaultType = aqb;
        this.color1 = integer2;
        this.color2 = integer3;
        SpawnEggItem.BY_ID.put(aqb, this);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        if (!(bru3 instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        final ItemStack bly4 = bnx.getItemInHand();
        final BlockPos fx5 = bnx.getClickedPos();
        final Direction gc6 = bnx.getClickedFace();
        final BlockState cee7 = bru3.getBlockState(fx5);
        if (cee7.is(Blocks.SPAWNER)) {
            final BlockEntity ccg8 = bru3.getBlockEntity(fx5);
            if (ccg8 instanceof SpawnerBlockEntity) {
                final BaseSpawner bqw9 = ((SpawnerBlockEntity)ccg8).getSpawner();
                final EntityType<?> aqb10 = this.getType(bly4.getTag());
                bqw9.setEntityId(aqb10);
                ccg8.setChanged();
                bru3.sendBlockUpdated(fx5, cee7, cee7, 3);
                bly4.shrink(1);
                return InteractionResult.CONSUME;
            }
        }
        BlockPos fx6;
        if (cee7.getCollisionShape(bru3, fx5).isEmpty()) {
            fx6 = fx5;
        }
        else {
            fx6 = fx5.relative(gc6);
        }
        final EntityType<?> aqb11 = this.getType(bly4.getTag());
        if (aqb11.spawn((ServerLevel)bru3, bly4, bnx.getPlayer(), fx6, MobSpawnType.SPAWN_EGG, true, !Objects.equals(fx5, fx6) && gc6 == Direction.UP) != null) {
            bly4.shrink(1);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final HitResult dci6 = Item.getPlayerPOVHitResult(bru, bft, ClipContext.Fluid.SOURCE_ONLY);
        if (dci6.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        if (!(bru instanceof ServerLevel)) {
            return InteractionResultHolder.<ItemStack>success(bly5);
        }
        final BlockHitResult dcg7 = (BlockHitResult)dci6;
        final BlockPos fx8 = dcg7.getBlockPos();
        if (!(bru.getBlockState(fx8).getBlock() instanceof LiquidBlock)) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        if (!bru.mayInteract(bft, fx8) || !bft.mayUseItemAt(fx8, dcg7.getDirection(), bly5)) {
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        final EntityType<?> aqb9 = this.getType(bly5.getTag());
        if (aqb9.spawn((ServerLevel)bru, bly5, bft, fx8, MobSpawnType.SPAWN_EGG, false, false) == null) {
            return InteractionResultHolder.<ItemStack>pass(bly5);
        }
        if (!bft.abilities.instabuild) {
            bly5.shrink(1);
        }
        bft.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.<ItemStack>consume(bly5);
    }
    
    public boolean spawnsEntity(@Nullable final CompoundTag md, final EntityType<?> aqb) {
        return Objects.equals(this.getType(md), aqb);
    }
    
    public int getColor(final int integer) {
        return (integer == 0) ? this.color1 : this.color2;
    }
    
    @Nullable
    public static SpawnEggItem byId(@Nullable final EntityType<?> aqb) {
        return (SpawnEggItem)SpawnEggItem.BY_ID.get(aqb);
    }
    
    public static Iterable<SpawnEggItem> eggs() {
        return (Iterable<SpawnEggItem>)Iterables.unmodifiableIterable((Iterable)SpawnEggItem.BY_ID.values());
    }
    
    public EntityType<?> getType(@Nullable final CompoundTag md) {
        if (md != null && md.contains("EntityTag", 10)) {
            final CompoundTag md2 = md.getCompound("EntityTag");
            if (md2.contains("id", 8)) {
                return EntityType.byString(md2.getString("id")).orElse(this.defaultType);
            }
        }
        return this.defaultType;
    }
    
    public Optional<Mob> spawnOffspringFromSpawnEgg(final Player bft, final Mob aqk, final EntityType<? extends Mob> aqb, final ServerLevel aag, final Vec3 dck, final ItemStack bly) {
        if (!this.spawnsEntity(bly.getTag(), aqb)) {
            return (Optional<Mob>)Optional.empty();
        }
        Mob aqk2;
        if (aqk instanceof AgableMob) {
            aqk2 = ((AgableMob)aqk).getBreedOffspring(aag, (AgableMob)aqk);
        }
        else {
            aqk2 = (Mob)aqb.create(aag);
        }
        if (aqk2 == null) {
            return (Optional<Mob>)Optional.empty();
        }
        aqk2.setBaby(true);
        if (!aqk2.isBaby()) {
            return (Optional<Mob>)Optional.empty();
        }
        aqk2.moveTo(dck.x(), dck.y(), dck.z(), 0.0f, 0.0f);
        aag.addFreshEntityWithPassengers(aqk2);
        if (bly.hasCustomHoverName()) {
            aqk2.setCustomName(bly.getHoverName());
        }
        if (!bft.abilities.instabuild) {
            bly.shrink(1);
        }
        return (Optional<Mob>)Optional.of(aqk2);
    }
    
    static {
        BY_ID = (Map)Maps.newIdentityHashMap();
    }
}
