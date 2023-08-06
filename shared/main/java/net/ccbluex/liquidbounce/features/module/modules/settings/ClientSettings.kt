/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package net.ccbluex.liquidbounce.features.module.modules.settings

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.LiquidBounce.CLIENT_NAME
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.Render2DEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.client.gui.ScaledResolution
import java.awt.Color

@ModuleInfo(name = "ClientSettings", description = "Settings", category = ModuleCategory.SETTINGS, canEnable = false)
class ClientSettings : Module() {
    companion object {

        @JvmStatic
        val customScoreBoard = TextValue("CustomScoreBoard", "WaWa-miHoYoTeam")

        @JvmStatic
        val clientNameLang: ListValue =
            object : ListValue("ClientNameLang", arrayOf("English", "Chinese"), "English") {
                override fun onChanged(oldValue: String, newValue: String) {
                    fun getChangedName(): String {
                        return when (newValue.toLowerCase()) {
                            "english" -> CLIENT_NAME
                            "chinese" -> "骄傲冲击"
                            else -> CLIENT_NAME
                        }
                    }
                    super.onChanged(oldValue, newValue)
                }
            }
        private val clientNameColor = ListValue(
            "ClientNameColor",
            arrayOf("Light-Blue", "Red", "Pink", "Green", "Gold", "Grey"),
            "Light-Blue"
        )

        @JvmStatic
        val settlePrefix =
            ListValue("CustomPrefix", arrayOf("Use»", "Use|", "Use>>", "Use>", "Use->"), "Use»")
        val forceFirstTitle = BoolValue("ForceFirstTitle", true)
        private val showClientName = BoolValue("ShowClientName", true)
        private val blurShader = BoolValue("BlurShader", true)


        @JvmStatic
        var prefix = "»"

        @JvmStatic
        var cname = "PrideImpact"

        @JvmStatic
        var ccolor = "§b"
    }

    @EventTarget
    fun onRender2D(event: Render2DEvent) {
        val sr = ScaledResolution(mc2)
        val height = sr.scaledHeight
        if (showClientName.get()) FontLoaders.F16.drawString(
            "$cname ${LiquidBounce.CLIENT_VERSION}",
            2.0F,
            height - (Fonts.font35.fontHeight + 2.0F),
            Color.WHITE.rgb,
            true
        )
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        val timer = MSTimer()
        if (!timer.hasTimePassed(1000L)) return
        prefix = when (settlePrefix.get().toLowerCase()) {
            "use»" -> "»"
            "use|" -> "|"
            "use>>" -> ">>"
            "use>" -> ">"
            "use->" -> "->"
            else -> "|"
        }

        cname = when (clientNameLang.get().toLowerCase()) {
            "english" -> CLIENT_NAME
            "chinese" -> "骄傲冲击"
            else -> CLIENT_NAME
        }
        ccolor = when (clientNameColor.get().toLowerCase()) {
            "light-blue" -> "§b"
            "red" -> "§c"
            "pink" -> "§d"
            "green" -> "§a"
            "gold" -> "§6"
            "grey" -> "§7"
            else -> "§b"
        }
        timer.reset()
    }


    override fun handleEvents(): Boolean = true
}