package net.minecraft.client.renderer;

import net.minecraft.client.resources.model.ModelBakery;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.SortedMap;

public class RenderBuffers {
    private final ChunkBufferBuilderPack fixedBufferPack;
    private final SortedMap<RenderType, BufferBuilder> fixedBuffers;
    private final MultiBufferSource.BufferSource bufferSource;
    private final MultiBufferSource.BufferSource crumblingBufferSource;
    private final OutlineBufferSource outlineBufferSource;
    
    public RenderBuffers() {
        this.fixedBufferPack = new ChunkBufferBuilderPack();
        this.fixedBuffers = Util.make((SortedMap)new Object2ObjectLinkedOpenHashMap(), (java.util.function.Consumer<SortedMap>)(object2ObjectLinkedOpenHashMap -> {
            object2ObjectLinkedOpenHashMap.put(Sheets.solidBlockSheet(), this.fixedBufferPack.builder(RenderType.solid()));
            object2ObjectLinkedOpenHashMap.put(Sheets.cutoutBlockSheet(), this.fixedBufferPack.builder(RenderType.cutout()));
            object2ObjectLinkedOpenHashMap.put(Sheets.bannerSheet(), this.fixedBufferPack.builder(RenderType.cutoutMipped()));
            object2ObjectLinkedOpenHashMap.put(Sheets.translucentCullBlockSheet(), this.fixedBufferPack.builder(RenderType.translucent()));
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, Sheets.shieldSheet());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, Sheets.bedSheet());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, Sheets.shulkerBoxSheet());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, Sheets.signSheet());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, Sheets.chestSheet());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.translucentNoCrumbling());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.armorGlint());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.armorEntityGlint());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.glint());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.glintDirect());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.glintTranslucent());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.entityGlint());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.entityGlintDirect());
            put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, RenderType.waterMask());
            ModelBakery.DESTROY_TYPES.forEach(eag -> put((Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder>)object2ObjectLinkedOpenHashMap, eag));
        }));
        this.bufferSource = MultiBufferSource.immediateWithBuffers((Map<RenderType, BufferBuilder>)this.fixedBuffers, new BufferBuilder(256));
        this.crumblingBufferSource = MultiBufferSource.immediate(new BufferBuilder(256));
        this.outlineBufferSource = new OutlineBufferSource(this.bufferSource);
    }
    
    private static void put(final Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> object2ObjectLinkedOpenHashMap, final RenderType eag) {
        object2ObjectLinkedOpenHashMap.put(eag, new BufferBuilder(eag.bufferSize()));
    }
    
    public ChunkBufferBuilderPack fixedBufferPack() {
        return this.fixedBufferPack;
    }
    
    public MultiBufferSource.BufferSource bufferSource() {
        return this.bufferSource;
    }
    
    public MultiBufferSource.BufferSource crumblingBufferSource() {
        return this.crumblingBufferSource;
    }
    
    public OutlineBufferSource outlineBufferSource() {
        return this.outlineBufferSource;
    }
}
