package net.minecraft.server.bossevents;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerPlayer;
import com.google.common.collect.Sets;
import net.minecraft.world.BossEvent;
import net.minecraft.network.chat.Component;
import java.util.UUID;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;

public class CustomBossEvent extends ServerBossEvent {
    private final ResourceLocation id;
    private final Set<UUID> players;
    private int value;
    private int max;
    
    public CustomBossEvent(final ResourceLocation vk, final Component nr) {
        super(nr, BossBarColor.WHITE, BossBarOverlay.PROGRESS);
        this.players = (Set<UUID>)Sets.newHashSet();
        this.max = 100;
        this.id = vk;
        this.setPercent(0.0f);
    }
    
    public ResourceLocation getTextId() {
        return this.id;
    }
    
    @Override
    public void addPlayer(final ServerPlayer aah) {
        super.addPlayer(aah);
        this.players.add(aah.getUUID());
    }
    
    public void addOfflinePlayer(final UUID uUID) {
        this.players.add(uUID);
    }
    
    @Override
    public void removePlayer(final ServerPlayer aah) {
        super.removePlayer(aah);
        this.players.remove(aah.getUUID());
    }
    
    @Override
    public void removeAllPlayers() {
        super.removeAllPlayers();
        this.players.clear();
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getMax() {
        return this.max;
    }
    
    public void setValue(final int integer) {
        this.value = integer;
        this.setPercent(Mth.clamp(integer / (float)this.max, 0.0f, 1.0f));
    }
    
    public void setMax(final int integer) {
        this.max = integer;
        this.setPercent(Mth.clamp(this.value / (float)integer, 0.0f, 1.0f));
    }
    
    public final Component getDisplayName() {
        return ComponentUtils.wrapInSquareBrackets(this.getName()).withStyle((UnaryOperator<Style>)(ob -> ob.withColor(this.getColor().getFormatting()).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent(this.getTextId().toString()))).withInsertion(this.getTextId().toString())));
    }
    
    public boolean setPlayers(final Collection<ServerPlayer> collection) {
        final Set<UUID> set3 = (Set<UUID>)Sets.newHashSet();
        final Set<ServerPlayer> set4 = (Set<ServerPlayer>)Sets.newHashSet();
        for (final UUID uUID6 : this.players) {
            boolean boolean7 = false;
            for (final ServerPlayer aah9 : collection) {
                if (aah9.getUUID().equals(uUID6)) {
                    boolean7 = true;
                    break;
                }
            }
            if (!boolean7) {
                set3.add(uUID6);
            }
        }
        for (final ServerPlayer aah10 : collection) {
            boolean boolean7 = false;
            for (final UUID uUID7 : this.players) {
                if (aah10.getUUID().equals(uUID7)) {
                    boolean7 = true;
                    break;
                }
            }
            if (!boolean7) {
                set4.add(aah10);
            }
        }
        for (final UUID uUID6 : set3) {
            for (final ServerPlayer aah11 : this.getPlayers()) {
                if (aah11.getUUID().equals(uUID6)) {
                    this.removePlayer(aah11);
                    break;
                }
            }
            this.players.remove(uUID6);
        }
        for (final ServerPlayer aah10 : set4) {
            this.addPlayer(aah10);
        }
        return !set3.isEmpty() || !set4.isEmpty();
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("Name", Component.Serializer.toJson(this.name));
        md2.putBoolean("Visible", this.isVisible());
        md2.putInt("Value", this.value);
        md2.putInt("Max", this.max);
        md2.putString("Color", this.getColor().getName());
        md2.putString("Overlay", this.getOverlay().getName());
        md2.putBoolean("DarkenScreen", this.shouldDarkenScreen());
        md2.putBoolean("PlayBossMusic", this.shouldPlayBossMusic());
        md2.putBoolean("CreateWorldFog", this.shouldCreateWorldFog());
        final ListTag mj3 = new ListTag();
        for (final UUID uUID5 : this.players) {
            mj3.add(NbtUtils.createUUID(uUID5));
        }
        md2.put("Players", (Tag)mj3);
        return md2;
    }
    
    public static CustomBossEvent load(final CompoundTag md, final ResourceLocation vk) {
        final CustomBossEvent wc3 = new CustomBossEvent(vk, Component.Serializer.fromJson(md.getString("Name")));
        wc3.setVisible(md.getBoolean("Visible"));
        wc3.setValue(md.getInt("Value"));
        wc3.setMax(md.getInt("Max"));
        wc3.setColor(BossBarColor.byName(md.getString("Color")));
        wc3.setOverlay(BossBarOverlay.byName(md.getString("Overlay")));
        wc3.setDarkenScreen(md.getBoolean("DarkenScreen"));
        wc3.setPlayBossMusic(md.getBoolean("PlayBossMusic"));
        wc3.setCreateWorldFog(md.getBoolean("CreateWorldFog"));
        final ListTag mj4 = md.getList("Players", 11);
        for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
            wc3.addOfflinePlayer(NbtUtils.loadUUID(mj4.get(integer5)));
        }
        return wc3;
    }
    
    public void onPlayerConnect(final ServerPlayer aah) {
        if (this.players.contains(aah.getUUID())) {
            this.addPlayer(aah);
        }
    }
    
    public void onPlayerDisconnect(final ServerPlayer aah) {
        super.removePlayer(aah);
    }
}
