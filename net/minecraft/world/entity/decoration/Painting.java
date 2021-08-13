package net.minecraft.world.entity.decoration;

import net.minecraft.network.protocol.game.ClientboundAddPaintingPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.GameRules;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.Registry;
import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Painting extends HangingEntity {
    public Motive motive;
    
    public Painting(final EntityType<? extends Painting> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public Painting(final Level bru, final BlockPos fx, final Direction gc) {
        super(EntityType.PAINTING, bru, fx);
        final List<Motive> list5 = (List<Motive>)Lists.newArrayList();
        int integer6 = 0;
        for (final Motive bco8 : Registry.MOTIVE) {
            this.motive = bco8;
            this.setDirection(gc);
            if (this.survives()) {
                list5.add(bco8);
                final int integer7 = bco8.getWidth() * bco8.getHeight();
                if (integer7 <= integer6) {
                    continue;
                }
                integer6 = integer7;
            }
        }
        if (!list5.isEmpty()) {
            final Iterator<Motive> iterator7 = (Iterator<Motive>)list5.iterator();
            while (iterator7.hasNext()) {
                final Motive bco8 = (Motive)iterator7.next();
                if (bco8.getWidth() * bco8.getHeight() < integer6) {
                    iterator7.remove();
                }
            }
            this.motive = (Motive)list5.get(this.random.nextInt(list5.size()));
        }
        this.setDirection(gc);
    }
    
    public Painting(final Level bru, final BlockPos fx, final Direction gc, final Motive bco) {
        this(bru, fx, gc);
        this.motive = bco;
        this.setDirection(gc);
    }
    
    @Override
    public void addAdditionalSaveData(final CompoundTag md) {
        md.putString("Motive", Registry.MOTIVE.getKey(this.motive).toString());
        md.putByte("Facing", (byte)this.direction.get2DDataValue());
        super.addAdditionalSaveData(md);
    }
    
    @Override
    public void readAdditionalSaveData(final CompoundTag md) {
        this.motive = Registry.MOTIVE.get(ResourceLocation.tryParse(md.getString("Motive")));
        this.direction = Direction.from2DDataValue(md.getByte("Facing"));
        super.readAdditionalSaveData(md);
        this.setDirection(this.direction);
    }
    
    @Override
    public int getWidth() {
        if (this.motive == null) {
            return 1;
        }
        return this.motive.getWidth();
    }
    
    @Override
    public int getHeight() {
        if (this.motive == null) {
            return 1;
        }
        return this.motive.getHeight();
    }
    
    @Override
    public void dropItem(@Nullable final Entity apx) {
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            return;
        }
        this.playSound(SoundEvents.PAINTING_BREAK, 1.0f, 1.0f);
        if (apx instanceof Player) {
            final Player bft3 = (Player)apx;
            if (bft3.abilities.instabuild) {
                return;
            }
        }
        this.spawnAtLocation(Items.PAINTING);
    }
    
    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0f, 1.0f);
    }
    
    @Override
    public void moveTo(final double double1, final double double2, final double double3, final float float4, final float float5) {
        this.setPos(double1, double2, double3);
    }
    
    @Override
    public void lerpTo(final double double1, final double double2, final double double3, final float float4, final float float5, final int integer, final boolean boolean7) {
        final BlockPos fx12 = this.pos.offset(double1 - this.getX(), double2 - this.getY(), double3 - this.getZ());
        this.setPos(fx12.getX(), fx12.getY(), fx12.getZ());
    }
    
    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddPaintingPacket(this);
    }
}
