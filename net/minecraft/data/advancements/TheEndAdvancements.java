package net.minecraft.data.advancements;

import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.world.item.Items;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.world.entity.EntityType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ItemLike;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.advancements.Advancement;
import java.util.function.Consumer;

public class TheEndAdvancements implements Consumer<Consumer<Advancement>> {
    public void accept(final Consumer<Advancement> consumer) {
        final Advancement y3 = Advancement.Builder.advancement().display(Blocks.END_STONE, new TranslatableComponent("advancements.end.root.title"), new TranslatableComponent("advancements.end.root.description"), new ResourceLocation("textures/gui/advancements/backgrounds/end.png"), FrameType.TASK, false, false, false).addCriterion("entered_end", (CriterionTriggerInstance)ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END)).save(consumer, "end/root");
        final Advancement y4 = Advancement.Builder.advancement().parent(y3).display(Blocks.DRAGON_HEAD, new TranslatableComponent("advancements.end.kill_dragon.title"), new TranslatableComponent("advancements.end.kill_dragon.description"), null, FrameType.TASK, true, true, false).addCriterion("killed_dragon", (CriterionTriggerInstance)KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.ENDER_DRAGON))).save(consumer, "end/kill_dragon");
        final Advancement y5 = Advancement.Builder.advancement().parent(y4).display(Items.ENDER_PEARL, new TranslatableComponent("advancements.end.enter_end_gateway.title"), new TranslatableComponent("advancements.end.enter_end_gateway.description"), null, FrameType.TASK, true, true, false).addCriterion("entered_end_gateway", (CriterionTriggerInstance)EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.END_GATEWAY)).save(consumer, "end/enter_end_gateway");
        Advancement.Builder.advancement().parent(y4).display(Items.END_CRYSTAL, new TranslatableComponent("advancements.end.respawn_dragon.title"), new TranslatableComponent("advancements.end.respawn_dragon.description"), null, FrameType.GOAL, true, true, false).addCriterion("summoned_dragon", (CriterionTriggerInstance)SummonedEntityTrigger.TriggerInstance.summonedEntity(EntityPredicate.Builder.entity().of(EntityType.ENDER_DRAGON))).save(consumer, "end/respawn_dragon");
        final Advancement y6 = Advancement.Builder.advancement().parent(y5).display(Blocks.PURPUR_BLOCK, new TranslatableComponent("advancements.end.find_end_city.title"), new TranslatableComponent("advancements.end.find_end_city.description"), null, FrameType.TASK, true, true, false).addCriterion("in_city", (CriterionTriggerInstance)LocationTrigger.TriggerInstance.located(LocationPredicate.inFeature(StructureFeature.END_CITY))).save(consumer, "end/find_end_city");
        Advancement.Builder.advancement().parent(y4).display(Items.DRAGON_BREATH, new TranslatableComponent("advancements.end.dragon_breath.title"), new TranslatableComponent("advancements.end.dragon_breath.description"), null, FrameType.GOAL, true, true, false).addCriterion("dragon_breath", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.DRAGON_BREATH)).save(consumer, "end/dragon_breath");
        Advancement.Builder.advancement().parent(y6).display(Items.SHULKER_SHELL, new TranslatableComponent("advancements.end.levitate.title"), new TranslatableComponent("advancements.end.levitate.description"), null, FrameType.CHALLENGE, true, true, false).rewards(AdvancementRewards.Builder.experience(50)).addCriterion("levitated", (CriterionTriggerInstance)LevitationTrigger.TriggerInstance.levitated(DistancePredicate.vertical(MinMaxBounds.Floats.atLeast(50.0f)))).save(consumer, "end/levitate");
        Advancement.Builder.advancement().parent(y6).display(Items.ELYTRA, new TranslatableComponent("advancements.end.elytra.title"), new TranslatableComponent("advancements.end.elytra.description"), null, FrameType.GOAL, true, true, false).addCriterion("elytra", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Items.ELYTRA)).save(consumer, "end/elytra");
        Advancement.Builder.advancement().parent(y4).display(Blocks.DRAGON_EGG, new TranslatableComponent("advancements.end.dragon_egg.title"), new TranslatableComponent("advancements.end.dragon_egg.description"), null, FrameType.GOAL, true, true, false).addCriterion("dragon_egg", (CriterionTriggerInstance)InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.DRAGON_EGG)).save(consumer, "end/dragon_egg");
    }
}
