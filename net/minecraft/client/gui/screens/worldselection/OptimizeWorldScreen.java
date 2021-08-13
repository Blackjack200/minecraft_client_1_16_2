package net.minecraft.client.gui.screens.worldselection;

import java.util.function.Consumer;
import it.unimi.dsi.fastutil.Hash;
import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.client.gui.Font;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.LevelSettings;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.datafixers.util.Function4;
import net.minecraft.world.level.DataPackConfig;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.storage.LevelStorageSource;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.worldupdate.WorldUpgrader;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.Screen;

public class OptimizeWorldScreen extends Screen {
    private static final Logger LOGGER;
    private static final Object2IntMap<ResourceKey<Level>> DIMENSION_COLORS;
    private final BooleanConsumer callback;
    private final WorldUpgrader upgrader;
    
    @Nullable
    public static OptimizeWorldScreen create(final Minecraft djw, final BooleanConsumer booleanConsumer, final DataFixer dataFixer, final LevelStorageSource.LevelStorageAccess a, final boolean boolean5) {
        final RegistryAccess.RegistryHolder b6 = RegistryAccess.builtin();
        try (final Minecraft.ServerStem b7 = djw.makeServerStem(b6, (Function<LevelStorageSource.LevelStorageAccess, DataPackConfig>)Minecraft::loadDataPacks, (Function4<LevelStorageSource.LevelStorageAccess, RegistryAccess.RegistryHolder, ResourceManager, DataPackConfig, WorldData>)Minecraft::loadWorldData, false, a)) {
            final WorldData cyk9 = b7.worldData();
            a.saveDataTag(b6, cyk9);
            final ImmutableSet<ResourceKey<Level>> immutableSet10 = cyk9.worldGenSettings().levels();
            return new OptimizeWorldScreen(booleanConsumer, dataFixer, a, cyk9.getLevelSettings(), boolean5, immutableSet10);
        }
        catch (Exception exception7) {
            OptimizeWorldScreen.LOGGER.warn("Failed to load datapacks, can't optimize world", (Throwable)exception7);
            return null;
        }
    }
    
    private OptimizeWorldScreen(final BooleanConsumer booleanConsumer, final DataFixer dataFixer, final LevelStorageSource.LevelStorageAccess a, final LevelSettings brx, final boolean boolean5, final ImmutableSet<ResourceKey<Level>> immutableSet) {
        super(new TranslatableComponent("optimizeWorld.title", new Object[] { brx.levelName() }));
        this.callback = booleanConsumer;
        this.upgrader = new WorldUpgrader(a, dataFixer, immutableSet, boolean5);
    }
    
    @Override
    protected void init() {
        super.init();
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 150, 200, 20, CommonComponents.GUI_CANCEL, dlg -> {
            this.upgrader.cancel();
            this.callback.accept(false);
        }));
    }
    
    @Override
    public void tick() {
        if (this.upgrader.isFinished()) {
            this.callback.accept(true);
        }
    }
    
    @Override
    public void onClose() {
        this.callback.accept(false);
    }
    
    @Override
    public void removed() {
        this.upgrader.cancel();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        final int integer4 = this.width / 2 - 150;
        final int integer5 = this.width / 2 + 150;
        final int integer6 = this.height / 4 + 100;
        final int integer7 = integer6 + 10;
        final Font font = this.font;
        final Component status = this.upgrader.getStatus();
        final int integer11 = this.width / 2;
        final int n = integer6;
        this.font.getClass();
        GuiComponent.drawCenteredString(dfj, font, status, integer11, n - 9 - 2, 10526880);
        if (this.upgrader.getTotalChunks() > 0) {
            GuiComponent.fill(dfj, integer4 - 1, integer6 - 1, integer5 + 1, integer7 + 1, -16777216);
            GuiComponent.drawString(dfj, this.font, new TranslatableComponent("optimizeWorld.info.converted", new Object[] { this.upgrader.getConverted() }), integer4, 40, 10526880);
            final Font font2 = this.font;
            final TranslatableComponent nr = new TranslatableComponent("optimizeWorld.info.skipped", new Object[] { this.upgrader.getSkipped() });
            final int integer12 = integer4;
            final int n2 = 40;
            this.font.getClass();
            GuiComponent.drawString(dfj, font2, nr, integer12, n2 + 9 + 3, 10526880);
            final Font font3 = this.font;
            final TranslatableComponent nr2 = new TranslatableComponent("optimizeWorld.info.total", new Object[] { this.upgrader.getTotalChunks() });
            final int integer13 = integer4;
            final int n3 = 40;
            this.font.getClass();
            GuiComponent.drawString(dfj, font3, nr2, integer13, n3 + (9 + 3) * 2, 10526880);
            int integer8 = 0;
            for (final ResourceKey<Level> vj12 : this.upgrader.levels()) {
                final int integer9 = Mth.floor(this.upgrader.dimensionProgress(vj12) * (integer5 - integer4));
                GuiComponent.fill(dfj, integer4 + integer8, integer6, integer4 + integer8 + integer9, integer7, OptimizeWorldScreen.DIMENSION_COLORS.getInt(vj12));
                integer8 += integer9;
            }
            final int integer10 = this.upgrader.getConverted() + this.upgrader.getSkipped();
            final Font font4 = this.font;
            final String string = new StringBuilder().append(integer10).append(" / ").append(this.upgrader.getTotalChunks()).toString();
            final int integer14 = this.width / 2;
            final int n4 = integer6;
            final int n5 = 2;
            this.font.getClass();
            GuiComponent.drawCenteredString(dfj, font4, string, integer14, n4 + n5 * 9 + 2, 10526880);
            final Font font5 = this.font;
            final String string2 = new StringBuilder().append(Mth.floor(this.upgrader.getProgress() * 100.0f)).append("%").toString();
            final int integer15 = this.width / 2;
            final int n6 = integer6 + (integer7 - integer6) / 2;
            this.font.getClass();
            GuiComponent.drawCenteredString(dfj, font5, string2, integer15, n6 - 9 / 2, 10526880);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DIMENSION_COLORS = Util.<Object2IntMap>make((Object2IntMap)new Object2IntOpenCustomHashMap((Hash.Strategy)Util.identityStrategy()), (java.util.function.Consumer<Object2IntMap>)(object2IntOpenCustomHashMap -> {
            object2IntOpenCustomHashMap.put(Level.OVERWORLD, -13408734);
            object2IntOpenCustomHashMap.put(Level.NETHER, -10075085);
            object2IntOpenCustomHashMap.put(Level.END, -8943531);
            object2IntOpenCustomHashMap.defaultReturnValue(-2236963);
        }));
    }
}
