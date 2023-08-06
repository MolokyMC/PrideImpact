/*
 * ColorByte Hacked Client
 * A free half-open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/SkidderRyF/ColorByte/
 */
package net.ccbluex.liquidbounce.features.module.modules.settings
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.ui.client.hud.designer.GuiHudDesigner
@ModuleInfo(name = "HudDesigner",description = "Design the Hud", category = ModuleCategory.SETTINGS, canEnable = false)
class HudDesigner : Module() {
    override fun onEnable() {
        mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiHudDesigner()))
    }
}