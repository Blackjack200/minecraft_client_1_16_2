package net.minecraft.client.gui.spectator.categories;

import net.minecraft.network.chat.TranslatableComponent;
import com.google.common.collect.ComparisonChain;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import java.util.Iterator;
import net.minecraft.client.gui.spectator.PlayerMenuItem;
import net.minecraft.world.level.GameType;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;
import net.minecraft.client.gui.spectator.SpectatorMenuCategory;

public class TeleportToPlayerMenuCategory implements SpectatorMenuCategory, SpectatorMenuItem {
    private static final Ordering<PlayerInfo> PROFILE_ORDER;
    private static final Component TELEPORT_TEXT;
    private static final Component TELEPORT_PROMPT;
    private final List<SpectatorMenuItem> items;
    
    public TeleportToPlayerMenuCategory() {
        this((Collection<PlayerInfo>)TeleportToPlayerMenuCategory.PROFILE_ORDER.sortedCopy((Iterable)Minecraft.getInstance().getConnection().getOnlinePlayers()));
    }
    
    public TeleportToPlayerMenuCategory(final Collection<PlayerInfo> collection) {
        this.items = (List<SpectatorMenuItem>)Lists.newArrayList();
        for (final PlayerInfo dwp4 : TeleportToPlayerMenuCategory.PROFILE_ORDER.sortedCopy((Iterable)collection)) {
            if (dwp4.getGameMode() != GameType.SPECTATOR) {
                this.items.add(new PlayerMenuItem(dwp4.getProfile()));
            }
        }
    }
    
    public List<SpectatorMenuItem> getItems() {
        return this.items;
    }
    
    public Component getPrompt() {
        return TeleportToPlayerMenuCategory.TELEPORT_PROMPT;
    }
    
    public void selectItem(final SpectatorMenu dsi) {
        dsi.selectCategory(this);
    }
    
    public Component getName() {
        return TeleportToPlayerMenuCategory.TELEPORT_TEXT;
    }
    
    public void renderIcon(final PoseStack dfj, final float float2, final int integer) {
        Minecraft.getInstance().getTextureManager().bind(SpectatorGui.SPECTATOR_LOCATION);
        GuiComponent.blit(dfj, 0, 0, 0.0f, 0.0f, 16, 16, 256, 256);
    }
    
    public boolean isEnabled() {
        return !this.items.isEmpty();
    }
    
    static {
        PROFILE_ORDER = Ordering.from((dwp1, dwp2) -> ComparisonChain.start().compare((Comparable)dwp1.getProfile().getId(), (Comparable)dwp2.getProfile().getId()).result());
        TELEPORT_TEXT = new TranslatableComponent("spectatorMenu.teleport");
        TELEPORT_PROMPT = new TranslatableComponent("spectatorMenu.teleport.prompt");
    }
}
