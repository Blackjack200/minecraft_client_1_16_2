package net.minecraft.world.item;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import org.apache.commons.lang3.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

public class PlayerHeadItem extends StandingAndWallBlockItem {
    public PlayerHeadItem(final Block bul1, final Block bul2, final Properties a) {
        super(bul1, bul2, a);
    }
    
    @Override
    public Component getName(final ItemStack bly) {
        if (bly.getItem() == Items.PLAYER_HEAD && bly.hasTag()) {
            String string3 = null;
            final CompoundTag md4 = bly.getTag();
            if (md4.contains("SkullOwner", 8)) {
                string3 = md4.getString("SkullOwner");
            }
            else if (md4.contains("SkullOwner", 10)) {
                final CompoundTag md5 = md4.getCompound("SkullOwner");
                if (md5.contains("Name", 8)) {
                    string3 = md5.getString("Name");
                }
            }
            if (string3 != null) {
                return new TranslatableComponent(this.getDescriptionId() + ".named", new Object[] { string3 });
            }
        }
        return super.getName(bly);
    }
    
    @Override
    public boolean verifyTagAfterLoad(final CompoundTag md) {
        super.verifyTagAfterLoad(md);
        if (md.contains("SkullOwner", 8) && !StringUtils.isBlank((CharSequence)md.getString("SkullOwner"))) {
            GameProfile gameProfile3 = new GameProfile((UUID)null, md.getString("SkullOwner"));
            gameProfile3 = SkullBlockEntity.updateGameprofile(gameProfile3);
            md.put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), gameProfile3));
            return true;
        }
        return false;
    }
}
