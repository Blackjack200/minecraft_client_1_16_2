package net.minecraft.data.models.blockstates;

import java.util.function.Supplier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.block.Block;

public class MultiPartGenerator implements BlockStateGenerator {
    private final Block block;
    private final List<Entry> parts;
    
    private MultiPartGenerator(final Block bul) {
        this.parts = (List<Entry>)Lists.newArrayList();
        this.block = bul;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public static MultiPartGenerator multiPart(final Block bul) {
        return new MultiPartGenerator(bul);
    }
    
    public MultiPartGenerator with(final List<Variant> list) {
        this.parts.add(new Entry((List)list));
        return this;
    }
    
    public MultiPartGenerator with(final Variant ir) {
        return this.with((List<Variant>)ImmutableList.of(ir));
    }
    
    public MultiPartGenerator with(final Condition im, final List<Variant> list) {
        this.parts.add(new ConditionalEntry(im, (List)list));
        return this;
    }
    
    public MultiPartGenerator with(final Condition im, final Variant... arr) {
        return this.with(im, (List<Variant>)ImmutableList.copyOf((Object[])arr));
    }
    
    public MultiPartGenerator with(final Condition im, final Variant ir) {
        return this.with(im, (List<Variant>)ImmutableList.of(ir));
    }
    
    public JsonElement get() {
        final StateDefinition<Block, BlockState> cef2 = this.block.getStateDefinition();
        this.parts.forEach(b -> b.validate(cef2));
        final JsonArray jsonArray3 = new JsonArray();
        this.parts.stream().map(Entry::get).forEach(jsonArray3::add);
        final JsonObject jsonObject4 = new JsonObject();
        jsonObject4.add("multipart", (JsonElement)jsonArray3);
        return (JsonElement)jsonObject4;
    }
    
    static class Entry implements Supplier<JsonElement> {
        private final List<Variant> variants;
        
        private Entry(final List<Variant> list) {
            this.variants = list;
        }
        
        public void validate(final StateDefinition<?, ?> cef) {
        }
        
        public void decorate(final JsonObject jsonObject) {
        }
        
        public JsonElement get() {
            final JsonObject jsonObject2 = new JsonObject();
            this.decorate(jsonObject2);
            jsonObject2.add("apply", Variant.convertList(this.variants));
            return (JsonElement)jsonObject2;
        }
    }
    
    static class ConditionalEntry extends Entry {
        private final Condition condition;
        
        private ConditionalEntry(final Condition im, final List<Variant> list) {
            super((List)list);
            this.condition = im;
        }
        
        @Override
        public void validate(final StateDefinition<?, ?> cef) {
            this.condition.validate(cef);
        }
        
        @Override
        public void decorate(final JsonObject jsonObject) {
            jsonObject.add("when", (JsonElement)this.condition.get());
        }
    }
}
