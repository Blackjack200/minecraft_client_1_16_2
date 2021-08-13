package net.minecraft.client.player;

import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.google.common.hash.Hashing;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import java.io.File;
import net.minecraft.util.StringUtil;
import net.minecraft.client.renderer.texture.HttpTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.world.level.GameType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractClientPlayer extends Player {
    private PlayerInfo playerInfo;
    public float elytraRotX;
    public float elytraRotY;
    public float elytraRotZ;
    public final ClientLevel clientLevel;
    
    public AbstractClientPlayer(final ClientLevel dwl, final GameProfile gameProfile) {
        super(dwl, dwl.getSharedSpawnPos(), dwl.getSharedSpawnAngle(), gameProfile);
        this.clientLevel = dwl;
    }
    
    @Override
    public boolean isSpectator() {
        final PlayerInfo dwp2 = Minecraft.getInstance().getConnection().getPlayerInfo(this.getGameProfile().getId());
        return dwp2 != null && dwp2.getGameMode() == GameType.SPECTATOR;
    }
    
    @Override
    public boolean isCreative() {
        final PlayerInfo dwp2 = Minecraft.getInstance().getConnection().getPlayerInfo(this.getGameProfile().getId());
        return dwp2 != null && dwp2.getGameMode() == GameType.CREATIVE;
    }
    
    public boolean isCapeLoaded() {
        return this.getPlayerInfo() != null;
    }
    
    @Nullable
    protected PlayerInfo getPlayerInfo() {
        if (this.playerInfo == null) {
            this.playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(this.getUUID());
        }
        return this.playerInfo;
    }
    
    public boolean isSkinLoaded() {
        final PlayerInfo dwp2 = this.getPlayerInfo();
        return dwp2 != null && dwp2.isSkinLoaded();
    }
    
    public ResourceLocation getSkinTextureLocation() {
        final PlayerInfo dwp2 = this.getPlayerInfo();
        return (dwp2 == null) ? DefaultPlayerSkin.getDefaultSkin(this.getUUID()) : dwp2.getSkinLocation();
    }
    
    @Nullable
    public ResourceLocation getCloakTextureLocation() {
        final PlayerInfo dwp2 = this.getPlayerInfo();
        return (dwp2 == null) ? null : dwp2.getCapeLocation();
    }
    
    public boolean isElytraLoaded() {
        return this.getPlayerInfo() != null;
    }
    
    @Nullable
    public ResourceLocation getElytraTextureLocation() {
        final PlayerInfo dwp2 = this.getPlayerInfo();
        return (dwp2 == null) ? null : dwp2.getElytraLocation();
    }
    
    public static HttpTexture registerSkinTexture(final ResourceLocation vk, final String string) {
        final TextureManager ejv3 = Minecraft.getInstance().getTextureManager();
        AbstractTexture eji4 = ejv3.getTexture(vk);
        if (eji4 == null) {
            eji4 = new HttpTexture(null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[] { StringUtil.stripColor(string) }), DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(string)), true, null);
            ejv3.register(vk, eji4);
        }
        return (HttpTexture)eji4;
    }
    
    public static ResourceLocation getSkinLocation(final String string) {
        return new ResourceLocation(new StringBuilder().append("skins/").append(Hashing.sha1().hashUnencodedChars((CharSequence)StringUtil.stripColor(string))).toString());
    }
    
    public String getModelName() {
        final PlayerInfo dwp2 = this.getPlayerInfo();
        return (dwp2 == null) ? DefaultPlayerSkin.getSkinModelName(this.getUUID()) : dwp2.getModelName();
    }
    
    public float getFieldOfViewModifier() {
        float float2 = 1.0f;
        if (this.abilities.flying) {
            float2 *= 1.1f;
        }
        float2 *= (float)((this.getAttributeValue(Attributes.MOVEMENT_SPEED) / this.abilities.getWalkingSpeed() + 1.0) / 2.0);
        if (this.abilities.getWalkingSpeed() == 0.0f || Float.isNaN(float2) || Float.isInfinite(float2)) {
            float2 = 1.0f;
        }
        if (this.isUsingItem() && this.getUseItem().getItem() == Items.BOW) {
            final int integer3 = this.getTicksUsingItem();
            float float3 = integer3 / 20.0f;
            if (float3 > 1.0f) {
                float3 = 1.0f;
            }
            else {
                float3 *= float3;
            }
            float2 *= 1.0f - float3 * 0.15f;
        }
        return Mth.lerp(Minecraft.getInstance().options.fovEffectScale, 1.0f, float2);
    }
}
