package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import java.util.Random;
import net.minecraft.world.Nameable;

public class EnchantmentTableBlockEntity extends BlockEntity implements Nameable, TickableBlockEntity {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final Random RANDOM;
    private Component name;
    
    public EnchantmentTableBlockEntity() {
        super(BlockEntityType.ENCHANTING_TABLE);
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (this.hasCustomName()) {
            md.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(md.getString("CustomName"));
        }
    }
    
    @Override
    public void tick() {
        this.oOpen = this.open;
        this.oRot = this.rot;
        final Player bft2 = this.level.getNearestPlayer(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, 3.0, false);
        if (bft2 != null) {
            final double double3 = bft2.getX() - (this.worldPosition.getX() + 0.5);
            final double double4 = bft2.getZ() - (this.worldPosition.getZ() + 0.5);
            this.tRot = (float)Mth.atan2(double4, double3);
            this.open += 0.1f;
            if (this.open < 0.5f || EnchantmentTableBlockEntity.RANDOM.nextInt(40) == 0) {
                final float float7 = this.flipT;
                do {
                    this.flipT += EnchantmentTableBlockEntity.RANDOM.nextInt(4) - EnchantmentTableBlockEntity.RANDOM.nextInt(4);
                } while (float7 == this.flipT);
            }
        }
        else {
            this.tRot += 0.02f;
            this.open -= 0.1f;
        }
        while (this.rot >= 3.1415927f) {
            this.rot -= 6.2831855f;
        }
        while (this.rot < -3.1415927f) {
            this.rot += 6.2831855f;
        }
        while (this.tRot >= 3.1415927f) {
            this.tRot -= 6.2831855f;
        }
        while (this.tRot < -3.1415927f) {
            this.tRot += 6.2831855f;
        }
        float float8;
        for (float8 = this.tRot - this.rot; float8 >= 3.1415927f; float8 -= 6.2831855f) {}
        while (float8 < -3.1415927f) {
            float8 += 6.2831855f;
        }
        this.rot += float8 * 0.4f;
        this.open = Mth.clamp(this.open, 0.0f, 1.0f);
        ++this.time;
        this.oFlip = this.flip;
        float float9 = (this.flipT - this.flip) * 0.4f;
        final float float10 = 0.2f;
        float9 = Mth.clamp(float9, -0.2f, 0.2f);
        this.flipA += (float9 - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
    
    @Override
    public Component getName() {
        if (this.name != null) {
            return this.name;
        }
        return new TranslatableComponent("container.enchant");
    }
    
    public void setCustomName(@Nullable final Component nr) {
        this.name = nr;
    }
    
    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }
    
    static {
        RANDOM = new Random();
    }
}
