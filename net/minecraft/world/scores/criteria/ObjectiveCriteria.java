package net.minecraft.world.scores.criteria;

import com.google.common.collect.ImmutableMap;
import net.minecraft.ChatFormatting;
import com.google.common.collect.Maps;
import net.minecraft.stats.StatType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Optional;
import java.util.Map;

public class ObjectiveCriteria {
    public static final Map<String, ObjectiveCriteria> CRITERIA_BY_NAME;
    public static final ObjectiveCriteria DUMMY;
    public static final ObjectiveCriteria TRIGGER;
    public static final ObjectiveCriteria DEATH_COUNT;
    public static final ObjectiveCriteria KILL_COUNT_PLAYERS;
    public static final ObjectiveCriteria KILL_COUNT_ALL;
    public static final ObjectiveCriteria HEALTH;
    public static final ObjectiveCriteria FOOD;
    public static final ObjectiveCriteria AIR;
    public static final ObjectiveCriteria ARMOR;
    public static final ObjectiveCriteria EXPERIENCE;
    public static final ObjectiveCriteria LEVEL;
    public static final ObjectiveCriteria[] TEAM_KILL;
    public static final ObjectiveCriteria[] KILLED_BY_TEAM;
    private final String name;
    private final boolean readOnly;
    private final RenderType renderType;
    
    public ObjectiveCriteria(final String string) {
        this(string, false, RenderType.INTEGER);
    }
    
    protected ObjectiveCriteria(final String string, final boolean boolean2, final RenderType a) {
        this.name = string;
        this.readOnly = boolean2;
        this.renderType = a;
        ObjectiveCriteria.CRITERIA_BY_NAME.put(string, this);
    }
    
    public static Optional<ObjectiveCriteria> byName(final String string) {
        if (ObjectiveCriteria.CRITERIA_BY_NAME.containsKey(string)) {
            return (Optional<ObjectiveCriteria>)Optional.of(ObjectiveCriteria.CRITERIA_BY_NAME.get(string));
        }
        final int integer2 = string.indexOf(58);
        if (integer2 < 0) {
            return (Optional<ObjectiveCriteria>)Optional.empty();
        }
        return (Optional<ObjectiveCriteria>)Registry.STAT_TYPE.getOptional(ResourceLocation.of(string.substring(0, integer2), '.')).flatMap(adx -> ObjectiveCriteria.getStat((StatType<Object>)adx, ResourceLocation.of(string.substring(integer2 + 1), '.')));
    }
    
    private static <T> Optional<ObjectiveCriteria> getStat(final StatType<T> adx, final ResourceLocation vk) {
        return (Optional<ObjectiveCriteria>)adx.getRegistry().getOptional(vk).map(adx::get);
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public RenderType getDefaultRenderType() {
        return this.renderType;
    }
    
    static {
        CRITERIA_BY_NAME = (Map)Maps.newHashMap();
        DUMMY = new ObjectiveCriteria("dummy");
        TRIGGER = new ObjectiveCriteria("trigger");
        DEATH_COUNT = new ObjectiveCriteria("deathCount");
        KILL_COUNT_PLAYERS = new ObjectiveCriteria("playerKillCount");
        KILL_COUNT_ALL = new ObjectiveCriteria("totalKillCount");
        HEALTH = new ObjectiveCriteria("health", true, RenderType.HEARTS);
        FOOD = new ObjectiveCriteria("food", true, RenderType.INTEGER);
        AIR = new ObjectiveCriteria("air", true, RenderType.INTEGER);
        ARMOR = new ObjectiveCriteria("armor", true, RenderType.INTEGER);
        EXPERIENCE = new ObjectiveCriteria("xp", true, RenderType.INTEGER);
        LEVEL = new ObjectiveCriteria("level", true, RenderType.INTEGER);
        TEAM_KILL = new ObjectiveCriteria[] { new ObjectiveCriteria("teamkill." + ChatFormatting.BLACK.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_BLUE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_GREEN.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_AQUA.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_RED.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_PURPLE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GOLD.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GRAY.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.DARK_GRAY.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.BLUE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.GREEN.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.AQUA.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.RED.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.LIGHT_PURPLE.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.YELLOW.getName()), new ObjectiveCriteria("teamkill." + ChatFormatting.WHITE.getName()) };
        KILLED_BY_TEAM = new ObjectiveCriteria[] { new ObjectiveCriteria("killedByTeam." + ChatFormatting.BLACK.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_BLUE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_GREEN.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_AQUA.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_RED.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_PURPLE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GOLD.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GRAY.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.DARK_GRAY.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.BLUE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.GREEN.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.AQUA.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.RED.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.LIGHT_PURPLE.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.YELLOW.getName()), new ObjectiveCriteria("killedByTeam." + ChatFormatting.WHITE.getName()) };
    }
    
    public enum RenderType {
        INTEGER("integer"), 
        HEARTS("hearts");
        
        private final String id;
        private static final Map<String, RenderType> BY_ID;
        
        private RenderType(final String string3) {
            this.id = string3;
        }
        
        public String getId() {
            return this.id;
        }
        
        public static RenderType byId(final String string) {
            return (RenderType)RenderType.BY_ID.getOrDefault(string, RenderType.INTEGER);
        }
        
        static {
            final ImmutableMap.Builder<String, RenderType> builder1 = (ImmutableMap.Builder<String, RenderType>)ImmutableMap.builder();
            for (final RenderType a5 : values()) {
                builder1.put(a5.id, a5);
            }
            BY_ID = (Map)builder1.build();
        }
    }
}
