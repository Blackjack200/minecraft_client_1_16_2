package net.minecraft.world.level.storage.loot.parameters;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

public class LootContextParams {
    public static final LootContextParam<Entity> THIS_ENTITY;
    public static final LootContextParam<Player> LAST_DAMAGE_PLAYER;
    public static final LootContextParam<DamageSource> DAMAGE_SOURCE;
    public static final LootContextParam<Entity> KILLER_ENTITY;
    public static final LootContextParam<Entity> DIRECT_KILLER_ENTITY;
    public static final LootContextParam<Vec3> ORIGIN;
    public static final LootContextParam<BlockState> BLOCK_STATE;
    public static final LootContextParam<BlockEntity> BLOCK_ENTITY;
    public static final LootContextParam<ItemStack> TOOL;
    public static final LootContextParam<Float> EXPLOSION_RADIUS;
    
    private static <T> LootContextParam<T> create(final String string) {
        return new LootContextParam<T>(new ResourceLocation(string));
    }
    
    static {
        THIS_ENTITY = LootContextParams.<Entity>create("this_entity");
        LAST_DAMAGE_PLAYER = LootContextParams.<Player>create("last_damage_player");
        DAMAGE_SOURCE = LootContextParams.<DamageSource>create("damage_source");
        KILLER_ENTITY = LootContextParams.<Entity>create("killer_entity");
        DIRECT_KILLER_ENTITY = LootContextParams.<Entity>create("direct_killer_entity");
        ORIGIN = LootContextParams.<Vec3>create("origin");
        BLOCK_STATE = LootContextParams.<BlockState>create("block_state");
        BLOCK_ENTITY = LootContextParams.<BlockEntity>create("block_entity");
        TOOL = LootContextParams.<ItemStack>create("tool");
        EXPLOSION_RADIUS = LootContextParams.<Float>create("explosion_radius");
    }
}
