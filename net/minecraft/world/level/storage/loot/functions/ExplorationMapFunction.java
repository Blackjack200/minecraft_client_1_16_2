package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Locale;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.apache.logging.log4j.Logger;

public class ExplorationMapFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER;
    public static final StructureFeature<?> DEFAULT_FEATURE;
    public static final MapDecoration.Type DEFAULT_DECORATION;
    private final StructureFeature<?> destination;
    private final MapDecoration.Type mapDecoration;
    private final byte zoom;
    private final int searchRadius;
    private final boolean skipKnownStructures;
    
    private ExplorationMapFunction(final LootItemCondition[] arr, final StructureFeature<?> ckx, final MapDecoration.Type a, final byte byte4, final int integer, final boolean boolean6) {
        super(arr);
        this.destination = ckx;
        this.mapDecoration = a;
        this.zoom = byte4;
        this.searchRadius = integer;
        this.skipKnownStructures = boolean6;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.EXPLORATION_MAP;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.ORIGIN);
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.getItem() != Items.MAP) {
            return bly;
        }
        final Vec3 dck4 = cys.<Vec3>getParamOrNull(LootContextParams.ORIGIN);
        if (dck4 != null) {
            final ServerLevel aag5 = cys.getLevel();
            final BlockPos fx6 = aag5.findNearestMapFeature(this.destination, new BlockPos(dck4), this.searchRadius, this.skipKnownStructures);
            if (fx6 != null) {
                final ItemStack bly2 = MapItem.create(aag5, fx6.getX(), fx6.getZ(), this.zoom, true, true);
                MapItem.renderBiomePreviewMap(aag5, bly2);
                MapItemSavedData.addTargetDecoration(bly2, fx6, "+", this.mapDecoration);
                bly2.setHoverName(new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
                return bly2;
            }
        }
        return bly;
    }
    
    public static Builder makeExplorationMap() {
        return new Builder();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        DEFAULT_FEATURE = StructureFeature.BURIED_TREASURE;
        DEFAULT_DECORATION = MapDecoration.Type.MANSION;
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private StructureFeature<?> destination;
        private MapDecoration.Type mapDecoration;
        private byte zoom;
        private int searchRadius;
        private boolean skipKnownStructures;
        
        public Builder() {
            this.destination = ExplorationMapFunction.DEFAULT_FEATURE;
            this.mapDecoration = ExplorationMapFunction.DEFAULT_DECORATION;
            this.zoom = 2;
            this.searchRadius = 50;
            this.skipKnownStructures = true;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder setDestination(final StructureFeature<?> ckx) {
            this.destination = ckx;
            return this;
        }
        
        public Builder setMapDecoration(final MapDecoration.Type a) {
            this.mapDecoration = a;
            return this;
        }
        
        public Builder setZoom(final byte byte1) {
            this.zoom = byte1;
            return this;
        }
        
        public Builder setSkipKnownStructures(final boolean boolean1) {
            this.skipKnownStructures = boolean1;
            return this;
        }
        
        public LootItemFunction build() {
            return new ExplorationMapFunction(this.getConditions(), this.destination, this.mapDecoration, this.zoom, this.searchRadius, this.skipKnownStructures, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<ExplorationMapFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final ExplorationMapFunction dab, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dab, jsonSerializationContext);
            if (!dab.destination.equals(ExplorationMapFunction.DEFAULT_FEATURE)) {
                jsonObject.add("destination", jsonSerializationContext.serialize(dab.destination.getFeatureName()));
            }
            if (dab.mapDecoration != ExplorationMapFunction.DEFAULT_DECORATION) {
                jsonObject.add("decoration", jsonSerializationContext.serialize(dab.mapDecoration.toString().toLowerCase(Locale.ROOT)));
            }
            if (dab.zoom != 2) {
                jsonObject.addProperty("zoom", (Number)dab.zoom);
            }
            if (dab.searchRadius != 50) {
                jsonObject.addProperty("search_radius", (Number)dab.searchRadius);
            }
            if (!dab.skipKnownStructures) {
                jsonObject.addProperty("skip_existing_chunks", Boolean.valueOf(dab.skipKnownStructures));
            }
        }
        
        @Override
        public ExplorationMapFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final StructureFeature<?> ckx5 = readStructure(jsonObject);
            final String string6 = jsonObject.has("decoration") ? GsonHelper.getAsString(jsonObject, "decoration") : "mansion";
            MapDecoration.Type a7 = ExplorationMapFunction.DEFAULT_DECORATION;
            try {
                a7 = MapDecoration.Type.valueOf(string6.toUpperCase(Locale.ROOT));
            }
            catch (IllegalArgumentException illegalArgumentException8) {
                ExplorationMapFunction.LOGGER.error(new StringBuilder().append("Error while parsing loot table decoration entry. Found {}. Defaulting to ").append(ExplorationMapFunction.DEFAULT_DECORATION).toString(), string6);
            }
            final byte byte8 = GsonHelper.getAsByte(jsonObject, "zoom", (byte)2);
            final int integer9 = GsonHelper.getAsInt(jsonObject, "search_radius", 50);
            final boolean boolean10 = GsonHelper.getAsBoolean(jsonObject, "skip_existing_chunks", true);
            return new ExplorationMapFunction(arr, ckx5, a7, byte8, integer9, boolean10, null);
        }
        
        private static StructureFeature<?> readStructure(final JsonObject jsonObject) {
            if (jsonObject.has("destination")) {
                final String string2 = GsonHelper.getAsString(jsonObject, "destination");
                final StructureFeature<?> ckx3 = StructureFeature.STRUCTURES_REGISTRY.get(string2.toLowerCase(Locale.ROOT));
                if (ckx3 != null) {
                    return ckx3;
                }
            }
            return ExplorationMapFunction.DEFAULT_FEATURE;
        }
    }
}
