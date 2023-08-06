package net.ccbluex.liquidbounce.features.module.modules.world


import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketEntityAction
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.event.WorldEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.TextValue
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory
import java.util.Timer

@ModuleInfo(name = "KeepArmor", description = "Automatically equips the best armor in your inventory.", category = ModuleCategory.WORLD)
class KeepArmor : Module() {
    val healths = IntegerValue("Health", 5, 1, 20)
    val lobbyValue = TextValue("LobbyValue", "/hub")

    val timer = Timer()
    var check = true
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if (mc.thePlayer!!.health < healths.get()) {
            for (i in 0..3) {
                val armorSlot = 3 - i
                move(8 - armorSlot, true)
            }

            if(check) {
                mc.thePlayer!!.sendChatMessage("/hub 875")
                mc.thePlayer!!.sendChatMessage(lobbyValue.get())
                check = false
            }
        }
    }

    /**
     * Shift+Left clicks the specified item
     *
     * @param item        Slot of the item to click
     * @param isArmorSlot
     * @return True if it is unable to move the item
     */
    private fun move(item: Int, isArmorSlot: Boolean) {
        if (item != -1) {
            val openInventory = mc.currentScreen !is IIInventory
            if (openInventory) mc.netHandler.addToSendQueue(classProvider.createCPacketEntityAction(mc.thePlayer!!,ICPacketEntityAction.WAction.OPEN_INVENTORY))
            mc.thePlayer?.let {
                mc.playerController.windowClick(
                        mc.thePlayer!!.inventoryContainer.windowId, if (isArmorSlot) item else if (item < 9) item + 36 else item, 0, 1,
                        it
                )
            }
            if (openInventory) mc.netHandler.addToSendQueue(classProvider.createCPacketCloseWindow())
        }
    }

    @EventTarget
    fun onWorld(event: WorldEvent) {

        check = true
    }

    override val tag: String?
        get() = "Health " + healths.get()
}