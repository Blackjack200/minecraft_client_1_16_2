package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataFixTypes;
import com.mojang.datafixers.DataFixer;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SerializableUUID;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.StringUtil;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.UUID;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import org.apache.logging.log4j.Logger;

public final class NbtUtils {
    private static final Logger LOGGER;
    
    @Nullable
    public static GameProfile readGameProfile(final CompoundTag md) {
        String string2 = null;
        UUID uUID3 = null;
        if (md.contains("Name", 8)) {
            string2 = md.getString("Name");
        }
        if (md.hasUUID("Id")) {
            uUID3 = md.getUUID("Id");
        }
        try {
            final GameProfile gameProfile4 = new GameProfile(uUID3, string2);
            if (md.contains("Properties", 10)) {
                final CompoundTag md2 = md.getCompound("Properties");
                for (final String string3 : md2.getAllKeys()) {
                    final ListTag mj8 = md2.getList(string3, 10);
                    for (int integer9 = 0; integer9 < mj8.size(); ++integer9) {
                        final CompoundTag md3 = mj8.getCompound(integer9);
                        final String string4 = md3.getString("Value");
                        if (md3.contains("Signature", 8)) {
                            gameProfile4.getProperties().put(string3, new Property(string3, string4, md3.getString("Signature")));
                        }
                        else {
                            gameProfile4.getProperties().put(string3, new Property(string3, string4));
                        }
                    }
                }
            }
            return gameProfile4;
        }
        catch (Throwable t) {
            return null;
        }
    }
    
    public static CompoundTag writeGameProfile(final CompoundTag md, final GameProfile gameProfile) {
        if (!StringUtil.isNullOrEmpty(gameProfile.getName())) {
            md.putString("Name", gameProfile.getName());
        }
        if (gameProfile.getId() != null) {
            md.putUUID("Id", gameProfile.getId());
        }
        if (!gameProfile.getProperties().isEmpty()) {
            final CompoundTag md2 = new CompoundTag();
            for (final String string5 : gameProfile.getProperties().keySet()) {
                final ListTag mj6 = new ListTag();
                for (final Property property8 : gameProfile.getProperties().get(string5)) {
                    final CompoundTag md3 = new CompoundTag();
                    md3.putString("Value", property8.getValue());
                    if (property8.hasSignature()) {
                        md3.putString("Signature", property8.getSignature());
                    }
                    mj6.add(md3);
                }
                md2.put(string5, mj6);
            }
            md.put("Properties", (Tag)md2);
        }
        return md;
    }
    
    @VisibleForTesting
    public static boolean compareNbt(@Nullable final Tag mt1, @Nullable final Tag mt2, final boolean boolean3) {
        if (mt1 == mt2) {
            return true;
        }
        if (mt1 == null) {
            return true;
        }
        if (mt2 == null) {
            return false;
        }
        if (!mt1.getClass().equals(mt2.getClass())) {
            return false;
        }
        if (mt1 instanceof CompoundTag) {
            final CompoundTag md4 = (CompoundTag)mt1;
            final CompoundTag md5 = (CompoundTag)mt2;
            for (final String string7 : md4.getAllKeys()) {
                final Tag mt3 = md4.get(string7);
                if (!compareNbt(mt3, md5.get(string7), boolean3)) {
                    return false;
                }
            }
            return true;
        }
        if (!(mt1 instanceof ListTag) || !boolean3) {
            return mt1.equals(mt2);
        }
        final ListTag mj4 = (ListTag)mt1;
        final ListTag mj5 = (ListTag)mt2;
        if (mj4.isEmpty()) {
            return mj5.isEmpty();
        }
        for (int integer6 = 0; integer6 < mj4.size(); ++integer6) {
            final Tag mt4 = mj4.get(integer6);
            boolean boolean4 = false;
            for (int integer7 = 0; integer7 < mj5.size(); ++integer7) {
                if (compareNbt(mt4, mj5.get(integer7), boolean3)) {
                    boolean4 = true;
                    break;
                }
            }
            if (!boolean4) {
                return false;
            }
        }
        return true;
    }
    
    public static IntArrayTag createUUID(final UUID uUID) {
        return new IntArrayTag(SerializableUUID.uuidToIntArray(uUID));
    }
    
    public static UUID loadUUID(final Tag mt) {
        if (mt.getType() != IntArrayTag.TYPE) {
            throw new IllegalArgumentException("Expected UUID-Tag to be of type " + IntArrayTag.TYPE.getName() + ", but found " + mt.getType().getName() + ".");
        }
        final int[] arr2 = ((IntArrayTag)mt).getAsIntArray();
        if (arr2.length != 4) {
            throw new IllegalArgumentException(new StringBuilder().append("Expected UUID-Array to be of length 4, but found ").append(arr2.length).append(".").toString());
        }
        return SerializableUUID.uuidFromIntArray(arr2);
    }
    
    public static BlockPos readBlockPos(final CompoundTag md) {
        return new BlockPos(md.getInt("X"), md.getInt("Y"), md.getInt("Z"));
    }
    
    public static CompoundTag writeBlockPos(final BlockPos fx) {
        final CompoundTag md2 = new CompoundTag();
        md2.putInt("X", fx.getX());
        md2.putInt("Y", fx.getY());
        md2.putInt("Z", fx.getZ());
        return md2;
    }
    
    public static BlockState readBlockState(final CompoundTag md) {
        if (!md.contains("Name", 8)) {
            return Blocks.AIR.defaultBlockState();
        }
        final Block bul2 = Registry.BLOCK.get(new ResourceLocation(md.getString("Name")));
        BlockState cee3 = bul2.defaultBlockState();
        if (md.contains("Properties", 10)) {
            final CompoundTag md2 = md.getCompound("Properties");
            final StateDefinition<Block, BlockState> cef5 = bul2.getStateDefinition();
            for (final String string7 : md2.getAllKeys()) {
                final net.minecraft.world.level.block.state.properties.Property<?> cfg8 = cef5.getProperty(string7);
                if (cfg8 != null) {
                    cee3 = NbtUtils.setValueHelper(cee3, cfg8, string7, md2, md);
                }
            }
        }
        return cee3;
    }
    
    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(final S ceg, final net.minecraft.world.level.block.state.properties.Property<T> cfg, final String string, final CompoundTag md4, final CompoundTag md5) {
        final Optional<T> optional6 = cfg.getValue(md4.getString(string));
        if (optional6.isPresent()) {
            return ((StateHolder<O, S>)ceg).<T, Comparable>setValue(cfg, optional6.get());
        }
        NbtUtils.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", string, md4.getString(string), md5.toString());
        return ceg;
    }
    
    public static CompoundTag writeBlockState(final BlockState cee) {
        final CompoundTag md2 = new CompoundTag();
        md2.putString("Name", Registry.BLOCK.getKey(cee.getBlock()).toString());
        final ImmutableMap<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> immutableMap3 = cee.getValues();
        if (!immutableMap3.isEmpty()) {
            final CompoundTag md3 = new CompoundTag();
            for (final Map.Entry<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> entry6 : immutableMap3.entrySet()) {
                final net.minecraft.world.level.block.state.properties.Property<?> cfg7 = entry6.getKey();
                md3.putString(cfg7.getName(), NbtUtils.getName(cfg7, entry6.getValue()));
            }
            md2.put("Properties", (Tag)md3);
        }
        return md2;
    }
    
    private static <T extends Comparable<T>> String getName(final net.minecraft.world.level.block.state.properties.Property<T> cfg, final Comparable<?> comparable) {
        return cfg.getName((T)comparable);
    }
    
    public static CompoundTag update(final DataFixer dataFixer, final DataFixTypes afx, final CompoundTag md, final int integer) {
        return update(dataFixer, afx, md, integer, SharedConstants.getCurrentVersion().getWorldVersion());
    }
    
    public static CompoundTag update(final DataFixer dataFixer, final DataFixTypes afx, final CompoundTag md, final int integer4, final int integer5) {
        return (CompoundTag)dataFixer.update(afx.getType(), new Dynamic((DynamicOps)NbtOps.INSTANCE, md), integer4, integer5).getValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
