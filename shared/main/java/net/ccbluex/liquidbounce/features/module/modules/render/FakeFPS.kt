package net.ccbluex.liquidbounce.features.module.modules.render

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.FloatValue


/**
 *
 * Skid by Paimon.
 * @Date 2022/8/26          //看看mainmenu 截图
 */

@ModuleInfo(name = "FakeFPS", description = "114514FPS", category = ModuleCategory.RENDER,array = false, canEnable = false)
class FakeFPS : Module(){//应该添加fun¿ 我去的没有6666 我删了 没用
public var fps  = 0
    private val fakeFPS: FloatValue = object : FloatValue("FakeFPS", 0f, 0f, 4000f) {
        override fun onChanged(oldValue: Float, newValue: Float) {
            fps = newValue.toInt()
        }
    }
    fun getFakeFPS(): Float {
        return fakeFPS.get()
    }
    override fun onEnable() {
        fps = fakeFPS.get().toInt()
    }

}