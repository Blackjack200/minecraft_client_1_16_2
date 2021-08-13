package net.minecraft.client.gui.components;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.entity.player.PlayerModelPart;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.network.chat.FormattedText;
import javax.annotation.Nullable;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.GameType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import com.google.common.collect.Ordering;
import net.minecraft.client.gui.GuiComponent;

public class PlayerTabOverlay extends GuiComponent {
    private static final Ordering<PlayerInfo> PLAYER_ORDERING;
    private final Minecraft minecraft;
    private final Gui gui;
    private Component footer;
    private Component header;
    private long visibilityId;
    private boolean visible;
    
    public PlayerTabOverlay(final Minecraft djw, final Gui dks) {
        this.minecraft = djw;
        this.gui = dks;
    }
    
    public Component getNameForDisplay(final PlayerInfo dwp) {
        if (dwp.getTabListDisplayName() != null) {
            return this.decorateName(dwp, dwp.getTabListDisplayName().copy());
        }
        return this.decorateName(dwp, PlayerTeam.formatNameForTeam(dwp.getTeam(), new TextComponent(dwp.getProfile().getName())));
    }
    
    private Component decorateName(final PlayerInfo dwp, final MutableComponent nx) {
        return (dwp.getGameMode() == GameType.SPECTATOR) ? nx.withStyle(ChatFormatting.ITALIC) : nx;
    }
    
    public void setVisible(final boolean boolean1) {
        if (boolean1 && !this.visible) {
            this.visibilityId = Util.getMillis();
        }
        this.visible = boolean1;
    }
    
    public void render(final PoseStack dfj, final int integer, final Scoreboard ddk, @Nullable final Objective ddh) {
        final ClientPacketListener dwm6 = this.minecraft.player.connection;
        List<PlayerInfo> list7 = (List<PlayerInfo>)PlayerTabOverlay.PLAYER_ORDERING.sortedCopy((Iterable)dwm6.getOnlinePlayers());
        int integer2 = 0;
        int integer3 = 0;
        for (final PlayerInfo dwp11 : list7) {
            int integer4 = this.minecraft.font.width(this.getNameForDisplay(dwp11));
            integer2 = Math.max(integer2, integer4);
            if (ddh != null && ddh.getRenderType() != ObjectiveCriteria.RenderType.HEARTS) {
                integer4 = this.minecraft.font.width(new StringBuilder().append(" ").append(ddk.getOrCreatePlayerScore(dwp11.getProfile().getName(), ddh).getScore()).toString());
                integer3 = Math.max(integer3, integer4);
            }
        }
        list7 = (List<PlayerInfo>)list7.subList(0, Math.min(list7.size(), 80));
        int integer4;
        int integer6;
        int integer5;
        for (integer5 = (integer6 = list7.size()), integer4 = 1; integer6 > 20; integer6 = (integer5 + integer4 - 1) / integer4) {
            ++integer4;
        }
        final boolean boolean13 = this.minecraft.isLocalServer() || this.minecraft.getConnection().getConnection().isEncrypted();
        int integer7;
        if (ddh != null) {
            if (ddh.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
                integer7 = 90;
            }
            else {
                integer7 = integer3;
            }
        }
        else {
            integer7 = 0;
        }
        final int integer8 = Math.min(integer4 * ((boolean13 ? 9 : 0) + integer2 + integer7 + 13), integer - 50) / integer4;
        final int integer9 = integer / 2 - (integer8 * integer4 + (integer4 - 1) * 5) / 2;
        int integer10 = 10;
        int integer11 = integer8 * integer4 + (integer4 - 1) * 5;
        List<FormattedCharSequence> list8 = null;
        if (this.header != null) {
            list8 = this.minecraft.font.split(this.header, integer - 50);
            for (final FormattedCharSequence aex21 : list8) {
                integer11 = Math.max(integer11, this.minecraft.font.width(aex21));
            }
        }
        List<FormattedCharSequence> list9 = null;
        if (this.footer != null) {
            list9 = this.minecraft.font.split(this.footer, integer - 50);
            for (final FormattedCharSequence aex22 : list9) {
                integer11 = Math.max(integer11, this.minecraft.font.width(aex22));
            }
        }
        if (list8 != null) {
            final int integer24 = integer / 2 - integer11 / 2 - 1;
            final int integer25 = integer10 - 1;
            final int integer26 = integer / 2 + integer11 / 2 + 1;
            final int n = integer10;
            final int size = list8.size();
            this.minecraft.font.getClass();
            GuiComponent.fill(dfj, integer24, integer25, integer26, n + size * 9, Integer.MIN_VALUE);
            for (final FormattedCharSequence aex22 : list8) {
                final int integer12 = this.minecraft.font.width(aex22);
                this.minecraft.font.drawShadow(dfj, aex22, (float)(integer / 2 - integer12 / 2), (float)integer10, -1);
                final int n2 = integer10;
                this.minecraft.font.getClass();
                integer10 = n2 + 9;
            }
            ++integer10;
        }
        GuiComponent.fill(dfj, integer / 2 - integer11 / 2 - 1, integer10 - 1, integer / 2 + integer11 / 2 + 1, integer10 + integer6 * 9, Integer.MIN_VALUE);
        final int integer13 = this.minecraft.options.getBackgroundColor(553648127);
        for (int integer14 = 0; integer14 < integer5; ++integer14) {
            final int integer12 = integer14 / integer6;
            final int integer15 = integer14 % integer6;
            int integer16 = integer9 + integer12 * integer8 + integer12 * 5;
            final int integer17 = integer10 + integer15 * 9;
            GuiComponent.fill(dfj, integer16, integer17, integer16 + integer8, integer17 + 8, integer13);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.enableAlphaTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (integer14 < list7.size()) {
                final PlayerInfo dwp12 = (PlayerInfo)list7.get(integer14);
                final GameProfile gameProfile28 = dwp12.getProfile();
                if (boolean13) {
                    final Player bft29 = this.minecraft.level.getPlayerByUUID(gameProfile28.getId());
                    final boolean boolean14 = bft29 != null && bft29.isModelPartShown(PlayerModelPart.CAPE) && ("Dinnerbone".equals(gameProfile28.getName()) || "Grumm".equals(gameProfile28.getName()));
                    this.minecraft.getTextureManager().bind(dwp12.getSkinLocation());
                    final int integer18 = 8 + (boolean14 ? 8 : 0);
                    final int integer19 = 8 * (boolean14 ? -1 : 1);
                    GuiComponent.blit(dfj, integer16, integer17, 8, 8, 8.0f, (float)integer18, 8, integer19, 64, 64);
                    if (bft29 != null && bft29.isModelPartShown(PlayerModelPart.HAT)) {
                        final int integer20 = 8 + (boolean14 ? 8 : 0);
                        final int integer21 = 8 * (boolean14 ? -1 : 1);
                        GuiComponent.blit(dfj, integer16, integer17, 8, 8, 40.0f, (float)integer20, 8, integer21, 64, 64);
                    }
                    integer16 += 9;
                }
                this.minecraft.font.drawShadow(dfj, this.getNameForDisplay(dwp12), (float)integer16, (float)integer17, (dwp12.getGameMode() == GameType.SPECTATOR) ? -1862270977 : -1);
                if (ddh != null && dwp12.getGameMode() != GameType.SPECTATOR) {
                    final int integer22 = integer16 + integer2 + 1;
                    final int integer23 = integer22 + integer7;
                    if (integer23 - integer22 > 5) {
                        this.renderTablistScore(ddh, integer17, gameProfile28.getName(), integer22, integer23, dwp12, dfj);
                    }
                }
                this.renderPingIcon(dfj, integer8, integer16 - (boolean13 ? 9 : 0), integer17, dwp12);
            }
        }
        if (list9 != null) {
            integer10 += integer6 * 9 + 1;
            final int integer27 = integer / 2 - integer11 / 2 - 1;
            final int integer28 = integer10 - 1;
            final int integer29 = integer / 2 + integer11 / 2 + 1;
            final int n3 = integer10;
            final int size2 = list9.size();
            this.minecraft.font.getClass();
            GuiComponent.fill(dfj, integer27, integer28, integer29, n3 + size2 * 9, Integer.MIN_VALUE);
            for (final FormattedCharSequence aex23 : list9) {
                final int integer15 = this.minecraft.font.width(aex23);
                this.minecraft.font.drawShadow(dfj, aex23, (float)(integer / 2 - integer15 / 2), (float)integer10, -1);
                final int n4 = integer10;
                this.minecraft.font.getClass();
                integer10 = n4 + 9;
            }
        }
    }
    
    protected void renderPingIcon(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final PlayerInfo dwp) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(PlayerTabOverlay.GUI_ICONS_LOCATION);
        final int integer5 = 0;
        int integer6;
        if (dwp.getLatency() < 0) {
            integer6 = 5;
        }
        else if (dwp.getLatency() < 150) {
            integer6 = 0;
        }
        else if (dwp.getLatency() < 300) {
            integer6 = 1;
        }
        else if (dwp.getLatency() < 600) {
            integer6 = 2;
        }
        else if (dwp.getLatency() < 1000) {
            integer6 = 3;
        }
        else {
            integer6 = 4;
        }
        this.setBlitOffset(this.getBlitOffset() + 100);
        this.blit(dfj, integer3 + integer2 - 11, integer4, 0, 176 + integer6 * 8, 10, 8);
        this.setBlitOffset(this.getBlitOffset() - 100);
    }
    
    private void renderTablistScore(final Objective ddh, final int integer2, final String string, final int integer4, final int integer5, final PlayerInfo dwp, final PoseStack dfj) {
        final int integer6 = ddh.getScoreboard().getOrCreatePlayerScore(string, ddh).getScore();
        if (ddh.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
            this.minecraft.getTextureManager().bind(PlayerTabOverlay.GUI_ICONS_LOCATION);
            final long long10 = Util.getMillis();
            if (this.visibilityId == dwp.getRenderVisibilityId()) {
                if (integer6 < dwp.getLastHealth()) {
                    dwp.setLastHealthTime(long10);
                    dwp.setHealthBlinkTime(this.gui.getGuiTicks() + 20);
                }
                else if (integer6 > dwp.getLastHealth()) {
                    dwp.setLastHealthTime(long10);
                    dwp.setHealthBlinkTime(this.gui.getGuiTicks() + 10);
                }
            }
            if (long10 - dwp.getLastHealthTime() > 1000L || this.visibilityId != dwp.getRenderVisibilityId()) {
                dwp.setLastHealth(integer6);
                dwp.setDisplayHealth(integer6);
                dwp.setLastHealthTime(long10);
            }
            dwp.setRenderVisibilityId(this.visibilityId);
            dwp.setLastHealth(integer6);
            final int integer7 = Mth.ceil(Math.max(integer6, dwp.getDisplayHealth()) / 2.0f);
            final int integer8 = Math.max(Mth.ceil((float)(integer6 / 2)), Math.max(Mth.ceil((float)(dwp.getDisplayHealth() / 2)), 10));
            final boolean boolean14 = dwp.getHealthBlinkTime() > this.gui.getGuiTicks() && (dwp.getHealthBlinkTime() - this.gui.getGuiTicks()) / 3L % 2L == 1L;
            if (integer7 > 0) {
                final int integer9 = Mth.floor(Math.min((integer5 - integer4 - 4) / (float)integer8, 9.0f));
                if (integer9 > 3) {
                    for (int integer10 = integer7; integer10 < integer8; ++integer10) {
                        this.blit(dfj, integer4 + integer10 * integer9, integer2, boolean14 ? 25 : 16, 0, 9, 9);
                    }
                    for (int integer10 = 0; integer10 < integer7; ++integer10) {
                        this.blit(dfj, integer4 + integer10 * integer9, integer2, boolean14 ? 25 : 16, 0, 9, 9);
                        if (boolean14) {
                            if (integer10 * 2 + 1 < dwp.getDisplayHealth()) {
                                this.blit(dfj, integer4 + integer10 * integer9, integer2, 70, 0, 9, 9);
                            }
                            if (integer10 * 2 + 1 == dwp.getDisplayHealth()) {
                                this.blit(dfj, integer4 + integer10 * integer9, integer2, 79, 0, 9, 9);
                            }
                        }
                        if (integer10 * 2 + 1 < integer6) {
                            this.blit(dfj, integer4 + integer10 * integer9, integer2, (integer10 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (integer10 * 2 + 1 == integer6) {
                            this.blit(dfj, integer4 + integer10 * integer9, integer2, (integer10 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float float16 = Mth.clamp(integer6 / 20.0f, 0.0f, 1.0f);
                    final int integer11 = (int)((1.0f - float16) * 255.0f) << 16 | (int)(float16 * 255.0f) << 8;
                    String string2 = new StringBuilder().append("").append(integer6 / 2.0f).toString();
                    if (integer5 - this.minecraft.font.width(string2 + "hp") >= integer4) {
                        string2 += "hp";
                    }
                    this.minecraft.font.drawShadow(dfj, string2, (float)((integer5 + integer4) / 2 - this.minecraft.font.width(string2) / 2), (float)integer2, integer11);
                }
            }
        }
        else {
            final String string3 = new StringBuilder().append(ChatFormatting.YELLOW).append("").append(integer6).toString();
            this.minecraft.font.drawShadow(dfj, string3, (float)(integer5 - this.minecraft.font.width(string3)), (float)integer2, 16777215);
        }
    }
    
    public void setFooter(@Nullable final Component nr) {
        this.footer = nr;
    }
    
    public void setHeader(@Nullable final Component nr) {
        this.header = nr;
    }
    
    public void reset() {
        this.header = null;
        this.footer = null;
    }
    
    static {
        PLAYER_ORDERING = Ordering.from((Comparator)new PlayerInfoComparator());
    }
    
    static class PlayerInfoComparator implements Comparator<PlayerInfo> {
        private PlayerInfoComparator() {
        }
        
        public int compare(final PlayerInfo dwp1, final PlayerInfo dwp2) {
            final PlayerTeam ddi4 = dwp1.getTeam();
            final PlayerTeam ddi5 = dwp2.getTeam();
            return ComparisonChain.start().compareTrueFirst(dwp1.getGameMode() != GameType.SPECTATOR, dwp2.getGameMode() != GameType.SPECTATOR).compare((ddi4 != null) ? ddi4.getName() : "", (ddi5 != null) ? ddi5.getName() : "").compare(dwp1.getProfile().getName(), dwp2.getProfile().getName(), String::compareToIgnoreCase).result();
        }
    }
}
