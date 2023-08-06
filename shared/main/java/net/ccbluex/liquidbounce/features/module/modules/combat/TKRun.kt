package net.ccbluex.liquidbounce.features.module.modules.combat;

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.combat.Velocity
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.client.gui.inventory.GuiInventory

@ModuleInfo(name = "TKRun",description = "自动逃逸", category = ModuleCategory.COMBAT)
class TKRun : Module() {

    /**
     * author: public
     */

    var health = FloatValue("Health",5F, 0F, 20F)
    var text = TextValue("Text", "/lobby")
    var autoDisable = BoolValue("AutoDisable", true)
    var keepArmor = BoolValue("KeepArmor", true)

    var lmao = false

    fun autoArmor(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = mc.currentScreen !is GuiInventory
            if (openInventory) mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!,
                    ICPacketEntityAction.WAction.OPEN_INVENTORY))
            mc.playerController.windowClick(
                    mc.thePlayer!!.inventoryContainer.windowId, if (isArmorSlot) item else if (item < 9) item + 36 else item, 0, 1, mc.thePlayer!!
            )
            if (openInventory) mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {

        if (mc.thePlayer!!.health < health.get()) {
            if (keepArmor.get()) {
                for (i in 0..3) {
                    val armorSlot = 3 - i
                    autoArmor(8 - armorSlot, true)
                }
            }

            if (mc.thePlayer!!.health <= health.get() && lmao == false) {
                mc2.player.sendChatMessage("[Drama]Bye")
                mc2.player.sendChatMessage(text.get())
                lmao = true
            }

            if (mc.thePlayer!!.health <= health.get() && autoDisable.get()) {
                val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
                val velocity = LiquidBounce.moduleManager[Velocity::class.java] as Velocity
                val speed = LiquidBounce.moduleManager[Speed::class.java] as Speed

                killAura.state = false
                velocity.state = false
                speed.state = false
            }

            if (mc.thePlayer!!.health >= health.get()) {
                lmao = false;
            }
        }
    }
}
