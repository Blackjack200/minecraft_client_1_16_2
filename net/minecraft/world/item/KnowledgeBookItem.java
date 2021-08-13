package net.minecraft.world.item;

import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import net.minecraft.world.item.crafting.RecipeManager;
import java.util.List;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Lists;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.Logger;

public class KnowledgeBookItem extends Item {
    private static final Logger LOGGER;
    
    public KnowledgeBookItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final ItemStack bly5 = bft.getItemInHand(aoq);
        final CompoundTag md6 = bly5.getTag();
        if (!bft.abilities.instabuild) {
            bft.setItemInHand(aoq, ItemStack.EMPTY);
        }
        if (md6 == null || !md6.contains("Recipes", 9)) {
            KnowledgeBookItem.LOGGER.error("Tag not valid: {}", md6);
            return InteractionResultHolder.<ItemStack>fail(bly5);
        }
        if (!bru.isClientSide) {
            final ListTag mj7 = md6.getList("Recipes", 8);
            final List<Recipe<?>> list8 = (List<Recipe<?>>)Lists.newArrayList();
            final RecipeManager boo9 = bru.getServer().getRecipeManager();
            for (int integer10 = 0; integer10 < mj7.size(); ++integer10) {
                final String string11 = mj7.getString(integer10);
                final Optional<? extends Recipe<?>> optional12 = boo9.byKey(new ResourceLocation(string11));
                if (!optional12.isPresent()) {
                    KnowledgeBookItem.LOGGER.error("Invalid recipe: {}", string11);
                    return InteractionResultHolder.<ItemStack>fail(bly5);
                }
                list8.add(optional12.get());
            }
            bft.awardRecipes((Collection<Recipe<?>>)list8);
            bft.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.<ItemStack>sidedSuccess(bly5, bru.isClientSide());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
