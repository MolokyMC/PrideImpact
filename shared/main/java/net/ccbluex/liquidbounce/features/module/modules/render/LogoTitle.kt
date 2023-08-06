package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import java.awt.Color



@ModuleInfo(name = "LogoTitle",description=":)", category = ModuleCategory.RENDER)
class LogoTitle : Module() {

    private val info = TextValue("Info", "im legit")
    private val Clientname = TextValue("Clientname","PrideImpact")
    private val colorModeValue = ListValue("Color", arrayOf("Custom","rainbow","anptherrainbow"), "Custom")
    private val RedValue = IntegerValue("R", 255, 0, 255)
    private val GreenValue = IntegerValue("G", 255, 0, 255)
    private val BlueValue = IntegerValue("B", 255, 0, 255)
    private val AlphaValue = IntegerValue("A", 255,0,255)
    private val rainbowSpeed = IntegerValue("RainbowSpeed", 10, 1, 10)
    private val rainbowIndex = IntegerValue("RainbowIndex", 1, 1, 20)





    @EventTarget
    fun onRender(event: Render2DEvent){

        val TextColor = when (colorModeValue.get().toLowerCase()) {
            "rainbow" -> ColorUtils.hslRainbow(rainbowIndex.get(), indexOffset = 100 * rainbowSpeed.get()).rgb
            "anotherrainbow" -> ColorUtils.fade(Color(RedValue.get(), GreenValue.get(), BlueValue.get(), AlphaValue.get()), 100, rainbowIndex.get()).rgb
            else -> Color(RedValue.get(), GreenValue.get(), BlueValue.get(), 1).rgb
        }
        Fonts.com96.drawStringWithShadow(Clientname.get(),10,15, TextColor)
        Fonts.com35.drawStringWithShadow(LiquidBounce.CLIENT_VERSION.toString(),Fonts.com96.getStringWidth(LiquidBounce.CLIENT_NAME) + 13, 14,TextColor)
        Fonts.com35.drawStringWithShadow(info.get(),Fonts.com96.getStringWidth(LiquidBounce.CLIENT_NAME) + 13, 24,TextColor)
    }

}
