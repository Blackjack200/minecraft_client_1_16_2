package net.minecraft.world.level.saveddata.maps;

import net.minecraft.nbt.Tag;
import java.util.Objects;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;

public class MapBanner {
    private final BlockPos pos;
    private final DyeColor color;
    @Nullable
    private final Component name;
    
    public MapBanner(final BlockPos fx, final DyeColor bku, @Nullable final Component nr) {
        this.pos = fx;
        this.color = bku;
        this.name = nr;
    }
    
    public static MapBanner load(final CompoundTag md) {
        final BlockPos fx2 = NbtUtils.readBlockPos(md.getCompound("Pos"));
        final DyeColor bku3 = DyeColor.byName(md.getString("Color"), DyeColor.WHITE);
        final Component nr4 = md.contains("Name") ? Component.Serializer.fromJson(md.getString("Name")) : null;
        return new MapBanner(fx2, bku3, nr4);
    }
    
    @Nullable
    public static MapBanner fromWorld(final BlockGetter bqz, final BlockPos fx) {
        final BlockEntity ccg3 = bqz.getBlockEntity(fx);
        if (ccg3 instanceof BannerBlockEntity) {
            final BannerBlockEntity cbx4 = (BannerBlockEntity)ccg3;
            final DyeColor bku5 = cbx4.getBaseColor((Supplier<BlockState>)(() -> bqz.getBlockState(fx)));
            final Component nr6 = cbx4.hasCustomName() ? cbx4.getCustomName() : null;
            return new MapBanner(fx, bku5, nr6);
        }
        return null;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public MapDecoration.Type getDecoration() {
        switch (this.color) {
            case WHITE: {
                return MapDecoration.Type.BANNER_WHITE;
            }
            case ORANGE: {
                return MapDecoration.Type.BANNER_ORANGE;
            }
            case MAGENTA: {
                return MapDecoration.Type.BANNER_MAGENTA;
            }
            case LIGHT_BLUE: {
                return MapDecoration.Type.BANNER_LIGHT_BLUE;
            }
            case YELLOW: {
                return MapDecoration.Type.BANNER_YELLOW;
            }
            case LIME: {
                return MapDecoration.Type.BANNER_LIME;
            }
            case PINK: {
                return MapDecoration.Type.BANNER_PINK;
            }
            case GRAY: {
                return MapDecoration.Type.BANNER_GRAY;
            }
            case LIGHT_GRAY: {
                return MapDecoration.Type.BANNER_LIGHT_GRAY;
            }
            case CYAN: {
                return MapDecoration.Type.BANNER_CYAN;
            }
            case PURPLE: {
                return MapDecoration.Type.BANNER_PURPLE;
            }
            case BLUE: {
                return MapDecoration.Type.BANNER_BLUE;
            }
            case BROWN: {
                return MapDecoration.Type.BANNER_BROWN;
            }
            case GREEN: {
                return MapDecoration.Type.BANNER_GREEN;
            }
            case RED: {
                return MapDecoration.Type.BANNER_RED;
            }
            default: {
                return MapDecoration.Type.BANNER_BLACK;
            }
        }
    }
    
    @Nullable
    public Component getName() {
        return this.name;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final MapBanner cxq3 = (MapBanner)object;
        return Objects.equals(this.pos, cxq3.pos) && this.color == cxq3.color && Objects.equals(this.name, cxq3.name);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.pos, this.color, this.name });
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.put("Pos", (Tag)NbtUtils.writeBlockPos(this.pos));
        md2.putString("Color", this.color.getName());
        if (this.name != null) {
            md2.putString("Name", Component.Serializer.toJson(this.name));
        }
        return md2;
    }
    
    public String getId() {
        return new StringBuilder().append("banner-").append(this.pos.getX()).append(",").append(this.pos.getY()).append(",").append(this.pos.getZ()).toString();
    }
}
