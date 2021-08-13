package net.minecraft.network.syncher;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Pose;
import java.util.OptionalInt;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;

public class EntityDataSerializers {
    private static final CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>> SERIALIZERS;
    public static final EntityDataSerializer<Byte> BYTE;
    public static final EntityDataSerializer<Integer> INT;
    public static final EntityDataSerializer<Float> FLOAT;
    public static final EntityDataSerializer<String> STRING;
    public static final EntityDataSerializer<Component> COMPONENT;
    public static final EntityDataSerializer<Optional<Component>> OPTIONAL_COMPONENT;
    public static final EntityDataSerializer<ItemStack> ITEM_STACK;
    public static final EntityDataSerializer<Optional<BlockState>> BLOCK_STATE;
    public static final EntityDataSerializer<Boolean> BOOLEAN;
    public static final EntityDataSerializer<ParticleOptions> PARTICLE;
    public static final EntityDataSerializer<Rotations> ROTATIONS;
    public static final EntityDataSerializer<BlockPos> BLOCK_POS;
    public static final EntityDataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS;
    public static final EntityDataSerializer<Direction> DIRECTION;
    public static final EntityDataSerializer<Optional<UUID>> OPTIONAL_UUID;
    public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG;
    public static final EntityDataSerializer<VillagerData> VILLAGER_DATA;
    public static final EntityDataSerializer<OptionalInt> OPTIONAL_UNSIGNED_INT;
    public static final EntityDataSerializer<Pose> POSE;
    
    public static void registerSerializer(final EntityDataSerializer<?> ut) {
        EntityDataSerializers.SERIALIZERS.add(ut);
    }
    
    @Nullable
    public static EntityDataSerializer<?> getSerializer(final int integer) {
        return EntityDataSerializers.SERIALIZERS.byId(integer);
    }
    
    public static int getSerializedId(final EntityDataSerializer<?> ut) {
        return EntityDataSerializers.SERIALIZERS.getId(ut);
    }
    
    static {
        SERIALIZERS = new CrudeIncrementalIntIdentityHashBiMap<EntityDataSerializer<?>>(16);
        BYTE = new EntityDataSerializer<Byte>() {
            public void write(final FriendlyByteBuf nf, final Byte byte2) {
                nf.writeByte(byte2);
            }
            
            public Byte read(final FriendlyByteBuf nf) {
                return nf.readByte();
            }
            
            public Byte copy(final Byte byte1) {
                return byte1;
            }
        };
        INT = new EntityDataSerializer<Integer>() {
            public void write(final FriendlyByteBuf nf, final Integer integer) {
                nf.writeVarInt(integer);
            }
            
            public Integer read(final FriendlyByteBuf nf) {
                return nf.readVarInt();
            }
            
            public Integer copy(final Integer integer) {
                return integer;
            }
        };
        FLOAT = new EntityDataSerializer<Float>() {
            public void write(final FriendlyByteBuf nf, final Float float2) {
                nf.writeFloat(float2);
            }
            
            public Float read(final FriendlyByteBuf nf) {
                return nf.readFloat();
            }
            
            public Float copy(final Float float1) {
                return float1;
            }
        };
        STRING = new EntityDataSerializer<String>() {
            public void write(final FriendlyByteBuf nf, final String string) {
                nf.writeUtf(string);
            }
            
            public String read(final FriendlyByteBuf nf) {
                return nf.readUtf(32767);
            }
            
            public String copy(final String string) {
                return string;
            }
        };
        COMPONENT = new EntityDataSerializer<Component>() {
            public void write(final FriendlyByteBuf nf, final Component nr) {
                nf.writeComponent(nr);
            }
            
            public Component read(final FriendlyByteBuf nf) {
                return nf.readComponent();
            }
            
            public Component copy(final Component nr) {
                return nr;
            }
        };
        OPTIONAL_COMPONENT = new EntityDataSerializer<Optional<Component>>() {
            public void write(final FriendlyByteBuf nf, final Optional<Component> optional) {
                if (optional.isPresent()) {
                    nf.writeBoolean(true);
                    nf.writeComponent((Component)optional.get());
                }
                else {
                    nf.writeBoolean(false);
                }
            }
            
            public Optional<Component> read(final FriendlyByteBuf nf) {
                return (Optional<Component>)(nf.readBoolean() ? Optional.of(nf.readComponent()) : Optional.empty());
            }
            
            public Optional<Component> copy(final Optional<Component> optional) {
                return optional;
            }
        };
        ITEM_STACK = new EntityDataSerializer<ItemStack>() {
            public void write(final FriendlyByteBuf nf, final ItemStack bly) {
                nf.writeItem(bly);
            }
            
            public ItemStack read(final FriendlyByteBuf nf) {
                return nf.readItem();
            }
            
            public ItemStack copy(final ItemStack bly) {
                return bly.copy();
            }
        };
        BLOCK_STATE = new EntityDataSerializer<Optional<BlockState>>() {
            public void write(final FriendlyByteBuf nf, final Optional<BlockState> optional) {
                if (optional.isPresent()) {
                    nf.writeVarInt(Block.getId((BlockState)optional.get()));
                }
                else {
                    nf.writeVarInt(0);
                }
            }
            
            public Optional<BlockState> read(final FriendlyByteBuf nf) {
                final int integer3 = nf.readVarInt();
                if (integer3 == 0) {
                    return (Optional<BlockState>)Optional.empty();
                }
                return (Optional<BlockState>)Optional.of(Block.stateById(integer3));
            }
            
            public Optional<BlockState> copy(final Optional<BlockState> optional) {
                return optional;
            }
        };
        BOOLEAN = new EntityDataSerializer<Boolean>() {
            public void write(final FriendlyByteBuf nf, final Boolean boolean2) {
                nf.writeBoolean(boolean2);
            }
            
            public Boolean read(final FriendlyByteBuf nf) {
                return nf.readBoolean();
            }
            
            public Boolean copy(final Boolean boolean1) {
                return boolean1;
            }
        };
        PARTICLE = new EntityDataSerializer<ParticleOptions>() {
            public void write(final FriendlyByteBuf nf, final ParticleOptions hf) {
                nf.writeVarInt(Registry.PARTICLE_TYPE.getId(hf.getType()));
                hf.writeToNetwork(nf);
            }
            
            public ParticleOptions read(final FriendlyByteBuf nf) {
                return this.<ParticleOptions>readParticle(nf, Registry.PARTICLE_TYPE.byId(nf.readVarInt()));
            }
            
            private <T extends ParticleOptions> T readParticle(final FriendlyByteBuf nf, final ParticleType<T> hg) {
                return hg.getDeserializer().fromNetwork(hg, nf);
            }
            
            public ParticleOptions copy(final ParticleOptions hf) {
                return hf;
            }
        };
        ROTATIONS = new EntityDataSerializer<Rotations>() {
            public void write(final FriendlyByteBuf nf, final Rotations go) {
                nf.writeFloat(go.getX());
                nf.writeFloat(go.getY());
                nf.writeFloat(go.getZ());
            }
            
            public Rotations read(final FriendlyByteBuf nf) {
                return new Rotations(nf.readFloat(), nf.readFloat(), nf.readFloat());
            }
            
            public Rotations copy(final Rotations go) {
                return go;
            }
        };
        BLOCK_POS = new EntityDataSerializer<BlockPos>() {
            public void write(final FriendlyByteBuf nf, final BlockPos fx) {
                nf.writeBlockPos(fx);
            }
            
            public BlockPos read(final FriendlyByteBuf nf) {
                return nf.readBlockPos();
            }
            
            public BlockPos copy(final BlockPos fx) {
                return fx;
            }
        };
        OPTIONAL_BLOCK_POS = new EntityDataSerializer<Optional<BlockPos>>() {
            public void write(final FriendlyByteBuf nf, final Optional<BlockPos> optional) {
                nf.writeBoolean(optional.isPresent());
                if (optional.isPresent()) {
                    nf.writeBlockPos((BlockPos)optional.get());
                }
            }
            
            public Optional<BlockPos> read(final FriendlyByteBuf nf) {
                if (!nf.readBoolean()) {
                    return (Optional<BlockPos>)Optional.empty();
                }
                return (Optional<BlockPos>)Optional.of(nf.readBlockPos());
            }
            
            public Optional<BlockPos> copy(final Optional<BlockPos> optional) {
                return optional;
            }
        };
        DIRECTION = new EntityDataSerializer<Direction>() {
            public void write(final FriendlyByteBuf nf, final Direction gc) {
                nf.writeEnum(gc);
            }
            
            public Direction read(final FriendlyByteBuf nf) {
                return nf.<Direction>readEnum(Direction.class);
            }
            
            public Direction copy(final Direction gc) {
                return gc;
            }
        };
        OPTIONAL_UUID = new EntityDataSerializer<Optional<UUID>>() {
            public void write(final FriendlyByteBuf nf, final Optional<UUID> optional) {
                nf.writeBoolean(optional.isPresent());
                if (optional.isPresent()) {
                    nf.writeUUID((UUID)optional.get());
                }
            }
            
            public Optional<UUID> read(final FriendlyByteBuf nf) {
                if (!nf.readBoolean()) {
                    return (Optional<UUID>)Optional.empty();
                }
                return (Optional<UUID>)Optional.of(nf.readUUID());
            }
            
            public Optional<UUID> copy(final Optional<UUID> optional) {
                return optional;
            }
        };
        COMPOUND_TAG = new EntityDataSerializer<CompoundTag>() {
            public void write(final FriendlyByteBuf nf, final CompoundTag md) {
                nf.writeNbt(md);
            }
            
            public CompoundTag read(final FriendlyByteBuf nf) {
                return nf.readNbt();
            }
            
            public CompoundTag copy(final CompoundTag md) {
                return md.copy();
            }
        };
        VILLAGER_DATA = new EntityDataSerializer<VillagerData>() {
            public void write(final FriendlyByteBuf nf, final VillagerData bfh) {
                nf.writeVarInt(Registry.VILLAGER_TYPE.getId(bfh.getType()));
                nf.writeVarInt(Registry.VILLAGER_PROFESSION.getId(bfh.getProfession()));
                nf.writeVarInt(bfh.getLevel());
            }
            
            public VillagerData read(final FriendlyByteBuf nf) {
                return new VillagerData(Registry.VILLAGER_TYPE.byId(nf.readVarInt()), Registry.VILLAGER_PROFESSION.byId(nf.readVarInt()), nf.readVarInt());
            }
            
            public VillagerData copy(final VillagerData bfh) {
                return bfh;
            }
        };
        OPTIONAL_UNSIGNED_INT = new EntityDataSerializer<OptionalInt>() {
            public void write(final FriendlyByteBuf nf, final OptionalInt optionalInt) {
                nf.writeVarInt(optionalInt.orElse(-1) + 1);
            }
            
            public OptionalInt read(final FriendlyByteBuf nf) {
                final int integer3 = nf.readVarInt();
                return (integer3 == 0) ? OptionalInt.empty() : OptionalInt.of(integer3 - 1);
            }
            
            public OptionalInt copy(final OptionalInt optionalInt) {
                return optionalInt;
            }
        };
        POSE = new EntityDataSerializer<Pose>() {
            public void write(final FriendlyByteBuf nf, final Pose aqu) {
                nf.writeEnum(aqu);
            }
            
            public Pose read(final FriendlyByteBuf nf) {
                return nf.<Pose>readEnum(Pose.class);
            }
            
            public Pose copy(final Pose aqu) {
                return aqu;
            }
        };
        registerSerializer(EntityDataSerializers.BYTE);
        registerSerializer(EntityDataSerializers.INT);
        registerSerializer(EntityDataSerializers.FLOAT);
        registerSerializer(EntityDataSerializers.STRING);
        registerSerializer(EntityDataSerializers.COMPONENT);
        registerSerializer(EntityDataSerializers.OPTIONAL_COMPONENT);
        registerSerializer(EntityDataSerializers.ITEM_STACK);
        registerSerializer(EntityDataSerializers.BOOLEAN);
        registerSerializer(EntityDataSerializers.ROTATIONS);
        registerSerializer(EntityDataSerializers.BLOCK_POS);
        registerSerializer(EntityDataSerializers.OPTIONAL_BLOCK_POS);
        registerSerializer(EntityDataSerializers.DIRECTION);
        registerSerializer(EntityDataSerializers.OPTIONAL_UUID);
        registerSerializer(EntityDataSerializers.BLOCK_STATE);
        registerSerializer(EntityDataSerializers.COMPOUND_TAG);
        registerSerializer(EntityDataSerializers.PARTICLE);
        registerSerializer(EntityDataSerializers.VILLAGER_DATA);
        registerSerializer(EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
        registerSerializer(EntityDataSerializers.POSE);
    }
}
