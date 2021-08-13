package net.minecraft.client.renderer.block.model;

import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.BlockModelRotation;
import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import java.util.stream.Collectors;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.resources.model.ModelBakery;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.BakedModel;
import java.util.List;

public class ItemOverrides {
    public static final ItemOverrides EMPTY;
    private final List<ItemOverride> overrides;
    private final List<BakedModel> overrideModels;
    
    private ItemOverrides() {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
        this.overrideModels = (List<BakedModel>)Collections.emptyList();
    }
    
    public ItemOverrides(final ModelBakery elk, final BlockModel eax, final Function<ResourceLocation, UnbakedModel> function, final List<ItemOverride> list) {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
        Collections.reverse((List)(this.overrideModels = (List<BakedModel>)list.stream().map(ebb -> {
            final UnbakedModel elq5 = (UnbakedModel)function.apply(ebb.getModel());
            if (Objects.equals(elq5, eax)) {
                return null;
            }
            return elk.bake(ebb.getModel(), BlockModelRotation.X0_Y0);
        }).collect(Collectors.toList())));
        for (int integer6 = list.size() - 1; integer6 >= 0; --integer6) {
            this.overrides.add(list.get(integer6));
        }
    }
    
    @Nullable
    public BakedModel resolve(final BakedModel elg, final ItemStack bly, @Nullable final ClientLevel dwl, @Nullable final LivingEntity aqj) {
        if (!this.overrides.isEmpty()) {
            int integer6 = 0;
            while (integer6 < this.overrides.size()) {
                final ItemOverride ebb7 = (ItemOverride)this.overrides.get(integer6);
                if (ebb7.test(bly, dwl, aqj)) {
                    final BakedModel elg2 = (BakedModel)this.overrideModels.get(integer6);
                    if (elg2 == null) {
                        return elg;
                    }
                    return elg2;
                }
                else {
                    ++integer6;
                }
            }
        }
        return elg;
    }
    
    static {
        EMPTY = new ItemOverrides();
    }
}
