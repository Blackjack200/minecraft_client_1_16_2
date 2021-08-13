package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Function;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class PlainVillagePools {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
    }
    
    static {
        START = Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/town_centers"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/town_centers/plains_fountain_01", ProcessorLists.MOSSIFY_20_PERCENT), (Object)50), Pair.of((Object)StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_1", ProcessorLists.MOSSIFY_20_PERCENT), (Object)50), Pair.of((Object)StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_2"), (Object)50), Pair.of((Object)StructurePoolElement.legacy("village/plains/town_centers/plains_meeting_point_3", ProcessorLists.MOSSIFY_70_PERCENT), (Object)50), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_fountain_01", ProcessorLists.ZOMBIE_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_1", ProcessorLists.ZOMBIE_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_2", ProcessorLists.ZOMBIE_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/town_centers/plains_meeting_point_3", ProcessorLists.ZOMBIE_PLAINS), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/streets"), new ResourceLocation("village/plains/terminators"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/corner_01", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/corner_02", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/corner_03", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_01", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_02", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_03", ProcessorLists.STREET_PLAINS), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_04", ProcessorLists.STREET_PLAINS), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_05", ProcessorLists.STREET_PLAINS), (Object)3), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/straight_06", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/crossroad_01", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/crossroad_02", ProcessorLists.STREET_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/streets/crossroad_03", ProcessorLists.STREET_PLAINS), (Object)2), (Object[])new Pair[] { Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_04", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_05", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/streets/crossroad_06", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/streets/turn_01", ProcessorLists.STREET_PLAINS), 3) }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/streets"), new ResourceLocation("village/plains/terminators"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/corner_01", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/corner_02", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/corner_03", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_01", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_02", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_03", ProcessorLists.STREET_PLAINS), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_04", ProcessorLists.STREET_PLAINS), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_05", ProcessorLists.STREET_PLAINS), (Object)3), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/straight_06", ProcessorLists.STREET_PLAINS), (Object)4), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_01", ProcessorLists.STREET_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_02", ProcessorLists.STREET_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_03", ProcessorLists.STREET_PLAINS), (Object)2), (Object[])new Pair[] { Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_04", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_05", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/crossroad_06", ProcessorLists.STREET_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/streets/turn_01", ProcessorLists.STREET_PLAINS), 3) }), StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/houses"), new ResourceLocation("village/plains/terminators"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_1", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_2", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_3", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_4", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_5", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_6", ProcessorLists.MOSSIFY_10_PERCENT), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_7", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_small_house_8", ProcessorLists.MOSSIFY_10_PERCENT), (Object)3), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_medium_house_1", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_medium_house_2", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_big_house_1", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", ProcessorLists.MOSSIFY_10_PERCENT), (Object)2), (Object[])new Pair[] { Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_2", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fletcher_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_shepherds_house_1"), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", ProcessorLists.MOSSIFY_10_PERCENT), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", ProcessorLists.MOSSIFY_10_PERCENT), 5), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", ProcessorLists.MOSSIFY_10_PERCENT), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_1", ProcessorLists.MOSSIFY_10_PERCENT), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2"), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", ProcessorLists.FARM_PLAINS), 4), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", ProcessorLists.FARM_PLAINS), 4), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1"), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2"), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_3"), 5), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_accessory_1"), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_4", ProcessorLists.MOSSIFY_70_PERCENT), 3), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_meeting_point_5"), 1), Pair.of(StructurePoolElement.empty(), 10) }), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/houses"), new ResourceLocation("village/plains/terminators"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_1", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_2", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_3", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_4", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_5", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_6", ProcessorLists.ZOMBIE_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_7", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_small_house_8", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_1", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_medium_house_2", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/houses/plains_big_house_1", ProcessorLists.ZOMBIE_PLAINS), (Object)2), Pair.of((Object)StructurePoolElement.legacy("village/plains/houses/plains_butcher_shop_1", ProcessorLists.ZOMBIE_PLAINS), (Object)2), (Object[])new Pair[] { Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_butcher_shop_2", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tool_smith_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_fletcher_house_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_shepherds_house_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_armorer_house_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_fisher_cottage_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_tannery_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_cartographer_1", ProcessorLists.ZOMBIE_PLAINS), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_1", ProcessorLists.ZOMBIE_PLAINS), 3), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_library_2", ProcessorLists.ZOMBIE_PLAINS), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_masons_house_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_weaponsmith_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_3", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_temple_4", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_stable_1", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_stable_2", ProcessorLists.ZOMBIE_PLAINS), 2), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_large_farm_1", ProcessorLists.ZOMBIE_PLAINS), 4), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_small_farm_1", ProcessorLists.ZOMBIE_PLAINS), 4), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_1", ProcessorLists.ZOMBIE_PLAINS), 1), Pair.of(StructurePoolElement.legacy("village/plains/houses/plains_animal_pen_2", ProcessorLists.ZOMBIE_PLAINS), 1), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_animal_pen_3", ProcessorLists.ZOMBIE_PLAINS), 5), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_4", ProcessorLists.ZOMBIE_PLAINS), 3), Pair.of(StructurePoolElement.legacy("village/plains/zombie/houses/plains_meeting_point_5", ProcessorLists.ZOMBIE_PLAINS), 1), Pair.of(StructurePoolElement.empty(), 10) }), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/terminators"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/terminators/terminator_01", ProcessorLists.STREET_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/terminators/terminator_02", ProcessorLists.STREET_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/terminators/terminator_03", ProcessorLists.STREET_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/terminators/terminator_04", ProcessorLists.STREET_PLAINS), (Object)1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/trees"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.feature(Features.OAK), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/decor"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/plains_lamp_1"), (Object)2), Pair.of((Object)StructurePoolElement.feature(Features.OAK), (Object)1), Pair.of((Object)StructurePoolElement.feature(Features.FLOWER_PLAIN), (Object)1), Pair.of((Object)StructurePoolElement.feature(Features.PILE_HAY), (Object)1), Pair.of((Object)StructurePoolElement.empty(), (Object)2)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/decor"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/plains_lamp_1", ProcessorLists.ZOMBIE_PLAINS), (Object)1), Pair.of((Object)StructurePoolElement.feature(Features.OAK), (Object)1), Pair.of((Object)StructurePoolElement.feature(Features.FLOWER_PLAIN), (Object)1), Pair.of((Object)StructurePoolElement.feature(Features.PILE_HAY), (Object)1), Pair.of((Object)StructurePoolElement.empty(), (Object)2)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/villagers"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/villagers/nitwit"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/villagers/baby"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/villagers/unemployed"), (Object)10)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/plains/zombie/villagers"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/villagers/nitwit"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/plains/zombie/villagers/unemployed"), (Object)10)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/animals"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cows_1"), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/pigs_1"), (Object)7), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/horses_1"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/horses_2"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/horses_3"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/horses_4"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/horses_5"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_1"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_2"), (Object)1), Pair.of((Object)StructurePoolElement.empty(), (Object)5)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/sheep"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_1"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_2"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/cats"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_black"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_british"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_calico"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_persian"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_ragdoll"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_red"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_siamese"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_tabby"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_white"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cat_jellie"), (Object)1), Pair.of((Object)StructurePoolElement.empty(), (Object)3)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/butcher_animals"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/animals/cows_1"), (Object)3), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/pigs_1"), (Object)3), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_1"), (Object)1), Pair.of((Object)StructurePoolElement.legacy("village/common/animals/sheep_2"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/iron_golem"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/iron_golem"), (Object)1)), StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation("village/common/well_bottoms"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.legacy("village/common/well_bottom"), (Object)1)), StructureTemplatePool.Projection.RIGID));
    }
}
