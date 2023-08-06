package net.ccbluex.liquidbounce.ui.cnfont;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontLoaders {
    public static FontDrawer F14;
    public static FontDrawer F18;
    public static FontDrawer F16;
    public static FontDrawer F22;
    public static FontDrawer F24;
    public static FontDrawer F28;
    public static FontDrawer F32;
    public static FontDrawer F35;
    public static FontDrawer F38;
    public static FontDrawer F42;
    public static FontDrawer F45;
    public static FontDrawer F48;
    public static FontDrawer F52;
    public static FontDrawer F55;
    public static FontDrawer F58;
    public static FontDrawer F62;
    public static FontDrawer F64;


    public static void initFonts() {
        F18 = getFont("misans", 18, true);
        F16 = getFont("misans", 16, true);
        F14 = getFont("misans", 14, true);
        F22 = getFont("misans", 22, true);
        F24 = getFont("misans", 24, true);
        F28 = getFont("misans", 28, true);
        F32 = getFont("misans", 32, true);
        F35 = getFont("misans", 35, true);
        F38 = getFont("misans", 38, true);
        F42 = getFont("misans", 42, true);
        F45 = getFont("misans", 45, true);
        F48 = getFont("misans", 48, true);
        F52 = getFont("misans", 52, true);
        F55 = getFont("misans", 55, true);
        F58 = getFont("misans", 58, true);
        F62 = getFont("misans", 62, true);
        F64 = getFont("misans", 64, true);

    }

    public static FontDrawer getFont(String name, int size, boolean antiAliasing) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("pride/font/"+name+".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return new FontDrawer(font, antiAliasing);
    }
}
