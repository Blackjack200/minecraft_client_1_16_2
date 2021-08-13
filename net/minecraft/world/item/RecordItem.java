package net.minecraft.world.item;

import com.google.common.collect.Maps;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.sounds.SoundEvent;
import java.util.Map;

public class RecordItem extends Item {
    private static final Map<SoundEvent, RecordItem> BY_NAME;
    private final int analogOutput;
    private final SoundEvent sound;
    
    protected RecordItem(final int integer, final SoundEvent adn, final Properties a) {
        super(a);
        this.analogOutput = integer;
        this.sound = adn;
        RecordItem.BY_NAME.put(this.sound, this);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (!cee5.is(Blocks.JUKEBOX) || cee5.<Boolean>getValue((Property<Boolean>)JukeboxBlock.HAS_RECORD)) {
            return InteractionResult.PASS;
        }
        final ItemStack bly6 = bnx.getItemInHand();
        if (!bru3.isClientSide) {
            ((JukeboxBlock)Blocks.JUKEBOX).setRecord(bru3, fx4, cee5, bly6);
            bru3.levelEvent(null, 1010, fx4, Item.getId(this));
            bly6.shrink(1);
            final Player bft7 = bnx.getPlayer();
            if (bft7 != null) {
                bft7.awardStat(Stats.PLAY_RECORD);
            }
        }
        return InteractionResult.sidedSuccess(bru3.isClientSide);
    }
    
    public int getAnalogOutput() {
        return this.analogOutput;
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
    }
    
    public MutableComponent getDisplayName() {
        return new TranslatableComponent(this.getDescriptionId() + ".desc");
    }
    
    @Nullable
    public static RecordItem getBySound(final SoundEvent adn) {
        return (RecordItem)RecordItem.BY_NAME.get(adn);
    }
    
    public SoundEvent getSound() {
        return this.sound;
    }
    
    static {
        BY_NAME = (Map)Maps.newHashMap();
    }
}
