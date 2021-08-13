package net.minecraft.data.advancements;

import net.minecraft.world.entity.animal.Cat;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.Registry;
import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.world.item.Items;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EntityType;
import net.minecraft.advancements.Advancement;
import java.util.function.Consumer;

public class HusbandryAdvancements implements Consumer<Consumer<Advancement>> {
    private static final EntityType<?>[] BREEDABLE_ANIMALS;
    private static final Item[] FISH;
    private static final Item[] FISH_BUCKETS;
    private static final Item[] EDIBLE_ITEMS;
    
    public void accept(final Consumer<Advancement> consumer) {
        final Advancement y3 = Advancement.Builder.advancement().display(Blocks.HAY_BLOCK, new TranslatableComponent("advancements.husbandry.root.title"), new TranslatableComponent("advancements.husbandry.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/husbandry.png"), FrameType.TASK, false, false, false).addCriterion("consumed_item", (CriterionTriggerInstance)ConsumeItemTrigger.TriggerInstance.usedItem()).save(consumer, "husbandry/root");
        final Advancement y4 = Advancement.Builder.advancement().parent(y3).display(Items.WHEAT, new TranslatableComponent("advancements.husbandry.plant_seed.title"), new TranslatableComponent("advancements.husbandry.plant_seed.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("wheat", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.WHEAT)).addCriterion("pumpkin_stem", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.PUMPKIN_STEM)).addCriterion("melon_stem", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.MELON_STEM)).addCriterion("beetroots", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.BEETROOTS)).addCriterion("nether_wart", (CriterionTriggerInstance)PlacedBlockTrigger.TriggerInstance.placedBlock(Blocks.NETHER_WART)).save(consumer, "husbandry/plant_seed");
        final Advancement y5 = Advancement.Builder.advancement().parent(y3).display(Items.WHEAT, new TranslatableComponent("advancements.husbandry.breed_an_animal.title"), new TranslatableComponent("advancements.husbandry.breed_an_animal.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("bred", (CriterionTriggerInstance)BredAnimalsTrigger.TriggerInstance.bredAnimals()).save(consumer, "husbandry/breed_an_animal");
        this.addFood(Advancement.Builder.advancement()).parent(y4).display(Items.APPLE, new TranslatableComponent("advancements.husbandry.balanced_diet.title"), new TranslatableComponent("advancements.husbandry.balanced_diet.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).save(consumer, "husbandry/balanced_diet");
        Advancement.Builder.advancement().parent(y4).display(Items.NETHERITE_HOE, new TranslatableComponent("advancements.husbandry.netherite_hoe.title"), new TranslatableComponent("advancements.husbandry.netherite_hoe.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).addCriterion("netherite_hoe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_HOE)).save(consumer, "husbandry/obtain_netherite_hoe");
        final Advancement y6 = Advancement.Builder.advancement().parent(y3).display(Items.LEAD, new TranslatableComponent("advancements.husbandry.tame_an_animal.title"), new TranslatableComponent("advancements.husbandry.tame_an_animal.description"), null, FrameType.TASK, true, true, false).addCriterion("tamed_animal", (CriterionTriggerInstance)TameAnimalTrigger.TriggerInstance.tamedAnimal()).save(consumer, "husbandry/tame_an_animal");
        this.addBreedable(Advancement.Builder.advancement()).parent(y5).display(Items.GOLDEN_CARROT, new TranslatableComponent("advancements.husbandry.breed_all_animals.title"), new TranslatableComponent("advancements.husbandry.breed_all_animals.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(100)).save(consumer, "husbandry/bred_all_animals");
        final Advancement y7 = this.addFish(Advancement.Builder.advancement()).parent(y3).requirements(RequirementsStrategy.OR).display(Items.FISHING_ROD, new TranslatableComponent("advancements.husbandry.fishy_business.title"), new TranslatableComponent("advancements.husbandry.fishy_business.description"), null, FrameType.TASK, true, true, false).save(consumer, "husbandry/fishy_business");
        this.addFishBuckets(Advancement.Builder.advancement()).parent(y7).requirements(RequirementsStrategy.OR).display(Items.PUFFERFISH_BUCKET, new TranslatableComponent("advancements.husbandry.tactical_fishing.title"), new TranslatableComponent("advancements.husbandry.tactical_fishing.description"), null, FrameType.TASK, true, true, false).save(consumer, "husbandry/tactical_fishing");
        this.addCatVariants(Advancement.Builder.advancement()).parent(y6).display(Items.COD, new TranslatableComponent("advancements.husbandry.complete_catalogue.title"), new TranslatableComponent("advancements.husbandry.complete_catalogue.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).save(consumer, "husbandry/complete_catalogue");
        Advancement.Builder.advancement().parent(y3).addCriterion("safely_harvest_honey", (CriterionTriggerInstance)ItemUsedOnBlockTrigger.TriggerInstance.itemUsedOnBlock(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(BlockTags.BEEHIVES).build()).setSmokey(true), ItemPredicate.Builder.item().of(Items.GLASS_BOTTLE))).display(Items.HONEY_BOTTLE, new TranslatableComponent("advancements.husbandry.safely_harvest_honey.title"), new TranslatableComponent("advancements.husbandry.safely_harvest_honey.description"), null, FrameType.TASK, true, true, false).save(consumer, "husbandry/safely_harvest_honey");
        Advancement.Builder.advancement().parent(y3).addCriterion("silk_touch_nest", (CriterionTriggerInstance)BeeNestDestroyedTrigger.TriggerInstance.destroyedBeeNest(Blocks.BEE_NEST, ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))), MinMaxBounds.Ints.exactly(3))).display(Blocks.BEE_NEST, new TranslatableComponent("advancements.husbandry.silk_touch_nest.title"), new TranslatableComponent("advancements.husbandry.silk_touch_nest.description"), null, FrameType.TASK, true, true, false).save(consumer, "husbandry/silk_touch_nest");
    }
    
    private Advancement.Builder addFood(final Advancement.Builder a) {
        for (final Item blu6 : HusbandryAdvancements.EDIBLE_ITEMS) {
            a.addCriterion(Registry.ITEM.getKey(blu6).getPath(), ConsumeItemTrigger.TriggerInstance.usedItem(blu6));
        }
        return a;
    }
    
    private Advancement.Builder addBreedable(final Advancement.Builder a) {
        for (final EntityType<?> aqb6 : HusbandryAdvancements.BREEDABLE_ANIMALS) {
            a.addCriterion(EntityType.getKey(aqb6).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(aqb6)));
        }
        a.addCriterion(EntityType.getKey(EntityType.TURTLE).toString(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(EntityType.TURTLE).build(), EntityPredicate.Builder.entity().of(EntityType.TURTLE).build(), EntityPredicate.ANY));
        return a;
    }
    
    private Advancement.Builder addFishBuckets(final Advancement.Builder a) {
        for (final Item blu6 : HusbandryAdvancements.FISH_BUCKETS) {
            a.addCriterion(Registry.ITEM.getKey(blu6).getPath(), FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.Builder.item().of(blu6).build()));
        }
        return a;
    }
    
    private Advancement.Builder addFish(final Advancement.Builder a) {
        for (final Item blu6 : HusbandryAdvancements.FISH) {
            a.addCriterion(Registry.ITEM.getKey(blu6).getPath(), FishingRodHookedTrigger.TriggerInstance.fishedItem(ItemPredicate.ANY, EntityPredicate.ANY, ItemPredicate.Builder.item().of(blu6).build()));
        }
        return a;
    }
    
    private Advancement.Builder addCatVariants(final Advancement.Builder a) {
        Cat.TEXTURE_BY_TYPE.forEach((integer, vk) -> a.addCriterion(vk.getPath(), TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(vk).build())));
        return a;
    }
    
    static {
        BREEDABLE_ANIMALS = new EntityType[] { EntityType.HORSE, EntityType.DONKEY, EntityType.MULE, EntityType.SHEEP, EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.CHICKEN, EntityType.WOLF, EntityType.OCELOT, EntityType.RABBIT, EntityType.LLAMA, EntityType.CAT, EntityType.PANDA, EntityType.FOX, EntityType.BEE, EntityType.HOGLIN, EntityType.STRIDER };
        FISH = new Item[] { Items.COD, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.SALMON };
        FISH_BUCKETS = new Item[] { Items.COD_BUCKET, Items.TROPICAL_FISH_BUCKET, Items.PUFFERFISH_BUCKET, Items.SALMON_BUCKET };
        EDIBLE_ITEMS = new Item[] { Items.APPLE, Items.MUSHROOM_STEW, Items.BREAD, Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE, Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH, Items.COOKED_COD, Items.COOKED_SALMON, Items.COOKIE, Items.MELON_SLICE, Items.BEEF, Items.COOKED_BEEF, Items.CHICKEN, Items.COOKED_CHICKEN, Items.ROTTEN_FLESH, Items.SPIDER_EYE, Items.CARROT, Items.POTATO, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.GOLDEN_CARROT, Items.PUMPKIN_PIE, Items.RABBIT, Items.COOKED_RABBIT, Items.RABBIT_STEW, Items.MUTTON, Items.COOKED_MUTTON, Items.CHORUS_FRUIT, Items.BEETROOT, Items.BEETROOT_SOUP, Items.DRIED_KELP, Items.SUSPICIOUS_STEW, Items.SWEET_BERRIES, Items.HONEY_BOTTLE };
    }
}
