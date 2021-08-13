package net.minecraft.world.level.block.entity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import java.util.UUID;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import javax.annotation.Nullable;
import net.minecraft.server.players.GameProfileCache;

public class SkullBlockEntity extends BlockEntity implements TickableBlockEntity {
    @Nullable
    private static GameProfileCache profileCache;
    @Nullable
    private static MinecraftSessionService sessionService;
    @Nullable
    private GameProfile owner;
    private int mouthTickCount;
    private boolean isMovingMouth;
    
    public SkullBlockEntity() {
        super(BlockEntityType.SKULL);
    }
    
    public static void setProfileCache(final GameProfileCache aco) {
        SkullBlockEntity.profileCache = aco;
    }
    
    public static void setSessionService(final MinecraftSessionService minecraftSessionService) {
        SkullBlockEntity.sessionService = minecraftSessionService;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (this.owner != null) {
            final CompoundTag md2 = new CompoundTag();
            NbtUtils.writeGameProfile(md2, this.owner);
            md.put("SkullOwner", (Tag)md2);
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.contains("SkullOwner", 10)) {
            this.setOwner(NbtUtils.readGameProfile(md.getCompound("SkullOwner")));
        }
        else if (md.contains("ExtraType", 8)) {
            final String string4 = md.getString("ExtraType");
            if (!StringUtil.isNullOrEmpty(string4)) {
                this.setOwner(new GameProfile((UUID)null, string4));
            }
        }
    }
    
    @Override
    public void tick() {
        final BlockState cee2 = this.getBlockState();
        if (cee2.is(Blocks.DRAGON_HEAD) || cee2.is(Blocks.DRAGON_WALL_HEAD)) {
            if (this.level.hasNeighborSignal(this.worldPosition)) {
                this.isMovingMouth = true;
                ++this.mouthTickCount;
            }
            else {
                this.isMovingMouth = false;
            }
        }
    }
    
    public float getMouthAnimation(final float float1) {
        if (this.isMovingMouth) {
            return this.mouthTickCount + float1;
        }
        return (float)this.mouthTickCount;
    }
    
    @Nullable
    public GameProfile getOwnerProfile() {
        return this.owner;
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 4, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public void setOwner(@Nullable final GameProfile gameProfile) {
        this.owner = gameProfile;
        this.updateOwnerProfile();
    }
    
    private void updateOwnerProfile() {
        this.owner = updateGameprofile(this.owner);
        this.setChanged();
    }
    
    @Nullable
    public static GameProfile updateGameprofile(@Nullable final GameProfile gameProfile) {
        if (gameProfile == null || StringUtil.isNullOrEmpty(gameProfile.getName())) {
            return gameProfile;
        }
        if (gameProfile.isComplete() && gameProfile.getProperties().containsKey("textures")) {
            return gameProfile;
        }
        if (SkullBlockEntity.profileCache == null || SkullBlockEntity.sessionService == null) {
            return gameProfile;
        }
        GameProfile gameProfile2 = SkullBlockEntity.profileCache.get(gameProfile.getName());
        if (gameProfile2 == null) {
            return gameProfile;
        }
        final Property property3 = (Property)Iterables.getFirst((Iterable)gameProfile2.getProperties().get("textures"), null);
        if (property3 == null) {
            gameProfile2 = SkullBlockEntity.sessionService.fillProfileProperties(gameProfile2, true);
        }
        return gameProfile2;
    }
}
