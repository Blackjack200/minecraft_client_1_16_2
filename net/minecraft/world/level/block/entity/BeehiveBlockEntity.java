package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.EntityTypeTags;
import java.util.function.Function;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.entity.player.Player;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.List;

public class BeehiveBlockEntity extends BlockEntity implements TickableBlockEntity {
    private final List<BeeData> stored;
    @Nullable
    private BlockPos savedFlowerPos;
    
    public BeehiveBlockEntity() {
        super(BlockEntityType.BEEHIVE);
        this.stored = (List<BeeData>)Lists.newArrayList();
        this.savedFlowerPos = null;
    }
    
    @Override
    public void setChanged() {
        if (this.isFireNearby()) {
            this.emptyAllLivingFromHive(null, this.level.getBlockState(this.getBlockPos()), BeeReleaseStatus.EMERGENCY);
        }
        super.setChanged();
    }
    
    public boolean isFireNearby() {
        if (this.level == null) {
            return false;
        }
        for (final BlockPos fx3 : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
            if (this.level.getBlockState(fx3).getBlock() instanceof FireBlock) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEmpty() {
        return this.stored.isEmpty();
    }
    
    public boolean isFull() {
        return this.stored.size() == 3;
    }
    
    public void emptyAllLivingFromHive(@Nullable final Player bft, final BlockState cee, final BeeReleaseStatus b) {
        final List<Entity> list5 = this.releaseAllOccupants(cee, b);
        if (bft != null) {
            for (final Entity apx7 : list5) {
                if (apx7 instanceof Bee) {
                    final Bee azx8 = (Bee)apx7;
                    if (bft.position().distanceToSqr(apx7.position()) > 16.0) {
                        continue;
                    }
                    if (!this.isSedated()) {
                        azx8.setTarget(bft);
                    }
                    else {
                        azx8.setStayOutOfHiveCountdown(400);
                    }
                }
            }
        }
    }
    
    private List<Entity> releaseAllOccupants(final BlockState cee, final BeeReleaseStatus b) {
        final List<Entity> list4 = (List<Entity>)Lists.newArrayList();
        this.stored.removeIf(a -> this.releaseOccupant(cee, a, list4, b));
        return list4;
    }
    
    public void addOccupant(final Entity apx, final boolean boolean2) {
        this.addOccupantWithPresetTicks(apx, boolean2, 0);
    }
    
    public int getOccupantCount() {
        return this.stored.size();
    }
    
    public static int getHoneyLevel(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)BeehiveBlock.HONEY_LEVEL);
    }
    
    public boolean isSedated() {
        return CampfireBlock.isSmokeyPos(this.level, this.getBlockPos());
    }
    
    protected void sendDebugPackets() {
        DebugPackets.sendHiveInfo(this);
    }
    
    public void addOccupantWithPresetTicks(final Entity apx, final boolean boolean2, final int integer) {
        if (this.stored.size() >= 3) {
            return;
        }
        apx.stopRiding();
        apx.ejectPassengers();
        final CompoundTag md5 = new CompoundTag();
        apx.save(md5);
        this.stored.add(new BeeData(md5, integer, boolean2 ? 2400 : 600));
        if (this.level != null) {
            if (apx instanceof Bee) {
                final Bee azx6 = (Bee)apx;
                if (azx6.hasSavedFlowerPos() && (!this.hasSavedFlowerPos() || this.level.random.nextBoolean())) {
                    this.savedFlowerPos = azx6.getSavedFlowerPos();
                }
            }
            final BlockPos fx6 = this.getBlockPos();
            this.level.playSound(null, fx6.getX(), fx6.getY(), fx6.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        apx.remove();
    }
    
    private boolean releaseOccupant(final BlockState cee, final BeeData a, @Nullable final List<Entity> list, final BeeReleaseStatus b) {
        if ((this.level.isNight() || this.level.isRaining()) && b != BeeReleaseStatus.EMERGENCY) {
            return false;
        }
        final BlockPos fx6 = this.getBlockPos();
        final CompoundTag md7 = a.entityData;
        md7.remove("Passengers");
        md7.remove("Leash");
        md7.remove("UUID");
        final Direction gc8 = cee.<Direction>getValue((Property<Direction>)BeehiveBlock.FACING);
        final BlockPos fx7 = fx6.relative(gc8);
        final boolean boolean10 = !this.level.getBlockState(fx7).getCollisionShape(this.level, fx7).isEmpty();
        if (boolean10 && b != BeeReleaseStatus.EMERGENCY) {
            return false;
        }
        final Entity apx11 = EntityType.loadEntityRecursive(md7, this.level, (Function<Entity, Entity>)(apx -> apx));
        if (apx11 == null) {
            return false;
        }
        if (!apx11.getType().is(EntityTypeTags.BEEHIVE_INHABITORS)) {
            return false;
        }
        if (apx11 instanceof Bee) {
            final Bee azx12 = (Bee)apx11;
            if (this.hasSavedFlowerPos() && !azx12.hasSavedFlowerPos() && this.level.random.nextFloat() < 0.9f) {
                azx12.setSavedFlowerPos(this.savedFlowerPos);
            }
            if (b == BeeReleaseStatus.HONEY_DELIVERED) {
                azx12.dropOffNectar();
                if (cee.getBlock().is(BlockTags.BEEHIVES)) {
                    final int integer13 = getHoneyLevel(cee);
                    if (integer13 < 5) {
                        int integer14 = (this.level.random.nextInt(100) == 0) ? 2 : 1;
                        if (integer13 + integer14 > 5) {
                            --integer14;
                        }
                        this.level.setBlockAndUpdate(this.getBlockPos(), ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)BeehiveBlock.HONEY_LEVEL, integer13 + integer14));
                    }
                }
            }
            this.setBeeReleaseData(a.ticksInHive, azx12);
            if (list != null) {
                list.add(azx12);
            }
            final float float13 = apx11.getBbWidth();
            final double double14 = boolean10 ? 0.0 : (0.55 + float13 / 2.0f);
            final double double15 = fx6.getX() + 0.5 + double14 * gc8.getStepX();
            final double double16 = fx6.getY() + 0.5 - apx11.getBbHeight() / 2.0f;
            final double double17 = fx6.getZ() + 0.5 + double14 * gc8.getStepZ();
            apx11.moveTo(double15, double16, double17, apx11.yRot, apx11.xRot);
        }
        this.level.playSound(null, fx6, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0f, 1.0f);
        return this.level.addFreshEntity(apx11);
    }
    
    private void setBeeReleaseData(final int integer, final Bee azx) {
        final int integer2 = azx.getAge();
        if (integer2 < 0) {
            azx.setAge(Math.min(0, integer2 + integer));
        }
        else if (integer2 > 0) {
            azx.setAge(Math.max(0, integer2 - integer));
        }
        azx.setInLoveTime(Math.max(0, azx.getInLoveTime() - integer));
        azx.resetTicksWithoutNectarSinceExitingHive();
    }
    
    private boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }
    
    private void tickOccupants() {
        final Iterator<BeeData> iterator2 = (Iterator<BeeData>)this.stored.iterator();
        final BlockState cee3 = this.getBlockState();
        while (iterator2.hasNext()) {
            final BeeData a4 = (BeeData)iterator2.next();
            if (a4.ticksInHive > a4.minOccupationTicks) {
                final BeeReleaseStatus b5 = a4.entityData.getBoolean("HasNectar") ? BeeReleaseStatus.HONEY_DELIVERED : BeeReleaseStatus.BEE_RELEASED;
                if (this.releaseOccupant(cee3, a4, null, b5)) {
                    iterator2.remove();
                }
            }
            a4.ticksInHive++;
        }
    }
    
    @Override
    public void tick() {
        if (this.level.isClientSide) {
            return;
        }
        this.tickOccupants();
        final BlockPos fx2 = this.getBlockPos();
        if (this.stored.size() > 0 && this.level.getRandom().nextDouble() < 0.005) {
            final double double3 = fx2.getX() + 0.5;
            final double double4 = fx2.getY();
            final double double5 = fx2.getZ() + 0.5;
            this.level.playSound(null, double3, double4, double5, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0f, 1.0f);
        }
        this.sendDebugPackets();
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.stored.clear();
        final ListTag mj4 = md.getList("Bees", 10);
        for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
            final CompoundTag md2 = mj4.getCompound(integer5);
            final BeeData a7 = new BeeData(md2.getCompound("EntityData"), md2.getInt("TicksInHive"), md2.getInt("MinOccupationTicks"));
            this.stored.add(a7);
        }
        this.savedFlowerPos = null;
        if (md.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(md.getCompound("FlowerPos"));
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.put("Bees", (net.minecraft.nbt.Tag)this.writeBees());
        if (this.hasSavedFlowerPos()) {
            md.put("FlowerPos", (net.minecraft.nbt.Tag)NbtUtils.writeBlockPos(this.savedFlowerPos));
        }
        return md;
    }
    
    public ListTag writeBees() {
        final ListTag mj2 = new ListTag();
        for (final BeeData a4 : this.stored) {
            a4.entityData.remove("UUID");
            final CompoundTag md5 = new CompoundTag();
            md5.put("EntityData", (net.minecraft.nbt.Tag)a4.entityData);
            md5.putInt("TicksInHive", a4.ticksInHive);
            md5.putInt("MinOccupationTicks", a4.minOccupationTicks);
            mj2.add(md5);
        }
        return mj2;
    }
    
    public enum BeeReleaseStatus {
        HONEY_DELIVERED, 
        BEE_RELEASED, 
        EMERGENCY;
    }
    
    static class BeeData {
        private final CompoundTag entityData;
        private int ticksInHive;
        private final int minOccupationTicks;
        
        private BeeData(final CompoundTag md, final int integer2, final int integer3) {
            md.remove("UUID");
            this.entityData = md;
            this.ticksInHive = integer2;
            this.minOccupationTicks = integer3;
        }
    }
}
