package net.ccbluex.liquidbounce.ui.client.hud.element.elements
//Coarse_KK

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder.killCounts
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FontValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.minecraft.client.Minecraft
import java.awt.Color
import java.text.SimpleDateFormat
import java.util.*


@ElementInfo(name = "GameInfo2")
class GameInfo2(x: Double = 5.0, y: Double = 87.0, scale: Float = 1F) : Element(x, y, scale) {

    private val redValue = IntegerValue("BackgroundRed", 0, 0, 255)
    private val greenValue = IntegerValue("BackgroundGreen", 0, 0, 255)
    private val blueValue = IntegerValue("BackgroundBlue", 0, 0, 255)
    private val alpha = IntegerValue("BackgroundAlpha", 155, 0, 255)
    private val rredValue = IntegerValue("RectRed", 0, 0, 255)
    private val rgreenValue = IntegerValue("RectGreen", 0, 0, 255)
    private val rblueValue = IntegerValue("RectBlue", 0, 0, 255)
    private val ralpha = IntegerValue("RectAlpha", 192,0, 255)
    private val textredValue = IntegerValue("TextRed", 255, 0, 255)
    private val textgreenValue = IntegerValue("TextGreen", 244, 0, 255)
    private val textblueValue = IntegerValue("TextBlue", 255, 0, 255)
    private val shadowValue = BoolValue("Shadow", true)
    private val fontValue = FontValue("Font", Fonts.font35)

    private var GameInfoRows = 0
    private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss")
    var aura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura?
    var target = aura!!.target

    /**
     * Draw element
     */


    override fun drawElement(): Border {
        val icon = Fonts.flux
        val color = Color.WHITE.rgb
        val fontHeight = Fonts.font40.fontHeight
        val fontRenderer = fontValue.get()
        if(shadowValue.get()){
            RenderUtils.drawShadowWithCustomAlpha(0F, 10.5F, 150F, 70F, 200F)
        }
        RenderUtils.drawRect(0F,11F,150F,12F,ColorUtils.hslRainbow(10, indexOffset = 30).rgb)
        RenderUtils.drawRect(0F, this.GameInfoRows * 18F + 12, 150F, this.GameInfoRows * 18F + 25F, Color(rredValue.get(), rgreenValue.get(), rblueValue.get(), ralpha.get()).rgb)
        RenderUtils.drawRect(0F, this.GameInfoRows * 18F + 25F, 150F, 80F, Color(redValue.get(), greenValue.get(), blueValue.get(), alpha.get()).rgb)
        icon.drawString("c", 3F, 2.5F + fontHeight + 6F, color)
        icon.drawString("m", 3F, 15.9F + fontHeight + 6F, color)
        icon.drawString("f", 3F, 28.5F + fontHeight + 6F, color)
        icon.drawString("a", 3F, 39.5F + fontHeight + 6F, color)
        icon.drawString("x", 3F, 52F + fontHeight + 6F, color)
        Fonts.font40.drawStringWithShadow("Game Info", (5F + icon.getStringWidth("u")).toInt(),
            (this.GameInfoRows * 18F + 16).toInt(), Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font35.drawStringWithShadow("BPS: " + calculateBPS(),
            (5F + icon.getStringWidth("b")).toInt(),
            (this.GameInfoRows * 18F + 30).toInt(), Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font35.drawStringWithShadow("FPS: " + Minecraft.getDebugFPS(), (5F + icon.getStringWidth("e")).toInt(),
            (this.GameInfoRows * 18F + 43).toInt(), Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font35.drawStringWithShadow(
            "Kills: $killCounts", (5F + icon.getStringWidth("G")).toInt(),
            (this.GameInfoRows * 18F + 54).toInt(), Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)
        Fonts.font35.drawStringWithShadow("Played Time: ${DATE_FORMAT.format(Date(System.currentTimeMillis() - Recorder.startTime - 8000L * 3600L))}" ,
            (5F + icon.getStringWidth("G")).toInt(),
            (this.GameInfoRows * 18F + 66).toInt(), Color(textredValue.get(), textgreenValue.get(), textblueValue.get(), 255).rgb)

        return Border(0F, this.GameInfoRows * 18F + 12F, 150F, 80F)
    }
    fun calculateBPS(): Double {
        if(mc.thePlayer != null) {
            val bps = Math.hypot(
                mc.thePlayer!!.posX - mc.thePlayer!!.prevPosX,
                mc.thePlayer!!.posZ - mc.thePlayer!!.prevPosZ
            ) * mc.timer.timerSpeed * 20
            return Math.round(bps * 100.0) / 100.0
        }else{
            return 0.00;
        }

    }




}
