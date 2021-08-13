package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundTeleportToEntityPacket;
import java.util.Map;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.world.entity.player.Player;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import com.mojang.authlib.GameProfile;

public class PlayerMenuItem implements SpectatorMenuItem {
    private final GameProfile profile;
    private final ResourceLocation location;
    private final TextComponent name;
    
    public PlayerMenuItem(final GameProfile gameProfile) {
        this.profile = gameProfile;
        final Minecraft djw3 = Minecraft.getInstance();
        final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map4 = djw3.getSkinManager().getInsecureSkinInformation(gameProfile);
        if (map4.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            this.location = djw3.getSkinManager().registerTexture((MinecraftProfileTexture)map4.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
        }
        else {
            this.location = DefaultPlayerSkin.getDefaultSkin(Player.createPlayerUUID(gameProfile));
        }
        this.name = new TextComponent(gameProfile.getName());
    }
    
    public void selectItem(final SpectatorMenu dsi) {
        Minecraft.getInstance().getConnection().send(new ServerboundTeleportToEntityPacket(this.profile.getId()));
    }
    
    public Component getName() {
        return this.name;
    }
    
    public void renderIcon(final PoseStack dfj, final float float2, final int integer) {
        Minecraft.getInstance().getTextureManager().bind(this.location);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, integer / 255.0f);
        GuiComponent.blit(dfj, 2, 2, 12, 12, 8.0f, 8.0f, 8, 8, 64, 64);
        GuiComponent.blit(dfj, 2, 2, 12, 12, 40.0f, 8.0f, 8, 8, 64, 64);
    }
    
    public boolean isEnabled() {
        return true;
    }
}
