package net.minecraft.data.models.blockstates;

import com.google.common.collect.ImmutableList;
import java.util.Map;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.gson.JsonObject;
import java.util.TreeMap;
import java.util.stream.Stream;
import com.mojang.datafixers.util.Pair;
import com.google.gson.JsonElement;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import java.util.List;
import net.minecraft.world.level.block.Block;

public class MultiVariantGenerator implements BlockStateGenerator {
    private final Block block;
    private final List<Variant> baseVariants;
    private final Set<Property<?>> seenProperties;
    private final List<PropertyDispatch> declaredPropertySets;
    
    private MultiVariantGenerator(final Block bul, final List<Variant> list) {
        this.seenProperties = (Set<Property<?>>)Sets.newHashSet();
        this.declaredPropertySets = (List<PropertyDispatch>)Lists.newArrayList();
        this.block = bul;
        this.baseVariants = list;
    }
    
    public MultiVariantGenerator with(final PropertyDispatch ip) {
        ip.getDefinedProperties().forEach(cfg -> {
            if (this.block.getStateDefinition().getProperty(cfg.getName()) != cfg) {
                throw new IllegalStateException(new StringBuilder().append("Property ").append(cfg).append(" is not defined for block ").append(this.block).toString());
            }
            if (!this.seenProperties.add(cfg)) {
                throw new IllegalStateException(new StringBuilder().append("Values of property ").append(cfg).append(" already defined for block ").append(this.block).toString());
            }
        });
        this.declaredPropertySets.add(ip);
        return this;
    }
    
    public JsonElement get() {
        Stream<Pair<Selector, List<Variant>>> stream2 = (Stream<Pair<Selector, List<Variant>>>)Stream.of(Pair.of((Object)Selector.empty(), (Object)this.baseVariants));
        for (final PropertyDispatch ip4 : this.declaredPropertySets) {
            final Map<Selector, List<Variant>> map5 = ip4.getEntries();
            stream2 = (Stream<Pair<Selector, List<Variant>>>)stream2.flatMap(pair -> map5.entrySet().stream().map5(entry -> {
                final Selector iq3 = ((Selector)pair.getFirst()).extend((Selector)entry.getKey());
                final List<Variant> list4 = mergeVariants((List<Variant>)pair.getSecond(), (List<Variant>)entry.getValue());
                return Pair.of(iq3, list4);
            }));
        }
        final Map<String, JsonElement> map6 = (Map<String, JsonElement>)new TreeMap();
        stream2.forEach(pair -> {
            final JsonElement jsonElement = (JsonElement)map6.put(((Selector)pair.getFirst()).getKey(), Variant.convertList((List<Variant>)pair.getSecond()));
        });
        final JsonObject jsonObject4 = new JsonObject();
        jsonObject4.add("variants", (JsonElement)Util.<JsonElement>make((JsonElement)new JsonObject(), (java.util.function.Consumer<JsonElement>)(jsonObject -> map6.forEach(jsonObject::add))));
        return (JsonElement)jsonObject4;
    }
    
    private static List<Variant> mergeVariants(final List<Variant> list1, final List<Variant> list2) {
        final ImmutableList.Builder<Variant> builder3 = (ImmutableList.Builder<Variant>)ImmutableList.builder();
        list1.forEach(ir -> list2.forEach(ir3 -> builder3.add(Variant.merge(ir, ir3))));
        return (List<Variant>)builder3.build();
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public static MultiVariantGenerator multiVariant(final Block bul) {
        return new MultiVariantGenerator(bul, (List<Variant>)ImmutableList.of(Variant.variant()));
    }
    
    public static MultiVariantGenerator multiVariant(final Block bul, final Variant ir) {
        return new MultiVariantGenerator(bul, (List<Variant>)ImmutableList.of(ir));
    }
    
    public static MultiVariantGenerator multiVariant(final Block bul, final Variant... arr) {
        return new MultiVariantGenerator(bul, (List<Variant>)ImmutableList.copyOf((Object[])arr));
    }
}
