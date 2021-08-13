package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import com.google.common.collect.Lists;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import java.util.List;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.Util;
import net.minecraft.SharedConstants;
import java.util.Iterator;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import com.google.common.collect.Maps;
import net.minecraft.world.item.Item;
import java.util.Map;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.WorldlyContainer;

public abstract class AbstractFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, TickableBlockEntity {
    private static final int[] SLOTS_FOR_UP;
    private static final int[] SLOTS_FOR_DOWN;
    private static final int[] SLOTS_FOR_SIDES;
    protected NonNullList<ItemStack> items;
    private int litTime;
    private int litDuration;
    private int cookingProgress;
    private int cookingTotalTime;
    protected final ContainerData dataAccess;
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed;
    protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
    
    protected AbstractFurnaceBlockEntity(final BlockEntityType<?> cch, final RecipeType<? extends AbstractCookingRecipe> boq) {
        super(cch);
        this.items = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(final int integer) {
                switch (integer) {
                    case 0: {
                        return AbstractFurnaceBlockEntity.this.litTime;
                    }
                    case 1: {
                        return AbstractFurnaceBlockEntity.this.litDuration;
                    }
                    case 2: {
                        return AbstractFurnaceBlockEntity.this.cookingProgress;
                    }
                    case 3: {
                        return AbstractFurnaceBlockEntity.this.cookingTotalTime;
                    }
                    default: {
                        return 0;
                    }
                }
            }
            
            public void set(final int integer1, final int integer2) {
                switch (integer1) {
                    case 0: {
                        AbstractFurnaceBlockEntity.this.litTime = integer2;
                        break;
                    }
                    case 1: {
                        AbstractFurnaceBlockEntity.this.litDuration = integer2;
                        break;
                    }
                    case 2: {
                        AbstractFurnaceBlockEntity.this.cookingProgress = integer2;
                        break;
                    }
                    case 3: {
                        AbstractFurnaceBlockEntity.this.cookingTotalTime = integer2;
                        break;
                    }
                }
            }
            
            public int getCount() {
                return 4;
            }
        };
        this.recipesUsed = (Object2IntOpenHashMap<ResourceLocation>)new Object2IntOpenHashMap();
        this.recipeType = boq;
    }
    
    public static Map<Item, Integer> getFuel() {
        final Map<Item, Integer> map1 = (Map<Item, Integer>)Maps.newLinkedHashMap();
        add(map1, Items.LAVA_BUCKET, 20000);
        add(map1, Blocks.COAL_BLOCK, 16000);
        add(map1, Items.BLAZE_ROD, 2400);
        add(map1, Items.COAL, 1600);
        add(map1, Items.CHARCOAL, 1600);
        add(map1, ItemTags.LOGS, 300);
        add(map1, ItemTags.PLANKS, 300);
        add(map1, ItemTags.WOODEN_STAIRS, 300);
        add(map1, ItemTags.WOODEN_SLABS, 150);
        add(map1, ItemTags.WOODEN_TRAPDOORS, 300);
        add(map1, ItemTags.WOODEN_PRESSURE_PLATES, 300);
        add(map1, Blocks.OAK_FENCE, 300);
        add(map1, Blocks.BIRCH_FENCE, 300);
        add(map1, Blocks.SPRUCE_FENCE, 300);
        add(map1, Blocks.JUNGLE_FENCE, 300);
        add(map1, Blocks.DARK_OAK_FENCE, 300);
        add(map1, Blocks.ACACIA_FENCE, 300);
        add(map1, Blocks.OAK_FENCE_GATE, 300);
        add(map1, Blocks.BIRCH_FENCE_GATE, 300);
        add(map1, Blocks.SPRUCE_FENCE_GATE, 300);
        add(map1, Blocks.JUNGLE_FENCE_GATE, 300);
        add(map1, Blocks.DARK_OAK_FENCE_GATE, 300);
        add(map1, Blocks.ACACIA_FENCE_GATE, 300);
        add(map1, Blocks.NOTE_BLOCK, 300);
        add(map1, Blocks.BOOKSHELF, 300);
        add(map1, Blocks.LECTERN, 300);
        add(map1, Blocks.JUKEBOX, 300);
        add(map1, Blocks.CHEST, 300);
        add(map1, Blocks.TRAPPED_CHEST, 300);
        add(map1, Blocks.CRAFTING_TABLE, 300);
        add(map1, Blocks.DAYLIGHT_DETECTOR, 300);
        add(map1, ItemTags.BANNERS, 300);
        add(map1, Items.BOW, 300);
        add(map1, Items.FISHING_ROD, 300);
        add(map1, Blocks.LADDER, 300);
        add(map1, ItemTags.SIGNS, 200);
        add(map1, Items.WOODEN_SHOVEL, 200);
        add(map1, Items.WOODEN_SWORD, 200);
        add(map1, Items.WOODEN_HOE, 200);
        add(map1, Items.WOODEN_AXE, 200);
        add(map1, Items.WOODEN_PICKAXE, 200);
        add(map1, ItemTags.WOODEN_DOORS, 200);
        add(map1, ItemTags.BOATS, 1200);
        add(map1, ItemTags.WOOL, 100);
        add(map1, ItemTags.WOODEN_BUTTONS, 100);
        add(map1, Items.STICK, 100);
        add(map1, ItemTags.SAPLINGS, 100);
        add(map1, Items.BOWL, 100);
        add(map1, ItemTags.CARPETS, 67);
        add(map1, Blocks.DRIED_KELP_BLOCK, 4001);
        add(map1, Items.CROSSBOW, 300);
        add(map1, Blocks.BAMBOO, 50);
        add(map1, Blocks.DEAD_BUSH, 100);
        add(map1, Blocks.SCAFFOLDING, 400);
        add(map1, Blocks.LOOM, 300);
        add(map1, Blocks.BARREL, 300);
        add(map1, Blocks.CARTOGRAPHY_TABLE, 300);
        add(map1, Blocks.FLETCHING_TABLE, 300);
        add(map1, Blocks.SMITHING_TABLE, 300);
        add(map1, Blocks.COMPOSTER, 300);
        return map1;
    }
    
    private static boolean isNeverAFurnaceFuel(final Item blu) {
        return ItemTags.NON_FLAMMABLE_WOOD.contains(blu);
    }
    
    private static void add(final Map<Item, Integer> map, final Tag<Item> aej, final int integer) {
        for (final Item blu5 : aej.getValues()) {
            if (!isNeverAFurnaceFuel(blu5)) {
                map.put(blu5, integer);
            }
        }
    }
    
    private static void add(final Map<Item, Integer> map, final ItemLike brt, final int integer) {
        final Item blu4 = brt.asItem();
        if (!isNeverAFurnaceFuel(blu4)) {
            map.put(blu4, integer);
            return;
        }
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException("A developer tried to explicitly make fire resistant item " + blu4.getName(null).getString() + " a furnace fuel. That will not work!"));
        }
    }
    
    private boolean isLit() {
        return this.litTime > 0;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        ContainerHelper.loadAllItems(md, this.items = NonNullList.<ItemStack>withSize(this.getContainerSize(), ItemStack.EMPTY));
        this.litTime = md.getShort("BurnTime");
        this.cookingProgress = md.getShort("CookTime");
        this.cookingTotalTime = md.getShort("CookTimeTotal");
        this.litDuration = this.getBurnDuration(this.items.get(1));
        final CompoundTag md2 = md.getCompound("RecipesUsed");
        for (final String string6 : md2.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(string6), md2.getInt(string6));
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putShort("BurnTime", (short)this.litTime);
        md.putShort("CookTime", (short)this.cookingProgress);
        md.putShort("CookTimeTotal", (short)this.cookingTotalTime);
        ContainerHelper.saveAllItems(md, this.items);
        final CompoundTag md2 = new CompoundTag();
        this.recipesUsed.forEach((vk, integer) -> md2.putInt(vk.toString(), integer));
        md.put("RecipesUsed", (net.minecraft.nbt.Tag)md2);
        return md;
    }
    
    @Override
    public void tick() {
        final boolean boolean2 = this.isLit();
        boolean boolean3 = false;
        if (this.isLit()) {
            --this.litTime;
        }
        if (!this.level.isClientSide) {
            final ItemStack bly4 = this.items.get(1);
            if (this.isLit() || (!bly4.isEmpty() && !this.items.get(0).isEmpty())) {
                final Recipe<?> bon5 = this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).orElse(null);
                if (!this.isLit() && this.canBurn(bon5)) {
                    this.litTime = this.getBurnDuration(bly4);
                    this.litDuration = this.litTime;
                    if (this.isLit()) {
                        boolean3 = true;
                        if (!bly4.isEmpty()) {
                            final Item blu6 = bly4.getItem();
                            bly4.shrink(1);
                            if (bly4.isEmpty()) {
                                final Item blu7 = blu6.getCraftingRemainingItem();
                                this.items.set(1, (blu7 == null) ? ItemStack.EMPTY : new ItemStack(blu7));
                            }
                        }
                    }
                }
                if (this.isLit() && this.canBurn(bon5)) {
                    ++this.cookingProgress;
                    if (this.cookingProgress == this.cookingTotalTime) {
                        this.cookingProgress = 0;
                        this.cookingTotalTime = this.getTotalCookTime();
                        this.burn(bon5);
                        boolean3 = true;
                    }
                }
                else {
                    this.cookingProgress = 0;
                }
            }
            else if (!this.isLit() && this.cookingProgress > 0) {
                this.cookingProgress = Mth.clamp(this.cookingProgress - 2, 0, this.cookingTotalTime);
            }
            if (boolean2 != this.isLit()) {
                boolean3 = true;
                this.level.setBlock(this.worldPosition, ((StateHolder<O, BlockState>)this.level.getBlockState(this.worldPosition)).<Comparable, Boolean>setValue((Property<Comparable>)AbstractFurnaceBlock.LIT, this.isLit()), 3);
            }
        }
        if (boolean3) {
            this.setChanged();
        }
    }
    
    protected boolean canBurn(@Nullable final Recipe<?> bon) {
        if (this.items.get(0).isEmpty() || bon == null) {
            return false;
        }
        final ItemStack bly3 = bon.getResultItem();
        if (bly3.isEmpty()) {
            return false;
        }
        final ItemStack bly4 = this.items.get(2);
        return bly4.isEmpty() || (bly4.sameItem(bly3) && ((bly4.getCount() < this.getMaxStackSize() && bly4.getCount() < bly4.getMaxStackSize()) || bly4.getCount() < bly3.getMaxStackSize()));
    }
    
    private void burn(@Nullable final Recipe<?> bon) {
        if (bon == null || !this.canBurn(bon)) {
            return;
        }
        final ItemStack bly3 = this.items.get(0);
        final ItemStack bly4 = bon.getResultItem();
        final ItemStack bly5 = this.items.get(2);
        if (bly5.isEmpty()) {
            this.items.set(2, bly4.copy());
        }
        else if (bly5.getItem() == bly4.getItem()) {
            bly5.grow(1);
        }
        if (!this.level.isClientSide) {
            this.setRecipeUsed(bon);
        }
        if (bly3.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
            this.items.set(1, new ItemStack(Items.WATER_BUCKET));
        }
        bly3.shrink(1);
    }
    
    protected int getBurnDuration(final ItemStack bly) {
        if (bly.isEmpty()) {
            return 0;
        }
        final Item blu3 = bly.getItem();
        return (int)getFuel().getOrDefault(blu3, 0);
    }
    
    protected int getTotalCookTime() {
        return (int)this.level.getRecipeManager().getRecipeFor(this.recipeType, this, this.level).map(AbstractCookingRecipe::getCookingTime).orElse(200);
    }
    
    public static boolean isFuel(final ItemStack bly) {
        return getFuel().containsKey(bly.getItem());
    }
    
    @Override
    public int[] getSlotsForFace(final Direction gc) {
        if (gc == Direction.DOWN) {
            return AbstractFurnaceBlockEntity.SLOTS_FOR_DOWN;
        }
        if (gc == Direction.UP) {
            return AbstractFurnaceBlockEntity.SLOTS_FOR_UP;
        }
        return AbstractFurnaceBlockEntity.SLOTS_FOR_SIDES;
    }
    
    @Override
    public boolean canPlaceItemThroughFace(final int integer, final ItemStack bly, @Nullable final Direction gc) {
        return this.canPlaceItem(integer, bly);
    }
    
    @Override
    public boolean canTakeItemThroughFace(final int integer, final ItemStack bly, final Direction gc) {
        if (gc == Direction.DOWN && integer == 1) {
            final Item blu5 = bly.getItem();
            if (blu5 != Items.WATER_BUCKET && blu5 != Items.BUCKET) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int getContainerSize() {
        return this.items.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (final ItemStack bly3 : this.items) {
            if (!bly3.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(final int integer) {
        return this.items.get(integer);
    }
    
    @Override
    public ItemStack removeItem(final int integer1, final int integer2) {
        return ContainerHelper.removeItem((List<ItemStack>)this.items, integer1, integer2);
    }
    
    @Override
    public ItemStack removeItemNoUpdate(final int integer) {
        return ContainerHelper.takeItem((List<ItemStack>)this.items, integer);
    }
    
    @Override
    public void setItem(final int integer, final ItemStack bly) {
        final ItemStack bly2 = this.items.get(integer);
        final boolean boolean5 = !bly.isEmpty() && bly.sameItem(bly2) && ItemStack.tagMatches(bly, bly2);
        this.items.set(integer, bly);
        if (bly.getCount() > this.getMaxStackSize()) {
            bly.setCount(this.getMaxStackSize());
        }
        if (integer == 0 && !boolean5) {
            this.cookingTotalTime = this.getTotalCookTime();
            this.cookingProgress = 0;
            this.setChanged();
        }
    }
    
    @Override
    public boolean stillValid(final Player bft) {
        return this.level.getBlockEntity(this.worldPosition) == this && bft.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public boolean canPlaceItem(final int integer, final ItemStack bly) {
        if (integer == 2) {
            return false;
        }
        if (integer == 1) {
            final ItemStack bly2 = this.items.get(1);
            return isFuel(bly) || (bly.getItem() == Items.BUCKET && bly2.getItem() != Items.BUCKET);
        }
        return true;
    }
    
    public void clearContent() {
        this.items.clear();
    }
    
    @Override
    public void setRecipeUsed(@Nullable final Recipe<?> bon) {
        if (bon != null) {
            final ResourceLocation vk3 = bon.getId();
            this.recipesUsed.addTo(vk3, 1);
        }
    }
    
    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }
    
    @Override
    public void awardUsedRecipes(final Player bft) {
    }
    
    public void awardUsedRecipesAndPopExperience(final Player bft) {
        final List<Recipe<?>> list3 = this.getRecipesToAwardAndPopExperience(bft.level, bft.position());
        bft.awardRecipes((Collection<Recipe<?>>)list3);
        this.recipesUsed.clear();
    }
    
    public List<Recipe<?>> getRecipesToAwardAndPopExperience(final Level bru, final Vec3 dck) {
        final List<Recipe<?>> list4 = (List<Recipe<?>>)Lists.newArrayList();
        for (final Object2IntMap.Entry<ResourceLocation> entry6 : this.recipesUsed.object2IntEntrySet()) {
            bru.getRecipeManager().byKey((ResourceLocation)entry6.getKey()).ifPresent(bon -> {
                list4.add(bon);
                createExperience(bru, dck, entry6.getIntValue(), ((AbstractCookingRecipe)bon).getExperience());
            });
        }
        return list4;
    }
    
    private static void createExperience(final Level bru, final Vec3 dck, final int integer, final float float4) {
        int integer2 = Mth.floor(integer * float4);
        final float float5 = Mth.frac(integer * float4);
        if (float5 != 0.0f && Math.random() < float5) {
            ++integer2;
        }
        while (integer2 > 0) {
            final int integer3 = ExperienceOrb.getExperienceValue(integer2);
            integer2 -= integer3;
            bru.addFreshEntity(new ExperienceOrb(bru, dck.x, dck.y, dck.z, integer3));
        }
    }
    
    @Override
    public void fillStackedContents(final StackedContents bfv) {
        for (final ItemStack bly4 : this.items) {
            bfv.accountStack(bly4);
        }
    }
    
    static {
        SLOTS_FOR_UP = new int[] { 0 };
        SLOTS_FOR_DOWN = new int[] { 2, 1 };
        SLOTS_FOR_SIDES = new int[] { 1 };
    }
}
