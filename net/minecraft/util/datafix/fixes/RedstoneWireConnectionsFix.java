package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class RedstoneWireConnectionsFix extends DataFix {
    public RedstoneWireConnectionsFix(final Schema schema) {
        super(schema, false);
    }
    
    protected TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        return this.fixTypeEverywhereTyped("RedstoneConnectionsFix", schema2.getType(References.BLOCK_STATE), typed -> typed.update(DSL.remainderFinder(), this::updateRedstoneConnections));
    }
    
    private <T> Dynamic<T> updateRedstoneConnections(final Dynamic<T> dynamic) {
        final boolean boolean3 = dynamic.get("Name").asString().result().filter("minecraft:redstone_wire"::equals).isPresent();
        if (!boolean3) {
            return dynamic;
        }
        return (Dynamic<T>)dynamic.update("Properties", dynamic -> {
            final String string2 = dynamic.get("east").asString("none");
            final String string3 = dynamic.get("west").asString("none");
            final String string4 = dynamic.get("north").asString("none");
            final String string5 = dynamic.get("south").asString("none");
            final boolean boolean6 = isConnected(string2) || isConnected(string3);
            final boolean boolean7 = isConnected(string4) || isConnected(string5);
            final String string6 = (!isConnected(string2) && !boolean7) ? "side" : string2;
            final String string7 = (!isConnected(string3) && !boolean7) ? "side" : string3;
            final String string8 = (!isConnected(string4) && !boolean6) ? "side" : string4;
            final String string9 = (!isConnected(string5) && !boolean6) ? "side" : string5;
            return dynamic.update("east", dynamic -> dynamic.createString(string6)).update("west", dynamic -> dynamic.createString(string7)).update("north", dynamic -> dynamic.createString(string8)).update("south", dynamic -> dynamic.createString(string9));
        });
    }
    
    private static boolean isConnected(final String string) {
        return !"none".equals(string);
    }
}
