package net.minecraft.world.entity.ai.sensing;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.function.Supplier;

public class SensorType<U extends Sensor<?>> {
    public static final SensorType<DummySensor> DUMMY;
    public static final SensorType<NearestItemSensor> NEAREST_ITEMS;
    public static final SensorType<NearestLivingEntitySensor> NEAREST_LIVING_ENTITIES;
    public static final SensorType<PlayerSensor> NEAREST_PLAYERS;
    public static final SensorType<NearestBedSensor> NEAREST_BED;
    public static final SensorType<HurtBySensor> HURT_BY;
    public static final SensorType<VillagerHostilesSensor> VILLAGER_HOSTILES;
    public static final SensorType<VillagerBabiesSensor> VILLAGER_BABIES;
    public static final SensorType<SecondaryPoiSensor> SECONDARY_POIS;
    public static final SensorType<GolemSensor> GOLEM_DETECTED;
    public static final SensorType<PiglinSpecificSensor> PIGLIN_SPECIFIC_SENSOR;
    public static final SensorType<PiglinBruteSpecificSensor> PIGLIN_BRUTE_SPECIFIC_SENSOR;
    public static final SensorType<HoglinSpecificSensor> HOGLIN_SPECIFIC_SENSOR;
    public static final SensorType<AdultSensor> NEAREST_ADULT;
    private final Supplier<U> factory;
    
    private SensorType(final Supplier<U> supplier) {
        this.factory = supplier;
    }
    
    public U create() {
        return (U)this.factory.get();
    }
    
    private static <U extends Sensor<?>> SensorType<U> register(final String string, final Supplier<U> supplier) {
        return Registry.<SensorType<?>, SensorType<U>>register(Registry.SENSOR_TYPE, new ResourceLocation(string), new SensorType<U>(supplier));
    }
    
    static {
        DUMMY = SensorType.<DummySensor>register("dummy", (java.util.function.Supplier<DummySensor>)DummySensor::new);
        NEAREST_ITEMS = SensorType.<NearestItemSensor>register("nearest_items", (java.util.function.Supplier<NearestItemSensor>)NearestItemSensor::new);
        NEAREST_LIVING_ENTITIES = SensorType.<NearestLivingEntitySensor>register("nearest_living_entities", (java.util.function.Supplier<NearestLivingEntitySensor>)NearestLivingEntitySensor::new);
        NEAREST_PLAYERS = SensorType.<PlayerSensor>register("nearest_players", (java.util.function.Supplier<PlayerSensor>)PlayerSensor::new);
        NEAREST_BED = SensorType.<NearestBedSensor>register("nearest_bed", (java.util.function.Supplier<NearestBedSensor>)NearestBedSensor::new);
        HURT_BY = SensorType.<HurtBySensor>register("hurt_by", (java.util.function.Supplier<HurtBySensor>)HurtBySensor::new);
        VILLAGER_HOSTILES = SensorType.<VillagerHostilesSensor>register("villager_hostiles", (java.util.function.Supplier<VillagerHostilesSensor>)VillagerHostilesSensor::new);
        VILLAGER_BABIES = SensorType.<VillagerBabiesSensor>register("villager_babies", (java.util.function.Supplier<VillagerBabiesSensor>)VillagerBabiesSensor::new);
        SECONDARY_POIS = SensorType.<SecondaryPoiSensor>register("secondary_pois", (java.util.function.Supplier<SecondaryPoiSensor>)SecondaryPoiSensor::new);
        GOLEM_DETECTED = SensorType.<GolemSensor>register("golem_detected", (java.util.function.Supplier<GolemSensor>)GolemSensor::new);
        PIGLIN_SPECIFIC_SENSOR = SensorType.<PiglinSpecificSensor>register("piglin_specific_sensor", (java.util.function.Supplier<PiglinSpecificSensor>)PiglinSpecificSensor::new);
        PIGLIN_BRUTE_SPECIFIC_SENSOR = SensorType.<PiglinBruteSpecificSensor>register("piglin_brute_specific_sensor", (java.util.function.Supplier<PiglinBruteSpecificSensor>)PiglinBruteSpecificSensor::new);
        HOGLIN_SPECIFIC_SENSOR = SensorType.<HoglinSpecificSensor>register("hoglin_specific_sensor", (java.util.function.Supplier<HoglinSpecificSensor>)HoglinSpecificSensor::new);
        NEAREST_ADULT = SensorType.<AdultSensor>register("nearest_adult", (java.util.function.Supplier<AdultSensor>)AdultSensor::new);
    }
}
