package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.network.IPacket
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer

object PacketUtils : MinecraftInstance() {
    private val packets = ArrayList<Packet<INetHandlerPlayServer>>()

    @JvmStatic
    fun handleSendPacket(packet: Packet<*>): Boolean {
        if (packets.contains(packet)) {
            packets.remove(packet)
            return true
        }
        return false
    }

    @JvmStatic
    fun sendPacketNoEvent(packet: Packet<INetHandlerPlayServer>) {
        packets.add(packet)
        mc.netHandler.addToSendQueue(packet as IPacket)
    }

    @JvmStatic
    fun getPacketType(packet: Packet<*>): PacketType {
        val className=packet.javaClass.simpleName
        if(className.startsWith("C",ignoreCase = true)){
            return PacketType.CLIENTSIDE
        }else if(className.startsWith("S",ignoreCase = true)){
            return PacketType.SERVERSIDE
        }
        return PacketType.UNKNOWN
    }

    enum class PacketType {
        SERVERSIDE,
        CLIENTSIDE,
        UNKNOWN
    }
}