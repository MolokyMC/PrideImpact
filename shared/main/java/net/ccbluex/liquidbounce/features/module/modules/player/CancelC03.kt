package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.ModuleManager
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.SuperKnockback
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue

/**
 * Skid or Made By WaWa
 * @date 2023/6/22 14:37
 * @author WaWa
 */
@ModuleInfo(name = "CancelC03", description = "By WaWa", category = ModuleCategory.PLAYER)
class CancelC03 : Module(){
    val isSetRange = BoolValue("SetKillAuraRange",false)
    val rangeValue = FloatValue("Range",5F,0F,7F)
    val isSetMulti = BoolValue("SetKillAuraMulti",false)
    val isSetFov = BoolValue("SetKillAuraFOV",false)
    val isSetOnlyM = BoolValue("SetSkbOnlyMove",false)
    val isSetOnlyG = BoolValue("SetSkbOnlyGround",false)

    val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura

    var range = 0F

    var fov = 0F

    var airbypass = false

    var multi = ""

    var skbOnlyMove = false

    var skbOnlyGround = false

    override fun onEnable() {
        range = 0F
        fov = 0F
        airbypass = false
        multi = ""
        skbOnlyMove = false
        skbOnlyGround = false
        range = killAura.rangeValue.get()
        fov = killAura.fovValue.get()
        multi = killAura.targetModeValue.get()
        skbOnlyMove = (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyMoveValue.get()
        skbOnlyGround = (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyGroundValue.get()
    }

    override fun onDisable() {
        killAura.rangeValue.set(range)
        killAura.fovValue.set(fov)
        killAura.targetModeValue.set(multi)
        (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyGroundValue.set(skbOnlyGround)
        (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyMoveValue.set(skbOnlyMove)
        range = 0F
        airbypass = false
        fov = 0F
        multi = ""
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        if (isSetRange.get() && killAura.rangeValue.get() != rangeValue.get()) killAura.rangeValue.set(rangeValue.get())
        if (isSetMulti.get() && killAura.targetModeValue.get() != "Multi") killAura.targetModeValue.set("Multi")
        if (isSetFov.get() && killAura.fovValue.get() != 360f) killAura.fovValue.set(360f)
        if (isSetOnlyG.get() && (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyGroundValue.get()) (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyGroundValue.set(false)
        if (isSetOnlyM.get() && (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyMoveValue.get()) (LiquidBounce.moduleManager[SuperKnockback::class.java] as SuperKnockback)!!.onlyMoveValue.set(false)
    }
    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet
        if (classProvider.isCPacketPlayer(packet)){
            event.cancelEvent()
        }
    }
    @EventTarget
    fun onMove(event: MoveEvent){
        event.zero()
    }
    override val tag: String
        get() = "GrimAC"
}