package net.minecraft.util.datafix.fixes;

import java.util.stream.Stream;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class SavedDataVillageCropFix extends DataFix {
    public SavedDataVillageCropFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.writeFixAndRead("SavedDataVillageCropFix", this.getInputSchema().getType(References.STRUCTURE_FEATURE), this.getOutputSchema().getType(References.STRUCTURE_FEATURE), this::fixTag);
    }
    
    private <T> Dynamic<T> fixTag(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.update("Children", SavedDataVillageCropFix::updateChildren);
    }
    
    private static <T> Dynamic<T> updateChildren(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.asStreamOpt().map(SavedDataVillageCropFix::updateChildren).map(dynamic::createList).result().orElse(dynamic);
    }
    
    private static Stream<? extends Dynamic<?>> updateChildren(final Stream<? extends Dynamic<?>> stream) {
        return stream.map(dynamic -> {
            final String string2 = dynamic.get("id").asString("");
            if ("ViF".equals(string2)) {
                return SavedDataVillageCropFix.updateSingleField((com.mojang.serialization.Dynamic<Object>)dynamic);
            }
            if ("ViDF".equals(string2)) {
                return SavedDataVillageCropFix.updateDoubleField((com.mojang.serialization.Dynamic<Object>)dynamic);
            }
            return dynamic;
        });
    }
    
    private static <T> Dynamic<T> updateSingleField(Dynamic<T> dynamic) {
        dynamic = SavedDataVillageCropFix.<T>updateCrop(dynamic, "CA");
        return SavedDataVillageCropFix.<T>updateCrop(dynamic, "CB");
    }
    
    private static <T> Dynamic<T> updateDoubleField(Dynamic<T> dynamic) {
        dynamic = SavedDataVillageCropFix.<T>updateCrop(dynamic, "CA");
        dynamic = SavedDataVillageCropFix.<T>updateCrop(dynamic, "CB");
        dynamic = SavedDataVillageCropFix.<T>updateCrop(dynamic, "CC");
        return SavedDataVillageCropFix.<T>updateCrop(dynamic, "CD");
    }
    
    private static <T> Dynamic<T> updateCrop(final Dynamic<T> dynamic, final String string) {
        if (dynamic.get(string).asNumber().result().isPresent()) {
            return (Dynamic<T>)dynamic.set(string, (Dynamic)BlockStateData.getTag(dynamic.get(string).asInt(0) << 4));
        }
        return dynamic;
    }
}
