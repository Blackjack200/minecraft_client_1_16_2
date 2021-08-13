package net.minecraft.core;

import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import java.util.function.Supplier;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import java.util.EnumMap;
import org.apache.logging.log4j.Logger;

public class BlockMath {
    private static final Logger LOGGER;
    public static final EnumMap<Direction, Transformation> vanillaUvTransformLocalToGlobal;
    public static final EnumMap<Direction, Transformation> vanillaUvTransformGlobalToLocal;
    
    public static Transformation blockCenterToCorner(final Transformation f) {
        final Matrix4f b2 = Matrix4f.createTranslateMatrix(0.5f, 0.5f, 0.5f);
        b2.multiply(f.getMatrix());
        b2.multiply(Matrix4f.createTranslateMatrix(-0.5f, -0.5f, -0.5f));
        return new Transformation(b2);
    }
    
    public static Transformation getUVLockTransform(final Transformation f, final Direction gc, final Supplier<String> supplier) {
        final Direction gc2 = Direction.rotate(f.getMatrix(), gc);
        final Transformation f2 = f.inverse();
        if (f2 == null) {
            BlockMath.LOGGER.warn((String)supplier.get());
            return new Transformation(null, null, new Vector3f(0.0f, 0.0f, 0.0f), null);
        }
        final Transformation f3 = ((Transformation)BlockMath.vanillaUvTransformGlobalToLocal.get(gc)).compose(f2).compose((Transformation)BlockMath.vanillaUvTransformLocalToGlobal.get(gc2));
        return blockCenterToCorner(f3);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        vanillaUvTransformLocalToGlobal = Util.<EnumMap>make(Maps.newEnumMap((Class)Direction.class), (java.util.function.Consumer<EnumMap>)(enumMap -> {
            enumMap.put((Enum)Direction.SOUTH, Transformation.identity());
            enumMap.put((Enum)Direction.EAST, new Transformation(null, new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), 90.0f, true), null, null));
            enumMap.put((Enum)Direction.WEST, new Transformation(null, new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), -90.0f, true), null, null));
            enumMap.put((Enum)Direction.NORTH, new Transformation(null, new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), 180.0f, true), null, null));
            enumMap.put((Enum)Direction.UP, new Transformation(null, new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), -90.0f, true), null, null));
            enumMap.put((Enum)Direction.DOWN, new Transformation(null, new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), 90.0f, true), null, null));
        }));
        vanillaUvTransformGlobalToLocal = Util.<EnumMap>make(Maps.newEnumMap((Class)Direction.class), (java.util.function.Consumer<EnumMap>)(enumMap -> {
            for (final Direction gc5 : Direction.values()) {
                enumMap.put((Enum)gc5, ((Transformation)BlockMath.vanillaUvTransformLocalToGlobal.get((Object)gc5)).inverse());
            }
        }));
    }
}
