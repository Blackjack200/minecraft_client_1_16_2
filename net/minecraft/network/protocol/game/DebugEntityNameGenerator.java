package net.minecraft.network.protocol.game;

import net.minecraft.Util;
import java.util.Random;
import java.util.UUID;

public class DebugEntityNameGenerator {
    private static final String[] NAMES_FIRST_PART;
    private static final String[] NAMES_SECOND_PART;
    
    public static String getEntityName(final UUID uUID) {
        final Random random2 = getRandom(uUID);
        return getRandomString(random2, DebugEntityNameGenerator.NAMES_FIRST_PART) + getRandomString(random2, DebugEntityNameGenerator.NAMES_SECOND_PART);
    }
    
    private static String getRandomString(final Random random, final String[] arr) {
        return Util.<String>getRandom(arr, random);
    }
    
    private static Random getRandom(final UUID uUID) {
        return new Random((long)(uUID.hashCode() >> 2));
    }
    
    static {
        NAMES_FIRST_PART = new String[] { "Slim", "Far", "River", "Silly", "Fat", "Thin", "Fish", "Bat", "Dark", "Oak", "Sly", "Bush", "Zen", "Bark", "Cry", "Slack", "Soup", "Grim", "Hook", "Dirt", "Mud", "Sad", "Hard", "Crook", "Sneak", "Stink", "Weird", "Fire", "Soot", "Soft", "Rough", "Cling", "Scar" };
        NAMES_SECOND_PART = new String[] { "Fox", "Tail", "Jaw", "Whisper", "Twig", "Root", "Finder", "Nose", "Brow", "Blade", "Fry", "Seek", "Wart", "Tooth", "Foot", "Leaf", "Stone", "Fall", "Face", "Tongue", "Voice", "Lip", "Mouth", "Snail", "Toe", "Ear", "Hair", "Beard", "Shirt", "Fist" };
    }
}
