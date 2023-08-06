package net.ccbluex.liquidbounce.utils;

import java.util.*;

import com.sun.org.apache.xerces.internal.xs.StringList;
import net.minecraft.util.ResourceLocation;

public class RandomImgUtils {
    private static long startTime = 0L;
    static Random random = new Random();
    static int count = random.nextInt(1);
    public static int count2 = random.nextInt(1);

    public static List<String> list = Arrays.asList(
            "pride/bg/bg.png",
            "pride/bg/bg2.jpg",
            "pride/bg/bg3.jpg",
            "pride/bg/bg4.jpg");

    public static ResourceLocation getBackGround() {
        int index = new Random().nextInt(list.size());
        return new ResourceLocation(list.get(index));
    }
    public static String getBackGroundPath() {
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }
}
