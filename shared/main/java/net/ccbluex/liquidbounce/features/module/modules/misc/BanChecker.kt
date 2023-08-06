package net.ccbluex.liquidbounce.features.module.modules.misc

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.injection.backend.unwrap
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.InfosUtils.Recorder.ban
import net.ccbluex.liquidbounce.value.TextValue
import net.minecraft.network.play.server.SPacketChat
import java.util.regex.Pattern

@ModuleInfo(name = "BanChecker", description = "HuaYuTing", category = ModuleCategory.MISC)
class BanChecker : Module() {

    private val message = TextValue("BanMsg", "%name% 主播你怎么死号了")
    private val msgBack = TextValue("BanMsgBack", "已有 %ban% 人被封禁")
    private var text = ""

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet.unwrap()
        if (packet is SPacketChat) {

            val chat = packet.chatComponent.unformattedText
            if ((!chat.contains(":") || !chat.contains("]") || !chat.contains(">")) && chat.contains("在本局游戏中行为异常")) {
                val matcher = Pattern.compile("玩家(.*?)在本局游戏中行为异常").matcher(chat)
                if (matcher.find()) {
                    ban++
                    if (this.state) {
                        val name = matcher.group(1).trim()
                        if (name != mc.thePlayer!!.displayNameString) {
                            chat(name)
                        }
                    }
                }
            }
        }
    }
    private fun chat(name: String) {
        text = "@a ${message.get()} ${msgBack.get()}"
        text = text.replace("%name%",name)
        text = text.replace("%ban%",ban.toString())
        mc.thePlayer!!.sendChatMessage(text)
    }
}
