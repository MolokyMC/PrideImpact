package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.value.BoolValue

@ModuleInfo(name = "FlagTips", description = "You Flag Tips.", category = ModuleCategory.MISC)
class FlagTips : Module(){
    private var packettips = BoolValue("PacketTips",false)
    private var resetflagnumbertips = BoolValue("ResetFlagNumberTips",false)
    private var flag = 0
    var time = 0
    var aa = 0
    var packets = 0
    var started = false

    override fun onEnable() {
        started = false
        flag = 0
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent){
        time++
        if(packets > 999) {
            if(packettips.get())
                ClientUtils.displayChatMessage("§7[§8§6PrideImpact§7]§f您的发包速度过快!,请调整发包功能")
        }
    }

    fun getPacket() : String {
        return if(started) {
            if(packets < 999) {
                packets.toString()
            } else {
                "§d$packets"
            }
        } else {
            "0"
        }
    }

    @EventTarget
    fun onPacket(event: PacketEvent){
        val packet = event.packet
        if(classProvider.isSPacketPlayerPosLook(packet)){
            flag += 1
            ClientUtils.displayChatMessage("§7[§8§6PrideImpact§7]§f您已Flag次数:$flag,目前发包速度:${getPacket()}/s")
        }
        if(classProvider.isSPacketChat(packet)){
            if(resetflagnumbertips.get())
                if(packet.asSPacketChat().chatComponent.unformattedText.equals("游戏开始...",true)){
                    flag = 0
                }
        }
        aa++
        if(time >= 20) {
            time = 0
            packets = aa
            aa = 0
            started = true
        }
    }

    override val tag: String?
        get() = "Packet:${getPacket()}/s"
}