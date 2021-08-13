package net.minecraft.world.level.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PlayerHeadBlock extends SkullBlock {
    protected PlayerHeadBlock(final Properties c) {
        super(Types.PLAYER, c);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        super.setPlacedBy(bru, fx, cee, aqj, bly);
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof SkullBlockEntity) {
            final SkullBlockEntity cdd8 = (SkullBlockEntity)ccg7;
            GameProfile gameProfile9 = null;
            if (bly.hasTag()) {
                final CompoundTag md10 = bly.getTag();
                if (md10.contains("SkullOwner", 10)) {
                    gameProfile9 = NbtUtils.readGameProfile(md10.getCompound("SkullOwner"));
                }
                else if (md10.contains("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)md10.getString("SkullOwner"))) {
                    gameProfile9 = new GameProfile((UUID)null, md10.getString("SkullOwner"));
                }
            }
            cdd8.setOwner(gameProfile9);
        }
    }
}
