package net.minecraft.data.advancements;

import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.world.level.Level;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.Items;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.advancements.Advancement;
import java.util.function.Consumer;

public class StoryAdvancements implements Consumer<Consumer<Advancement>> {
    public void accept(final Consumer<Advancement> consumer) {
        final Advancement y3 = Advancement.Builder.advancement().display(Blocks.GRASS_BLOCK, new TranslatableComponent("advancements.story.root.title"), new TranslatableComponent("advancements.story.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"), FrameType.TASK, false, false, false).addCriterion("crafting_table", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE)).save(consumer, "story/root");
        final Advancement y4 = Advancement.Builder.advancement().parent(y3).display(Items.WOODEN_PICKAXE, new TranslatableComponent("advancements.story.mine_stone.title"), new TranslatableComponent("advancements.story.mine_stone.description"), null, FrameType.TASK, true, true, false).addCriterion("get_stone", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.STONE_TOOL_MATERIALS).build())).save(consumer, "story/mine_stone");
        final Advancement y5 = Advancement.Builder.advancement().parent(y4).display(Items.STONE_PICKAXE, new TranslatableComponent("advancements.story.upgrade_tools.title"), new TranslatableComponent("advancements.story.upgrade_tools.description"), null, FrameType.TASK, true, true, false).addCriterion("stone_pickaxe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.STONE_PICKAXE)).save(consumer, "story/upgrade_tools");
        final Advancement y6 = Advancement.Builder.advancement().parent(y5).display(Items.IRON_INGOT, new TranslatableComponent("advancements.story.smelt_iron.title"), new TranslatableComponent("advancements.story.smelt_iron.description"), null, FrameType.TASK, true, true, false).addCriterion("iron", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT)).save(consumer, "story/smelt_iron");
        final Advancement y7 = Advancement.Builder.advancement().parent(y6).display(Items.IRON_PICKAXE, new TranslatableComponent("advancements.story.iron_tools.title"), new TranslatableComponent("advancements.story.iron_tools.description"), null, FrameType.TASK, true, true, false).addCriterion("iron_pickaxe", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_PICKAXE)).save(consumer, "story/iron_tools");
        final Advancement y8 = Advancement.Builder.advancement().parent(y7).display(Items.DIAMOND, new TranslatableComponent("advancements.story.mine_diamond.title"), new TranslatableComponent("advancements.story.mine_diamond.description"), null, FrameType.TASK, true, true, false).addCriterion("diamond", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND)).save(consumer, "story/mine_diamond");
        final Advancement y9 = Advancement.Builder.advancement().parent(y6).display(Items.LAVA_BUCKET, new TranslatableComponent("advancements.story.lava_bucket.title"), new TranslatableComponent("advancements.story.lava_bucket.description"), null, FrameType.TASK, true, true, false).addCriterion("lava_bucket", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.LAVA_BUCKET)).save(consumer, "story/lava_bucket");
        final Advancement y10 = Advancement.Builder.advancement().parent(y6).display(Items.IRON_CHESTPLATE, new TranslatableComponent("advancements.story.obtain_armor.title"), new TranslatableComponent("advancements.story.obtain_armor.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("iron_helmet", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_HELMET)).addCriterion("iron_chestplate", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_CHESTPLATE)).addCriterion("iron_leggings", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_LEGGINGS)).addCriterion("iron_boots", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_BOOTS)).save(consumer, "story/obtain_armor");
        Advancement.Builder.advancement().parent(y8).display(Items.ENCHANTED_BOOK, new TranslatableComponent("advancements.story.enchant_item.title"), new TranslatableComponent("advancements.story.enchant_item.description"), null, FrameType.TASK, true, true, false).addCriterion("enchanted_item", (CriterionTriggerInstance)EnchantedItemTrigger.TriggerInstance.enchantedItem()).save(consumer, "story/enchant_item");
        final Advancement y11 = Advancement.Builder.advancement().parent(y9).display(Blocks.OBSIDIAN, new TranslatableComponent("advancements.story.form_obsidian.title"), new TranslatableComponent("advancements.story.form_obsidian.description"), null, FrameType.TASK, true, true, false).addCriterion("obsidian", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.OBSIDIAN)).save(consumer, "story/form_obsidian");
        Advancement.Builder.advancement().parent(y10).display(Items.SHIELD, new TranslatableComponent("advancements.story.deflect_arrow.title"), new TranslatableComponent("advancements.story.deflect_arrow.description"), null, FrameType.TASK, true, true, false).addCriterion("deflected_projectile", (CriterionTriggerInstance)EntityHurtPlayerTrigger.TriggerInstance.entityHurtPlayer(DamagePredicate.Builder.damageInstance().type(DamageSourcePredicate.Builder.damageType().isProjectile(true)).blocked(true))).save(consumer, "story/deflect_arrow");
        Advancement.Builder.advancement().parent(y8).display(Items.DIAMOND_CHESTPLATE, new TranslatableComponent("advancements.story.shiny_gear.title"), new TranslatableComponent("advancements.story.shiny_gear.description"), null, FrameType.TASK, true, true, false).requirements(RequirementsStrategy.OR).addCriterion("diamond_helmet", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_HELMET)).addCriterion("diamond_chestplate", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_CHESTPLATE)).addCriterion("diamond_leggings", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_LEGGINGS)).addCriterion("diamond_boots", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DIAMOND_BOOTS)).save(consumer, "story/shiny_gear");
        final Advancement y12 = Advancement.Builder.advancement().parent(y11).display(Items.FLINT_AND_STEEL, new TranslatableComponent("advancements.story.enter_the_nether.title"), new TranslatableComponent("advancements.story.enter_the_nether.description"), null, FrameType.TASK, true, true, false).addCriterion("entered_nether", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.NETHER)).save(consumer, "story/enter_the_nether");
        Advancement.Builder.advancement().parent(y12).display(Items.GOLDEN_APPLE, new TranslatableComponent("advancements.story.cure_zombie_villager.title"), new TranslatableComponent("advancements.story.cure_zombie_villager.description"), null, FrameType.GOAL, true, true, false).addCriterion("cured_zombie", (CriterionTriggerInstance)CuredZombieVillagerTrigger.TriggerInstance.curedZombieVillager()).save(consumer, "story/cure_zombie_villager");
        final Advancement y13 = Advancement.Builder.advancement().parent(y12).display(Items.ENDER_EYE, new TranslatableComponent("advancements.story.follow_ender_eye.title"), new TranslatableComponent("advancements.story.follow_ender_eye.description"), null, FrameType.TASK, true, true, false).addCriterion("in_stronghold", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.STRONGHOLD))).save(consumer, "story/follow_ender_eye");
        Advancement.Builder.advancement().parent(y13).display(Blocks.END_STONE, new TranslatableComponent("advancements.story.enter_the_end.title"), new TranslatableComponent("advancements.story.enter_the_end.description"), null, FrameType.TASK, true, true, false).addCriterion("entered_end", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(consumer, "story/enter_the_end");
    }
}
