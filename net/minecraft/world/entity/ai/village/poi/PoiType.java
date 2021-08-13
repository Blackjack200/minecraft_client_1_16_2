package net.minecraft.world.entity.ai.village.poi;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import com.google.common.base.Suppliers;
import java.util.stream.Collectors;
import net.minecraft.world.entity.npc.VillagerProfession;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.Util;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import java.util.Set;
import java.util.function.Supplier;

public class PoiType {
    private static final Supplier<Set<PoiType>> ALL_JOB_POI_TYPES;
    public static final Predicate<PoiType> ALL_JOBS;
    public static final Predicate<PoiType> ALL;
    private static final Set<BlockState> BEDS;
    private static final Map<BlockState, PoiType> TYPE_BY_STATE;
    public static final PoiType UNEMPLOYED;
    public static final PoiType ARMORER;
    public static final PoiType BUTCHER;
    public static final PoiType CARTOGRAPHER;
    public static final PoiType CLERIC;
    public static final PoiType FARMER;
    public static final PoiType FISHERMAN;
    public static final PoiType FLETCHER;
    public static final PoiType LEATHERWORKER;
    public static final PoiType LIBRARIAN;
    public static final PoiType MASON;
    public static final PoiType NITWIT;
    public static final PoiType SHEPHERD;
    public static final PoiType TOOLSMITH;
    public static final PoiType WEAPONSMITH;
    public static final PoiType HOME;
    public static final PoiType MEETING;
    public static final PoiType BEEHIVE;
    public static final PoiType BEE_NEST;
    public static final PoiType NETHER_PORTAL;
    public static final PoiType LODESTONE;
    protected static final Set<BlockState> ALL_STATES;
    private final String name;
    private final Set<BlockState> matchingStates;
    private final int maxTickets;
    private final Predicate<PoiType> predicate;
    private final int validRange;
    
    private static Set<BlockState> getBlockStates(final Block bul) {
        return (Set<BlockState>)ImmutableSet.copyOf((Collection)bul.getStateDefinition().getPossibleStates());
    }
    
    private PoiType(final String string, final Set<BlockState> set, final int integer3, final Predicate<PoiType> predicate, final int integer5) {
        this.name = string;
        this.matchingStates = (Set<BlockState>)ImmutableSet.copyOf((Collection)set);
        this.maxTickets = integer3;
        this.predicate = predicate;
        this.validRange = integer5;
    }
    
    private PoiType(final String string, final Set<BlockState> set, final int integer3, final int integer4) {
        this.name = string;
        this.matchingStates = (Set<BlockState>)ImmutableSet.copyOf((Collection)set);
        this.maxTickets = integer3;
        this.predicate = (Predicate<PoiType>)(azo -> azo == this);
        this.validRange = integer4;
    }
    
    public int getMaxTickets() {
        return this.maxTickets;
    }
    
    public Predicate<PoiType> getPredicate() {
        return this.predicate;
    }
    
    public int getValidRange() {
        return this.validRange;
    }
    
    public String toString() {
        return this.name;
    }
    
    private static PoiType register(final String string, final Set<BlockState> set, final int integer3, final int integer4) {
        return registerBlockStates(Registry.<PoiType, PoiType>register(Registry.POINT_OF_INTEREST_TYPE, new ResourceLocation(string), new PoiType(string, set, integer3, integer4)));
    }
    
    private static PoiType register(final String string, final Set<BlockState> set, final int integer3, final Predicate<PoiType> predicate, final int integer5) {
        return registerBlockStates(Registry.<PoiType, PoiType>register(Registry.POINT_OF_INTEREST_TYPE, new ResourceLocation(string), new PoiType(string, set, integer3, predicate, integer5)));
    }
    
    private static PoiType registerBlockStates(final PoiType azo) {
        azo.matchingStates.forEach(cee -> {
            final PoiType azo2 = (PoiType)PoiType.TYPE_BY_STATE.put(cee, azo);
            if (azo2 != null) {
                throw Util.<IllegalStateException>pauseInIde(new IllegalStateException(String.format("%s is defined in too many tags", new Object[] { cee })));
            }
        });
        return azo;
    }
    
    public static Optional<PoiType> forState(final BlockState cee) {
        return (Optional<PoiType>)Optional.ofNullable(PoiType.TYPE_BY_STATE.get(cee));
    }
    
    static {
        ALL_JOB_POI_TYPES = (Supplier)Suppliers.memoize(() -> (Set)Registry.VILLAGER_PROFESSION.stream().map(VillagerProfession::getJobPoiType).collect(Collectors.toSet()));
        ALL_JOBS = (azo -> ((Set)PoiType.ALL_JOB_POI_TYPES.get()).contains(azo));
        ALL = (azo -> true);
        BEDS = (Set)ImmutableList.of(Blocks.RED_BED, Blocks.BLACK_BED, Blocks.BLUE_BED, Blocks.BROWN_BED, Blocks.CYAN_BED, Blocks.GRAY_BED, Blocks.GREEN_BED, Blocks.LIGHT_BLUE_BED, Blocks.LIGHT_GRAY_BED, Blocks.LIME_BED, Blocks.MAGENTA_BED, Blocks.ORANGE_BED, (Object[])new Block[] { Blocks.PINK_BED, Blocks.PURPLE_BED, Blocks.WHITE_BED, Blocks.YELLOW_BED }).stream().flatMap(bul -> bul.getStateDefinition().getPossibleStates().stream()).filter(cee -> cee.<BedPart>getValue(BedBlock.PART) == BedPart.HEAD).collect(ImmutableSet.toImmutableSet());
        TYPE_BY_STATE = (Map)Maps.newHashMap();
        UNEMPLOYED = register("unemployed", (Set<BlockState>)ImmutableSet.of(), 1, PoiType.ALL_JOBS, 1);
        ARMORER = register("armorer", getBlockStates(Blocks.BLAST_FURNACE), 1, 1);
        BUTCHER = register("butcher", getBlockStates(Blocks.SMOKER), 1, 1);
        CARTOGRAPHER = register("cartographer", getBlockStates(Blocks.CARTOGRAPHY_TABLE), 1, 1);
        CLERIC = register("cleric", getBlockStates(Blocks.BREWING_STAND), 1, 1);
        FARMER = register("farmer", getBlockStates(Blocks.COMPOSTER), 1, 1);
        FISHERMAN = register("fisherman", getBlockStates(Blocks.BARREL), 1, 1);
        FLETCHER = register("fletcher", getBlockStates(Blocks.FLETCHING_TABLE), 1, 1);
        LEATHERWORKER = register("leatherworker", getBlockStates(Blocks.CAULDRON), 1, 1);
        LIBRARIAN = register("librarian", getBlockStates(Blocks.LECTERN), 1, 1);
        MASON = register("mason", getBlockStates(Blocks.STONECUTTER), 1, 1);
        NITWIT = register("nitwit", (Set<BlockState>)ImmutableSet.of(), 1, 1);
        SHEPHERD = register("shepherd", getBlockStates(Blocks.LOOM), 1, 1);
        TOOLSMITH = register("toolsmith", getBlockStates(Blocks.SMITHING_TABLE), 1, 1);
        WEAPONSMITH = register("weaponsmith", getBlockStates(Blocks.GRINDSTONE), 1, 1);
        HOME = register("home", PoiType.BEDS, 1, 1);
        MEETING = register("meeting", getBlockStates(Blocks.BELL), 32, 6);
        BEEHIVE = register("beehive", getBlockStates(Blocks.BEEHIVE), 0, 1);
        BEE_NEST = register("bee_nest", getBlockStates(Blocks.BEE_NEST), 0, 1);
        NETHER_PORTAL = register("nether_portal", getBlockStates(Blocks.NETHER_PORTAL), 0, 1);
        LODESTONE = register("lodestone", getBlockStates(Blocks.LODESTONE), 0, 1);
        ALL_STATES = (Set)new ObjectOpenHashSet((Collection)PoiType.TYPE_BY_STATE.keySet());
    }
}
