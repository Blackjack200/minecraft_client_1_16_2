package net.minecraft.client.gui.screens;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.world.level.chunk.ChunkStatus;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.progress.StoringChunkProgressListener;

public class LevelLoadingScreen extends Screen {
    private final StoringChunkProgressListener progressListener;
    private long lastNarration;
    private static final Object2IntMap<ChunkStatus> COLORS;
    
    public LevelLoadingScreen(final StoringChunkProgressListener aat) {
        super(NarratorChatListener.NO_TITLE);
        this.lastNarration = -1L;
        this.progressListener = aat;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public void removed() {
        NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.loading.done").getString());
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final String string6 = new StringBuilder().append(Mth.clamp(this.progressListener.getProgress(), 0, 100)).append("%").toString();
        final long long7 = Util.getMillis();
        if (long7 - this.lastNarration > 2000L) {
            this.lastNarration = long7;
            NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.loading", new Object[] { string6 }).getString());
        }
        final int integer4 = this.width / 2;
        final int integer5 = this.height / 2;
        final int integer6 = 30;
        renderChunks(dfj, this.progressListener, integer4, integer5 + 30, 2, 0);
        final Font font = this.font;
        final String string7 = string6;
        final int integer7 = integer4;
        final int n = integer5;
        this.font.getClass();
        GuiComponent.drawCenteredString(dfj, font, string7, integer7, n - 9 / 2 - 30, 16777215);
    }
    
    public static void renderChunks(final PoseStack dfj, final StoringChunkProgressListener aat, final int integer3, final int integer4, final int integer5, final int integer6) {
        final int integer7 = integer5 + integer6;
        final int integer8 = aat.getFullDiameter();
        final int integer9 = integer8 * integer7 - integer6;
        final int integer10 = aat.getDiameter();
        final int integer11 = integer10 * integer7 - integer6;
        final int integer12 = integer3 - integer11 / 2;
        final int integer13 = integer4 - integer11 / 2;
        final int integer14 = integer9 / 2 + 1;
        final int integer15 = -16772609;
        if (integer6 != 0) {
            GuiComponent.fill(dfj, integer3 - integer14, integer4 - integer14, integer3 - integer14 + 1, integer4 + integer14, -16772609);
            GuiComponent.fill(dfj, integer3 + integer14 - 1, integer4 - integer14, integer3 + integer14, integer4 + integer14, -16772609);
            GuiComponent.fill(dfj, integer3 - integer14, integer4 - integer14, integer3 + integer14, integer4 - integer14 + 1, -16772609);
            GuiComponent.fill(dfj, integer3 - integer14, integer4 + integer14 - 1, integer3 + integer14, integer4 + integer14, -16772609);
        }
        for (int integer16 = 0; integer16 < integer10; ++integer16) {
            for (int integer17 = 0; integer17 < integer10; ++integer17) {
                final ChunkStatus cfx18 = aat.getStatus(integer16, integer17);
                final int integer18 = integer12 + integer16 * integer7;
                final int integer19 = integer13 + integer17 * integer7;
                GuiComponent.fill(dfj, integer18, integer19, integer18 + integer5, integer19 + integer5, LevelLoadingScreen.COLORS.getInt(cfx18) | 0xFF000000);
            }
        }
    }
    
    static {
        COLORS = Util.<Object2IntMap>make((Object2IntMap)new Object2IntOpenHashMap(), (java.util.function.Consumer<Object2IntMap>)(object2IntOpenHashMap -> {
            object2IntOpenHashMap.defaultReturnValue(0);
            object2IntOpenHashMap.put(ChunkStatus.EMPTY, 5526612);
            object2IntOpenHashMap.put(ChunkStatus.STRUCTURE_STARTS, 10066329);
            object2IntOpenHashMap.put(ChunkStatus.STRUCTURE_REFERENCES, 6250897);
            object2IntOpenHashMap.put(ChunkStatus.BIOMES, 8434258);
            object2IntOpenHashMap.put(ChunkStatus.NOISE, 13750737);
            object2IntOpenHashMap.put(ChunkStatus.SURFACE, 7497737);
            object2IntOpenHashMap.put(ChunkStatus.CARVERS, 7169628);
            object2IntOpenHashMap.put(ChunkStatus.LIQUID_CARVERS, 3159410);
            object2IntOpenHashMap.put(ChunkStatus.FEATURES, 2213376);
            object2IntOpenHashMap.put(ChunkStatus.LIGHT, 13421772);
            object2IntOpenHashMap.put(ChunkStatus.SPAWN, 15884384);
            object2IntOpenHashMap.put(ChunkStatus.HEIGHTMAPS, 15658734);
            object2IntOpenHashMap.put(ChunkStatus.FULL, 16777215);
        }));
    }
}
