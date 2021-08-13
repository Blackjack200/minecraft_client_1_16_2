package net.minecraft.client.gui.screens;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.world.item.Items;
import java.util.Arrays;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import java.util.Map;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import java.util.Optional;
import net.minecraft.world.level.levelgen.StructureSettings;
import com.google.common.collect.Maps;
import java.util.function.Function;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.ItemLike;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import java.util.Iterator;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biomes;
import com.google.common.base.Splitter;
import net.minecraft.world.level.biome.Biome;
import java.util.Collections;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class PresetFlatWorldScreen extends Screen {
    private static final Logger LOGGER;
    private static final List<PresetInfo> PRESETS;
    private final CreateFlatWorldScreen parent;
    private Component shareText;
    private Component listText;
    private PresetsList list;
    private Button selectButton;
    private EditBox export;
    private FlatLevelGeneratorSettings settings;
    
    public PresetFlatWorldScreen(final CreateFlatWorldScreen dns) {
        super(new TranslatableComponent("createWorld.customize.presets.title"));
        this.parent = dns;
    }
    
    @Nullable
    private static FlatLayerInfo getLayerInfoFromString(final String string, final int integer) {
        final String[] arr3 = string.split("\\*", 2);
        int integer2 = 0;
        Label_0049: {
            if (arr3.length == 2) {
                try {
                    integer2 = Math.max(Integer.parseInt(arr3[0]), 0);
                    break Label_0049;
                }
                catch (NumberFormatException numberFormatException5) {
                    PresetFlatWorldScreen.LOGGER.error("Error while parsing flat world string => {}", numberFormatException5.getMessage());
                    return null;
                }
            }
            integer2 = 1;
        }
        final int integer3 = Math.min(integer + integer2, 256);
        final int integer4 = integer3 - integer;
        final String string2 = arr3[arr3.length - 1];
        Block bul7;
        try {
            bul7 = (Block)Registry.BLOCK.getOptional(new ResourceLocation(string2)).orElse(null);
        }
        catch (Exception exception9) {
            PresetFlatWorldScreen.LOGGER.error("Error while parsing flat world string => {}", exception9.getMessage());
            return null;
        }
        if (bul7 == null) {
            PresetFlatWorldScreen.LOGGER.error("Error while parsing flat world string => Unknown block, {}", string2);
            return null;
        }
        final FlatLayerInfo cpb9 = new FlatLayerInfo(integer4, bul7);
        cpb9.setStart(integer);
        return cpb9;
    }
    
    private static List<FlatLayerInfo> getLayersInfoFromString(final String string) {
        final List<FlatLayerInfo> list2 = (List<FlatLayerInfo>)Lists.newArrayList();
        final String[] arr3 = string.split(",");
        int integer4 = 0;
        for (final String string2 : arr3) {
            final FlatLayerInfo cpb9 = getLayerInfoFromString(string2, integer4);
            if (cpb9 == null) {
                return (List<FlatLayerInfo>)Collections.emptyList();
            }
            list2.add(cpb9);
            integer4 += cpb9.getHeight();
        }
        return list2;
    }
    
    public static FlatLevelGeneratorSettings fromString(final Registry<Biome> gm, final String string, final FlatLevelGeneratorSettings cpc) {
        final Iterator<String> iterator4 = (Iterator<String>)Splitter.on(';').split((CharSequence)string).iterator();
        if (!iterator4.hasNext()) {
            return FlatLevelGeneratorSettings.getDefault(gm);
        }
        final List<FlatLayerInfo> list5 = getLayersInfoFromString((String)iterator4.next());
        if (list5.isEmpty()) {
            return FlatLevelGeneratorSettings.getDefault(gm);
        }
        final FlatLevelGeneratorSettings cpc2 = cpc.withLayers(list5, cpc.structureSettings());
        ResourceKey<Biome> vj7 = Biomes.PLAINS;
        if (iterator4.hasNext()) {
            try {
                final ResourceLocation vk8 = new ResourceLocation((String)iterator4.next());
                vj7 = ResourceKey.<Biome>create(Registry.BIOME_REGISTRY, vk8);
                gm.getOptional(vj7).orElseThrow(() -> new IllegalArgumentException(new StringBuilder().append("Invalid Biome: ").append(vk8).toString()));
            }
            catch (Exception exception8) {
                PresetFlatWorldScreen.LOGGER.error("Error while parsing flat world string => {}", exception8.getMessage());
            }
        }
        final ResourceKey<Biome> vj8 = vj7;
        cpc2.setBiome((Supplier<Biome>)(() -> gm.getOrThrow(vj8)));
        return cpc2;
    }
    
    private static String save(final Registry<Biome> gm, final FlatLevelGeneratorSettings cpc) {
        final StringBuilder stringBuilder3 = new StringBuilder();
        for (int integer4 = 0; integer4 < cpc.getLayersInfo().size(); ++integer4) {
            if (integer4 > 0) {
                stringBuilder3.append(",");
            }
            stringBuilder3.append(cpc.getLayersInfo().get(integer4));
        }
        stringBuilder3.append(";");
        stringBuilder3.append(gm.getKey(cpc.getBiome()));
        return stringBuilder3.toString();
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.shareText = new TranslatableComponent("createWorld.customize.presets.share");
        this.listText = new TranslatableComponent("createWorld.customize.presets.list");
        (this.export = new EditBox(this.font, 50, 40, this.width - 100, 20, this.shareText)).setMaxLength(1230);
        final Registry<Biome> gm2 = this.parent.parent.worldGenSettingsComponent.registryHolder().registryOrThrow(Registry.BIOME_REGISTRY);
        this.export.setValue(save(gm2, this.parent.settings()));
        this.settings = this.parent.settings();
        this.children.add(this.export);
        this.list = new PresetsList();
        this.children.add(this.list);
        final FlatLevelGeneratorSettings cpc4;
        this.selectButton = this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 28, 150, 20, new TranslatableComponent("createWorld.customize.presets.select"), dlg -> {
            cpc4 = fromString(gm2, this.export.getValue(), this.settings);
            this.parent.setConfig(cpc4);
            this.minecraft.setScreen(this.parent);
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height - 28, 150, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.parent)));
        this.updateButtonValidity(this.list.getSelected() != null);
    }
    
    @Override
    public boolean mouseScrolled(final double double1, final double double2, final double double3) {
        return this.list.mouseScrolled(double1, double2, double3);
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.export.getValue();
        this.init(djw, integer2, integer3);
        this.export.setValue(string5);
    }
    
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.list.render(dfj, integer2, integer3, float4);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(0.0f, 0.0f, 400.0f);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 8, 16777215);
        GuiComponent.drawString(dfj, this.font, this.shareText, 50, 30, 10526880);
        GuiComponent.drawString(dfj, this.font, this.listText, 50, 70, 10526880);
        RenderSystem.popMatrix();
        this.export.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public void tick() {
        this.export.tick();
        super.tick();
    }
    
    public void updateButtonValidity(final boolean boolean1) {
        this.selectButton.active = (boolean1 || this.export.getValue().length() > 1);
    }
    
    private static void preset(final Component nr, final ItemLike brt, final ResourceKey<Biome> vj, final List<StructureFeature<?>> list, final boolean boolean5, final boolean boolean6, final boolean boolean7, final FlatLayerInfo... arr) {
        PresetFlatWorldScreen.PRESETS.add(new PresetInfo(brt.asItem(), nr, (Function<Registry<Biome>, FlatLevelGeneratorSettings>)(gm -> {
            final Map<StructureFeature<?>, StructureFeatureConfiguration> map8 = (Map<StructureFeature<?>, StructureFeatureConfiguration>)Maps.newHashMap();
            for (final StructureFeature<?> ckx10 : list) {
                map8.put((Object)ckx10, StructureSettings.DEFAULTS.get((Object)ckx10));
            }
            final StructureSettings chs9 = new StructureSettings((Optional<StrongholdConfiguration>)(boolean5 ? Optional.of((Object)StructureSettings.DEFAULT_STRONGHOLD) : Optional.empty()), map8);
            final FlatLevelGeneratorSettings cpc10 = new FlatLevelGeneratorSettings(chs9, gm);
            if (boolean6) {
                cpc10.setDecoration();
            }
            if (boolean7) {
                cpc10.setAddLakes();
            }
            for (int integer11 = arr.length - 1; integer11 >= 0; --integer11) {
                cpc10.getLayersInfo().add((Object)arr[integer11]);
            }
            cpc10.setBiome((Supplier<Biome>)(() -> gm.getOrThrow(vj)));
            cpc10.updateLayers();
            return cpc10.withStructureSettings(chs9);
        })));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PRESETS = (List)Lists.newArrayList();
        preset(new TranslatableComponent("createWorld.customize.preset.classic_flat"), Blocks.GRASS_BLOCK, Biomes.PLAINS, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.VILLAGE }), false, false, false, new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(2, Blocks.DIRT), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.tunnelers_dream"), Blocks.STONE, Biomes.MOUNTAINS, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.MINESHAFT }), true, true, false, new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(230, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.water_world"), Items.WATER_BUCKET, Biomes.DEEP_OCEAN, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.OCEAN_RUIN, StructureFeature.SHIPWRECK, StructureFeature.OCEAN_MONUMENT }), false, false, false, new FlatLayerInfo(90, Blocks.WATER), new FlatLayerInfo(5, Blocks.SAND), new FlatLayerInfo(5, Blocks.DIRT), new FlatLayerInfo(5, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.overworld"), Blocks.GRASS, Biomes.PLAINS, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.VILLAGE, StructureFeature.MINESHAFT, StructureFeature.PILLAGER_OUTPOST, StructureFeature.RUINED_PORTAL }), true, true, true, new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.snowy_kingdom"), Blocks.SNOW, Biomes.SNOWY_TUNDRA, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.VILLAGE, StructureFeature.IGLOO }), false, false, false, new FlatLayerInfo(1, Blocks.SNOW), new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(59, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.bottomless_pit"), Items.FEATHER, Biomes.PLAINS, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.VILLAGE }), false, false, false, new FlatLayerInfo(1, Blocks.GRASS_BLOCK), new FlatLayerInfo(3, Blocks.DIRT), new FlatLayerInfo(2, Blocks.COBBLESTONE));
        preset(new TranslatableComponent("createWorld.customize.preset.desert"), Blocks.SAND, Biomes.DESERT, (List<StructureFeature<?>>)Arrays.asList((Object[])new StructureFeature[] { StructureFeature.VILLAGE, StructureFeature.DESERT_PYRAMID, StructureFeature.MINESHAFT }), true, true, false, new FlatLayerInfo(8, Blocks.SAND), new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.redstone_ready"), Items.REDSTONE, Biomes.DESERT, (List<StructureFeature<?>>)Collections.emptyList(), false, false, false, new FlatLayerInfo(52, Blocks.SANDSTONE), new FlatLayerInfo(3, Blocks.STONE), new FlatLayerInfo(1, Blocks.BEDROCK));
        preset(new TranslatableComponent("createWorld.customize.preset.the_void"), Blocks.BARRIER, Biomes.THE_VOID, (List<StructureFeature<?>>)Collections.emptyList(), false, true, false, new FlatLayerInfo(1, Blocks.AIR));
    }
    
    class PresetsList extends ObjectSelectionList<Entry> {
        public PresetsList() {
            super(PresetFlatWorldScreen.this.minecraft, PresetFlatWorldScreen.this.width, PresetFlatWorldScreen.this.height, 80, PresetFlatWorldScreen.this.height - 37, 24);
            for (int integer3 = 0; integer3 < PresetFlatWorldScreen.PRESETS.size(); ++integer3) {
                this.addEntry(new Entry());
            }
        }
        
        @Override
        public void setSelected(@Nullable final Entry a) {
            super.setSelected(a);
            if (a != null) {
                NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.select", new Object[] { ((PresetInfo)PresetFlatWorldScreen.PRESETS.get(this.children().indexOf(a))).getName() }).getString());
            }
            PresetFlatWorldScreen.this.updateButtonValidity(a != null);
        }
        
        @Override
        protected boolean isFocused() {
            return PresetFlatWorldScreen.this.getFocused() == this;
        }
        
        @Override
        public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
            if (super.keyPressed(integer1, integer2, integer3)) {
                return true;
            }
            if ((integer1 == 257 || integer1 == 335) && this.getSelected() != null) {
                this.getSelected().select();
            }
            return false;
        }
        
        public class Entry extends ObjectSelectionList.Entry<Entry> {
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                final PresetInfo a12 = (PresetInfo)PresetFlatWorldScreen.PRESETS.get(integer2);
                this.blitSlot(dfj, integer4, integer3, a12.icon);
                PresetFlatWorldScreen.this.font.draw(dfj, a12.name, (float)(integer4 + 18 + 5), (float)(integer3 + 6), 16777215);
            }
            
            public boolean mouseClicked(final double double1, final double double2, final int integer) {
                if (integer == 0) {
                    this.select();
                }
                return false;
            }
            
            private void select() {
                PresetsList.this.setSelected(this);
                final PresetInfo a2 = (PresetInfo)PresetFlatWorldScreen.PRESETS.get(PresetsList.this.children().indexOf(this));
                final Registry<Biome> gm3 = PresetFlatWorldScreen.this.parent.parent.worldGenSettingsComponent.registryHolder().registryOrThrow(Registry.BIOME_REGISTRY);
                PresetFlatWorldScreen.this.settings = (FlatLevelGeneratorSettings)a2.settings.apply(gm3);
                PresetFlatWorldScreen.this.export.setValue(save(gm3, PresetFlatWorldScreen.this.settings));
                PresetFlatWorldScreen.this.export.moveCursorToStart();
            }
            
            private void blitSlot(final PoseStack dfj, final int integer2, final int integer3, final Item blu) {
                this.blitSlotBg(dfj, integer2 + 1, integer3 + 1);
                RenderSystem.enableRescaleNormal();
                PresetFlatWorldScreen.this.itemRenderer.renderGuiItem(new ItemStack(blu), integer2 + 2, integer3 + 2);
                RenderSystem.disableRescaleNormal();
            }
            
            private void blitSlotBg(final PoseStack dfj, final int integer2, final int integer3) {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                PresetsList.this.minecraft.getTextureManager().bind(GuiComponent.STATS_ICON_LOCATION);
                GuiComponent.blit(dfj, integer2, integer3, PresetFlatWorldScreen.this.getBlitOffset(), 0.0f, 0.0f, 18, 18, 128, 128);
            }
        }
    }
    
    static class PresetInfo {
        public final Item icon;
        public final Component name;
        public final Function<Registry<Biome>, FlatLevelGeneratorSettings> settings;
        
        public PresetInfo(final Item blu, final Component nr, final Function<Registry<Biome>, FlatLevelGeneratorSettings> function) {
            this.icon = blu;
            this.name = nr;
            this.settings = function;
        }
        
        public Component getName() {
            return this.name;
        }
    }
}
