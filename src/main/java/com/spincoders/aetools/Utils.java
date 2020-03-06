package com.spincoders.aetools;

import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.text.Normalizer;

public class Utils {
    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    private static final int NORMAL = 0;
    private static final int BRIGHT = 1;

    public static final int COLOR_BLACK = 30;
    public static final int COLOR_RED = 31;
    public static final int COLOR_GREEN = 32;
    public static final int COLOR_YELLOW = 33;
    public static final int COLOR_BLUE = 34;
    public static final int COLOR_MAGENTA = 35;
    public static final int COLOR_CYAN = 36;
    public static final int COLOR_WHITE = 37;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m";
    private static final char SEPARATOR = ';';
    private static final String END_COLOUR = PREFIX + SUFFIX;

    public static String colored(String text) {
        return colored(text, COLOR_GREEN, true);
    }

    public static String colored(String text, int color, boolean bright)
    {
        String prefix=PREFIX + (bright?BRIGHT:NORMAL) + SEPARATOR + color + SUFFIX;
        return String.format("%s%s%s", prefix,text,END_COLOUR);
    }

    public static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static File getWorldSaveLocation(World world)
    {
        File dir = DimensionManager.getCurrentSaveRootDirectory();

        if (world != null)
        {
            if (world.provider.getSaveFolder() != null)
            {
                return new File(dir, world.provider.getSaveFolder());
            }
            else if (world.provider.getDimension() == 0)
            {
                return dir;
            }
        }

        return null;
    }

    public static File getRegionDirectory(World world)
    {
        File worldDir = getWorldSaveLocation(world);

        if (worldDir != null)
        {
            return new File(worldDir, "region");
        }

        return null;
    }

}
