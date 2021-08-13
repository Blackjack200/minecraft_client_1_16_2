package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DataFixUtils;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.commons.lang3.math.NumberUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.google.common.base.Splitter;
import com.mojang.datafixers.DataFix;

public class LevelFlatGeneratorInfoFix extends DataFix {
    private static final Splitter SPLITTER;
    private static final Splitter LAYER_SPLITTER;
    private static final Splitter OLD_AMOUNT_SPLITTER;
    private static final Splitter AMOUNT_SPLITTER;
    private static final Splitter BLOCK_SPLITTER;
    
    public LevelFlatGeneratorInfoFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("LevelFlatGeneratorInfoFix", this.getInputSchema().getType(References.LEVEL), typed -> typed.update(DSL.remainderFinder(), this::fix));
    }
    
    private Dynamic<?> fix(final Dynamic<?> dynamic) {
        if (dynamic.get("generatorName").asString("").equalsIgnoreCase("flat")) {
            return dynamic.update("generatorOptions", dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asString().map(this::fixString).map(dynamic::createString).result(), dynamic));
        }
        return dynamic;
    }
    
    @VisibleForTesting
    String fixString(final String string) {
        if (string.isEmpty()) {
            return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
        }
        final Iterator<String> iterator3 = (Iterator<String>)LevelFlatGeneratorInfoFix.SPLITTER.split((CharSequence)string).iterator();
        final String string2 = (String)iterator3.next();
        int integer5;
        String string3;
        if (iterator3.hasNext()) {
            integer5 = NumberUtils.toInt(string2, 0);
            string3 = (String)iterator3.next();
        }
        else {
            integer5 = 0;
            string3 = string2;
        }
        if (integer5 < 0 || integer5 > 3) {
            return "minecraft:bedrock,2*minecraft:dirt,minecraft:grass_block;1;village";
        }
        final StringBuilder stringBuilder7 = new StringBuilder();
        final Splitter splitter8 = (integer5 < 3) ? LevelFlatGeneratorInfoFix.OLD_AMOUNT_SPLITTER : LevelFlatGeneratorInfoFix.AMOUNT_SPLITTER;
        stringBuilder7.append((String)StreamSupport.stream(LevelFlatGeneratorInfoFix.LAYER_SPLITTER.split((CharSequence)string3).spliterator(), false).map(string -> {
            final List<String> list6 = (List<String>)splitter8.splitToList((CharSequence)string);
            int integer2;
            String string2;
            if (list6.size() == 2) {
                integer2 = NumberUtils.toInt((String)list6.get(0));
                string2 = (String)list6.get(1);
            }
            else {
                integer2 = 1;
                string2 = (String)list6.get(0);
            }
            final List<String> list7 = (List<String>)LevelFlatGeneratorInfoFix.BLOCK_SPLITTER.splitToList((CharSequence)string2);
            final int integer3 = ((String)list7.get(0)).equals("minecraft") ? 1 : 0;
            final String string3 = (String)list7.get(integer3);
            final int integer4 = (integer5 == 3) ? EntityBlockStateFix.getBlockId("minecraft:" + string3) : NumberUtils.toInt(string3, 0);
            final int integer5 = integer3 + 1;
            final int integer6 = (list7.size() > integer5) ? NumberUtils.toInt((String)list7.get(integer5), 0) : 0;
            return ((integer2 == 1) ? "" : new StringBuilder().append(integer2).append("*").toString()) + BlockStateData.getTag(integer4 << 4 | integer6).get("Name").asString("");
        }).collect(Collectors.joining(",")));
        while (iterator3.hasNext()) {
            stringBuilder7.append(';').append((String)iterator3.next());
        }
        return stringBuilder7.toString();
    }
    
    static {
        SPLITTER = Splitter.on(';').limit(5);
        LAYER_SPLITTER = Splitter.on(',');
        OLD_AMOUNT_SPLITTER = Splitter.on('x').limit(2);
        AMOUNT_SPLITTER = Splitter.on('*').limit(2);
        BLOCK_SPLITTER = Splitter.on(':').limit(3);
    }
}
