package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.mojang.serialization.Dynamic;
import java.util.stream.Stream;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ItemLoreFix extends DataFix {
    public ItemLoreFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<?> opticFinder3 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("Item Lore componentize", (Type)type2, typed -> typed.updateTyped(opticFinder3, typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("display", dynamic -> dynamic.update("Lore", dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asStreamOpt().map(ItemLoreFix::fixLoreList).map(dynamic::createList).result(), dynamic))))));
    }
    
    private static <T> Stream<Dynamic<T>> fixLoreList(final Stream<Dynamic<T>> stream) {
        return (Stream<Dynamic<T>>)stream.map(dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asString().map(ItemLoreFix::fixLoreEntry).map(dynamic::createString).result(), dynamic));
    }
    
    private static String fixLoreEntry(final String string) {
        return Component.Serializer.toJson(new TextComponent(string));
    }
}
