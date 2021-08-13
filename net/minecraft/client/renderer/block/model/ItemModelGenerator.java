package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import com.mojang.datafixers.util.Either;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import java.util.function.Function;
import java.util.List;

public class ItemModelGenerator {
    public static final List<String> LAYERS;
    
    public BlockModel generateBlockModel(final Function<Material, TextureAtlasSprite> function, final BlockModel eax) {
        final Map<String, Either<Material, String>> map4 = (Map<String, Either<Material, String>>)Maps.newHashMap();
        final List<BlockElement> list5 = (List<BlockElement>)Lists.newArrayList();
        for (int integer6 = 0; integer6 < ItemModelGenerator.LAYERS.size(); ++integer6) {
            final String string7 = (String)ItemModelGenerator.LAYERS.get(integer6);
            if (!eax.hasTexture(string7)) {
                break;
            }
            final Material elj8 = eax.getMaterial(string7);
            map4.put(string7, Either.left((Object)elj8));
            final TextureAtlasSprite eju9 = (TextureAtlasSprite)function.apply(elj8);
            list5.addAll((Collection)this.processFrames(integer6, string7, eju9));
        }
        map4.put("particle", (eax.hasTexture("particle") ? Either.left((Object)eax.getMaterial("particle")) : map4.get("layer0")));
        final BlockModel eax2 = new BlockModel(null, list5, map4, false, eax.getGuiLight(), eax.getTransforms(), eax.getOverrides());
        eax2.name = eax.name;
        return eax2;
    }
    
    private List<BlockElement> processFrames(final int integer, final String string, final TextureAtlasSprite eju) {
        final Map<Direction, BlockElementFace> map5 = (Map<Direction, BlockElementFace>)Maps.newHashMap();
        map5.put(Direction.SOUTH, new BlockElementFace(null, integer, string, new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0)));
        map5.put(Direction.NORTH, new BlockElementFace(null, integer, string, new BlockFaceUV(new float[] { 16.0f, 0.0f, 0.0f, 16.0f }, 0)));
        final List<BlockElement> list6 = (List<BlockElement>)Lists.newArrayList();
        list6.add(new BlockElement(new Vector3f(0.0f, 0.0f, 7.5f), new Vector3f(16.0f, 16.0f, 8.5f), map5, null, true));
        list6.addAll((Collection)this.createSideElements(eju, string, integer));
        return list6;
    }
    
    private List<BlockElement> createSideElements(final TextureAtlasSprite eju, final String string, final int integer) {
        final float float5 = (float)eju.getWidth();
        final float float6 = (float)eju.getHeight();
        final List<BlockElement> list7 = (List<BlockElement>)Lists.newArrayList();
        for (final Span a9 : this.getSpans(eju)) {
            float float7 = 0.0f;
            float float8 = 0.0f;
            float float9 = 0.0f;
            float float10 = 0.0f;
            float float11 = 0.0f;
            float float12 = 0.0f;
            float float13 = 0.0f;
            float float14 = 0.0f;
            final float float15 = 16.0f / float5;
            final float float16 = 16.0f / float6;
            final float float17 = (float)a9.getMin();
            final float float18 = (float)a9.getMax();
            final float float19 = (float)a9.getAnchor();
            final SpanFacing b23 = a9.getFacing();
            switch (b23) {
                case UP: {
                    float11 = (float7 = float17);
                    float12 = (float9 = float18 + 1.0f);
                    float13 = (float8 = float19);
                    float10 = float19;
                    float14 = float19 + 1.0f;
                    break;
                }
                case DOWN: {
                    float13 = float19;
                    float14 = float19 + 1.0f;
                    float11 = (float7 = float17);
                    float12 = (float9 = float18 + 1.0f);
                    float8 = float19 + 1.0f;
                    float10 = float19 + 1.0f;
                    break;
                }
                case LEFT: {
                    float11 = (float7 = float19);
                    float9 = float19;
                    float12 = float19 + 1.0f;
                    float14 = (float8 = float17);
                    float13 = (float10 = float18 + 1.0f);
                    break;
                }
                case RIGHT: {
                    float11 = float19;
                    float12 = float19 + 1.0f;
                    float7 = float19 + 1.0f;
                    float9 = float19 + 1.0f;
                    float14 = (float8 = float17);
                    float13 = (float10 = float18 + 1.0f);
                    break;
                }
            }
            float7 *= float15;
            float9 *= float15;
            float8 *= float16;
            float10 *= float16;
            float8 = 16.0f - float8;
            float10 = 16.0f - float10;
            float11 *= float15;
            float12 *= float15;
            float13 *= float16;
            float14 *= float16;
            final Map<Direction, BlockElementFace> map24 = (Map<Direction, BlockElementFace>)Maps.newHashMap();
            map24.put(b23.getDirection(), new BlockElementFace(null, integer, string, new BlockFaceUV(new float[] { float11, float13, float12, float14 }, 0)));
            switch (b23) {
                case UP: {
                    list7.add(new BlockElement(new Vector3f(float7, float8, 7.5f), new Vector3f(float9, float8, 8.5f), map24, null, true));
                    continue;
                }
                case DOWN: {
                    list7.add(new BlockElement(new Vector3f(float7, float10, 7.5f), new Vector3f(float9, float10, 8.5f), map24, null, true));
                    continue;
                }
                case LEFT: {
                    list7.add(new BlockElement(new Vector3f(float7, float8, 7.5f), new Vector3f(float7, float10, 8.5f), map24, null, true));
                    continue;
                }
                case RIGHT: {
                    list7.add(new BlockElement(new Vector3f(float9, float8, 7.5f), new Vector3f(float9, float10, 8.5f), map24, null, true));
                    continue;
                }
            }
        }
        return list7;
    }
    
    private List<Span> getSpans(final TextureAtlasSprite eju) {
        final int integer3 = eju.getWidth();
        final int integer4 = eju.getHeight();
        final List<Span> list5 = (List<Span>)Lists.newArrayList();
        for (int integer5 = 0; integer5 < eju.getFrameCount(); ++integer5) {
            for (int integer6 = 0; integer6 < integer4; ++integer6) {
                for (int integer7 = 0; integer7 < integer3; ++integer7) {
                    final boolean boolean9 = !this.isTransparent(eju, integer5, integer7, integer6, integer3, integer4);
                    this.checkTransition(SpanFacing.UP, list5, eju, integer5, integer7, integer6, integer3, integer4, boolean9);
                    this.checkTransition(SpanFacing.DOWN, list5, eju, integer5, integer7, integer6, integer3, integer4, boolean9);
                    this.checkTransition(SpanFacing.LEFT, list5, eju, integer5, integer7, integer6, integer3, integer4, boolean9);
                    this.checkTransition(SpanFacing.RIGHT, list5, eju, integer5, integer7, integer6, integer3, integer4, boolean9);
                }
            }
        }
        return list5;
    }
    
    private void checkTransition(final SpanFacing b, final List<Span> list, final TextureAtlasSprite eju, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9) {
        final boolean boolean10 = this.isTransparent(eju, integer4, integer5 + b.getXOffset(), integer6 + b.getYOffset(), integer7, integer8) && boolean9;
        if (boolean10) {
            this.createOrExpandSpan(list, b, integer5, integer6);
        }
    }
    
    private void createOrExpandSpan(final List<Span> list, final SpanFacing b, final int integer3, final int integer4) {
        Span a6 = null;
        for (final Span a7 : list) {
            if (a7.getFacing() != b) {
                continue;
            }
            final int integer5 = b.isHorizontal() ? integer4 : integer3;
            if (a7.getAnchor() == integer5) {
                a6 = a7;
                break;
            }
        }
        final int integer6 = b.isHorizontal() ? integer4 : integer3;
        final int integer7 = b.isHorizontal() ? integer3 : integer4;
        if (a6 == null) {
            list.add(new Span(b, integer7, integer6));
        }
        else {
            a6.expand(integer7);
        }
    }
    
    private boolean isTransparent(final TextureAtlasSprite eju, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        return integer3 < 0 || integer4 < 0 || integer3 >= integer5 || integer4 >= integer6 || eju.isTransparent(integer2, integer3, integer4);
    }
    
    static {
        LAYERS = (List)Lists.newArrayList((Object[])new String[] { "layer0", "layer1", "layer2", "layer3", "layer4" });
    }
    
    enum SpanFacing {
        UP(Direction.UP, 0, -1), 
        DOWN(Direction.DOWN, 0, 1), 
        LEFT(Direction.EAST, -1, 0), 
        RIGHT(Direction.WEST, 1, 0);
        
        private final Direction direction;
        private final int xOffset;
        private final int yOffset;
        
        private SpanFacing(final Direction gc, final int integer4, final int integer5) {
            this.direction = gc;
            this.xOffset = integer4;
            this.yOffset = integer5;
        }
        
        public Direction getDirection() {
            return this.direction;
        }
        
        public int getXOffset() {
            return this.xOffset;
        }
        
        public int getYOffset() {
            return this.yOffset;
        }
        
        private boolean isHorizontal() {
            return this == SpanFacing.DOWN || this == SpanFacing.UP;
        }
    }
    
    static class Span {
        private final SpanFacing facing;
        private int min;
        private int max;
        private final int anchor;
        
        public Span(final SpanFacing b, final int integer2, final int integer3) {
            this.facing = b;
            this.min = integer2;
            this.max = integer2;
            this.anchor = integer3;
        }
        
        public void expand(final int integer) {
            if (integer < this.min) {
                this.min = integer;
            }
            else if (integer > this.max) {
                this.max = integer;
            }
        }
        
        public SpanFacing getFacing() {
            return this.facing;
        }
        
        public int getMin() {
            return this.min;
        }
        
        public int getMax() {
            return this.max;
        }
        
        public int getAnchor() {
            return this.anchor;
        }
    }
}
