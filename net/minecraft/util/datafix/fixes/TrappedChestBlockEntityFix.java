package net.minecraft.util.datafix.fixes;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.DataFix;

public class TrappedChestBlockEntityFix extends DataFix {
    private static final Logger LOGGER;
    
    public TrappedChestBlockEntityFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final Type<?> type4 = type3.findFieldType("TileEntities");
        if (!(type4 instanceof List.ListType)) {
            throw new IllegalStateException("Tile entity type is not a list type.");
        }
        final List.ListType<?> listType5 = type4;
        final OpticFinder<? extends java.util.List<?>> opticFinder6 = DSL.fieldFinder("TileEntities", (Type)listType5);
        final Type<?> type5 = this.getInputSchema().getType(References.CHUNK);
        final OpticFinder<?> opticFinder7 = type5.findField("Level");
        final OpticFinder<?> opticFinder8 = opticFinder7.type().findField("Sections");
        final Type<?> type6 = opticFinder8.type();
        if (!(type6 instanceof List.ListType)) {
            throw new IllegalStateException("Expecting sections to be a list.");
        }
        final Type<?> type7 = ((List.ListType)type6).getElement();
        final OpticFinder<?> opticFinder9 = DSL.typeFinder((Type)type7);
        return TypeRewriteRule.seq(new AddNewChoices(this.getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY).makeRule(), this.fixTypeEverywhereTyped("Trapped Chest fix", (Type)type5, typed -> typed.updateTyped(opticFinder7, typed -> {
            final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder8);
            if (!optional6.isPresent()) {
                return typed;
            }
            final java.util.List<? extends Typed<?>> list7 = ((Typed)optional6.get()).getAllTyped(opticFinder9);
            final IntSet intSet8 = (IntSet)new IntOpenHashSet();
            for (final Typed<?> typed2 : list7) {
                final TrappedChestSection a11 = new TrappedChestSection(typed2, this.getInputSchema());
                if (a11.isSkippable()) {
                    continue;
                }
                for (int integer12 = 0; integer12 < 4096; ++integer12) {
                    final int integer13 = a11.getBlock(integer12);
                    if (a11.isTrappedChest(integer13)) {
                        intSet8.add(a11.getIndex() << 12 | integer12);
                    }
                }
            }
            final Dynamic<?> dynamic9 = typed.get(DSL.remainderFinder());
            final int integer14 = dynamic9.get("xPos").asInt(0);
            final int integer15 = dynamic9.get("zPos").asInt(0);
            final TaggedChoice.TaggedChoiceType<String> taggedChoiceType12 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.BLOCK_ENTITY);
            return typed.updateTyped(opticFinder6, typed -> typed.updateTyped(taggedChoiceType12.finder(), typed -> {
                final Dynamic<?> dynamic6 = typed.getOrCreate(DSL.remainderFinder());
                final int integer3 = dynamic6.get("x").asInt(0) - (integer14 << 4);
                final int integer4 = dynamic6.get("y").asInt(0);
                final int integer5 = dynamic6.get("z").asInt(0) - (integer15 << 4);
                if (intSet8.contains(LeavesFix.getIndex(integer3, integer4, integer5))) {
                    return typed.update(taggedChoiceType12.finder(), pair -> pair.mapFirst(string -> {
                        if (!Objects.equals(string, "minecraft:chest")) {
                            TrappedChestBlockEntityFix.LOGGER.warn("Block Entity was expected to be a chest");
                        }
                        return "minecraft:trapped_chest";
                    }));
                }
                return typed;
            }));
        })));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static final class TrappedChestSection extends LeavesFix.Section {
        @Nullable
        private IntSet chestIds;
        
        public TrappedChestSection(final Typed<?> typed, final Schema schema) {
            super(typed, schema);
        }
        
        @Override
        protected boolean skippable() {
            this.chestIds = (IntSet)new IntOpenHashSet();
            for (int integer2 = 0; integer2 < this.palette.size(); ++integer2) {
                final Dynamic<?> dynamic3 = this.palette.get(integer2);
                final String string4 = dynamic3.get("Name").asString("");
                if (Objects.equals(string4, "minecraft:trapped_chest")) {
                    this.chestIds.add(integer2);
                }
            }
            return this.chestIds.isEmpty();
        }
        
        public boolean isTrappedChest(final int integer) {
            return this.chestIds.contains(integer);
        }
    }
}
