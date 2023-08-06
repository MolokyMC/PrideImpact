package net.ccbluex.liquidbounce.features.module.modules.color

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue


@ModuleInfo(name = "CustomUI", description = "Custom", category = ModuleCategory.COLOR)
class CustomUI : Module() {


    companion object {
        @JvmField
        val r = IntegerValue("Red", 39, 0, 255)
        @JvmField
        val g = IntegerValue("Green", 120, 0, 255)
        @JvmField
        val b = IntegerValue("Blue", 186, 0, 255)
        @JvmField
        val r2= IntegerValue("Red2", 20, 0, 255)
        @JvmField
        val g2= IntegerValue("Green2", 50, 0, 255)
        @JvmField
        val b2 = IntegerValue("Blue2", 80, 0, 255)
        @JvmField
        val a = IntegerValue("Alpha", 180, 0, 255)
        @JvmField
        val radius = FloatValue("RadiusSize", 3f, 0f, 10f)
        @JvmField
        val outlinet = FloatValue("OutlineSize", 0.4f, 0f, 5f)
        @JvmField
        val drawMode = ListValue("DrawingMode", arrayOf("RoundedRect", "Outline-RoundedRect","Shadow","GaussianBlur-Shadow"), "圆角矩形")
        @JvmField
        val shadowValue = FloatValue("ShadowValue", 8f,0f,20f)
        @JvmField
        val blurValue = FloatValue("BlurValue", 15f,0f,30f)

    }




}
