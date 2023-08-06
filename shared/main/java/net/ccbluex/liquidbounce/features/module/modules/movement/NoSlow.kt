/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement

import me.utils.Debug.thePlayerisBlocking
import me.utils.PacketUtils
import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.enums.EnumFacingType
import net.ccbluex.liquidbounce.api.enums.WEnumHand
import net.ccbluex.liquidbounce.api.minecraft.item.IItem
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.*
import net.ccbluex.liquidbounce.api.minecraft.util.IEnumFacing
import net.ccbluex.liquidbounce.api.network.IPacketBuffer
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.createUseItemPacket
import net.ccbluex.liquidbounce.utils.timer.MSTimer
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.item.ItemSword
import net.minecraft.network.Packet
import net.minecraft.network.play.INetHandlerPlayServer
import net.minecraft.network.play.client.*
import net.minecraft.util.EnumFacing
import java.util.*

@ModuleInfo(name = "NoSlow", description = "Cancels slowness effects caused by soulsand and using items.",
        category = ModuleCategory.MOVEMENT)
class NoSlow : Module() {

    private val modeValue = ListValue("PacketMode", arrayOf("None",
        "GrimAC",
        "HuaYuTing",
        "Vanilla",
        "NoPacket",
        "AAC",
        "AAC5",
        "Matrix",
        "Vulcan",
        "Custom"), "GrimAC")
    private val blockForwardMultiplier = FloatValue("BlockForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val blockStrafeMultiplier = FloatValue("BlockStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeForwardMultiplier = FloatValue("ConsumeForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val consumeStrafeMultiplier = FloatValue("ConsumeStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowForwardMultiplier = FloatValue("BowForwardMultiplier", 1.0F, 0.2F, 1.0F)
    private val bowStrafeMultiplier = FloatValue("BowStrafeMultiplier", 1.0F, 0.2F, 1.0F)
    private val customOnGround = BoolValue("CustomOnGround", false)
    private val customDelayValue = IntegerValue("CustomDelay",60,10,200)

    val packetValue = BoolValue("Renderer", true)

    // Soulsand
    val soulsandValue = BoolValue("Soulsand", true)

    val timer = MSTimer()
    private val Timer = MSTimer()
    private var pendingFlagApplyPacket = false
    private val msTimer = MSTimer()
    private var sendBuf = false
    private var packetBuf = LinkedList<Packet<INetHandlerPlayServer>>()
    private var nextTemp = false
    private var waitC03 = false
    private var lastBlockingStat = false

    var float = 0


    val killAura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura


    fun isBlock(): Boolean {
        return thePlayerisBlocking || killAura.blockingStatus
    }

    fun fuckKotline(value: Int): Boolean{
        return value == 1
    }

    private fun OnPre(event : MotionEvent): Boolean {
        return event.eventState == EventState.PRE
    }

    private fun OnPost(event : MotionEvent): Boolean {
        return event.eventState == EventState.POST
    }

    private val isBlocking: Boolean
        get() = (mc.thePlayer!!.isUsingItem || (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).blockingStatus) && mc.thePlayer!!.heldItem != null && mc.thePlayer!!.heldItem!!.item is ItemSword

    override fun onDisable() {
        Timer.reset()
        msTimer.reset()
        pendingFlagApplyPacket = false
        sendBuf = false
        packetBuf.clear()
        nextTemp = false
        waitC03 = false
    }

    private fun sendPacket(Event : MotionEvent,SendC07 : Boolean, SendC08 : Boolean,Delay : Boolean,DelayValue : Long,onGround : Boolean,Hypixel : Boolean = false,GrimAC:Boolean = false) {
        val aura = LiquidBounce.moduleManager[KillAura::class.java] as KillAura
        val digging = classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos(-1,-1,-1),EnumFacing.DOWN as IEnumFacing)
        val grimDigging = classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN))
        val blockPlace = classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.currentItem as IItemStack)
        val blockMent = classProvider.createCPacketPlayerBlockPlacement(WBlockPos(-1, -1, -1), 255, mc.thePlayer!!.inventory.currentItem as IItemStack, 0f, 0f, 0f)
        if(onGround && !mc.thePlayer!!.onGround) {
            return
        }

        if(SendC07 && OnPre(Event)) {
            if (GrimAC){
                if (classProvider.isItemSword(mc.thePlayer!!.inventory.getCurrentItemInHand())) {
                    if (mc.thePlayer!!.isUsingItem || mc.thePlayer!!.isBlocking || isBlock()) {
                        mc.netHandler.addToSendQueue(grimDigging)
                    }
                }
            }else {
                if (Delay && Timer.hasTimePassed(DelayValue)) {
                    mc.netHandler.addToSendQueue(digging)
                } else if (!Delay) {
                    mc.netHandler.addToSendQueue(digging)
                }
            }
        }else if (GrimAC && OnPre(Event)){
            mc2.connection!!.sendPacket(CPacketHeldItemChange((mc2.player.inventory.currentItem + 1) % 9))
            mc2.connection!!.sendPacket(CPacketHeldItemChange(mc2.player.inventory.currentItem))
        }
        if(SendC08 && OnPost(Event) && !GrimAC) {
            if(Delay && Timer.hasTimePassed(DelayValue) && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
                Timer.reset()
            } else if(!Delay && !Hypixel) {
                mc.netHandler.addToSendQueue(blockPlace)
            } else if(Hypixel) {
                mc.netHandler.addToSendQueue(blockMent)
            }
        }
    }

    @EventTarget
    fun onMotion(event: MotionEvent) {
        val thePlayer = mc.thePlayer ?: return
        var test = fuckKotline(mc.thePlayer!!.ticksExisted and 1)
        val heldItem = thePlayer.heldItem

        if (!MovementUtils.isMoving) {
            return
        }

            when(modeValue.get().toLowerCase()) {
                "custom" -> {
                    sendPacket(event,true,true,true,customDelayValue.get().toLong(),customOnGround.get())
                }
                "grimfull" -> {
                    sendPacket(event,true,false,false,0L,
                        false,false,true)
                    if (event.eventState == EventState.PRE && mc.thePlayer!!.itemInUse != null &&
                        mc.thePlayer!!.itemInUse!!.item != null) {
                        if (!classProvider.isItemSword(mc.thePlayer!!.inventory.getCurrentItemInHand()) ||
                            !classProvider.isItemBlock(mc.thePlayer!!.inventory.getCurrentItemInHand())) {
                            if (mc.thePlayer!!.isUsingItem) {
                                sendPacket(
                                    event,false,false,false,0L,
                                    false,false,true)
                            }
                        }
                    }
                }
                "grimac" -> {
                    if (heldItem != null) {
                        if (classProvider.isItemSword(heldItem.item) && MovementUtils.isMoving && (thePlayer.isBlocking||thePlayer.isUsingItem||mc.gameSettings.keyBindUseItem.isKeyDown||killAura.blockingStatus)) {
                            if(float > 1){
                                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange((mc.thePlayer!!.inventory.currentItem + 1) % 9))
                                mc.netHandler.addToSendQueue(createUseItemPacket(thePlayer.inventory.getCurrentItemInHand(), WEnumHand.MAIN_HAND))
                                mc.netHandler.addToSendQueue(createUseItemPacket(thePlayer.inventory.getCurrentItemInHand(), WEnumHand.OFF_HAND))
                                mc.netHandler.addToSendQueue(classProvider.createCPacketHeldItemChange((mc.thePlayer!!.inventory.currentItem) % 9))
                                float = 0
                            }
                        }
                    }
                }
                "vanilla" -> {
                    mc.thePlayer!!.motionX=mc.thePlayer!!.motionX
                    mc.thePlayer!!.motionY=mc.thePlayer!!.motionY
                    mc.thePlayer!!.motionZ= mc.thePlayer!!.motionZ
                }
                "hauyuting" -> {
                    if (event.eventState == EventState.PRE) {
                        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM,
                                WBlockPos.ORIGIN, classProvider.getEnumFacing(EnumFacingType.DOWN)))
                        mc.netHandler.addToSendQueue(classProvider.createCPacketPlayerBlockPlacement(mc.thePlayer!!.inventory.getCurrentItemInHand() as IItemStack))
                    }
                }
                "aac" -> {
                    if (mc.thePlayer!!.ticksExisted % 3 == 0) {
                        sendPacket(event, true, false, false, 0, false)
                    } else {
                        sendPacket(event, false, true, false, 0, false)
                    }
                }
                "aac5" -> {
                    if (mc.thePlayer!!.isUsingItem || mc.thePlayer!!.isBlocking || isBlock()) {
                        mc.netHandler.addToSendQueue(createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.MAIN_HAND))
                        mc.netHandler.addToSendQueue(createUseItemPacket(mc.thePlayer!!.inventory.getCurrentItemInHand(), WEnumHand.OFF_HAND))
                    }
                }
            }


    }

    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if(modeValue.equals("Matrix") || modeValue.equals("Vulcan")&& nextTemp) {
            if((packet is CPacketPlayerDigging || packet is ICPacketPlayerBlockPlacement) && isBlocking) {
                event.cancelEvent()
            }
            event.cancelEvent()
        }else if (packet is CPacketPlayer || packet is CPacketAnimation || packet is CPacketEntityAction || packet is CPacketUseEntity || packet is CPacketPlayerDigging || packet is ICPacketPlayerBlockPlacement) {
            if (modeValue.equals("Vulcan") && waitC03 && packet is ICPacketPlayer) {
                waitC03 = false
                return
            }
            packetBuf.add(packet as Packet<INetHandlerPlayServer>)
        }
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent) {
        if((modeValue.equals("Matrix") || modeValue.equals("Vulcan")) && (lastBlockingStat || isBlocking)) {
            if(msTimer.hasTimePassed(230) && nextTemp) {
                nextTemp = false
                classProvider.createCPacketPlayerDigging(ICPacketPlayerDigging.WAction.RELEASE_USE_ITEM, WBlockPos(-1, -1, -1), EnumFacing.DOWN as IEnumFacing)
                if(packetBuf.isNotEmpty()) {
                    var canAttack = false
                    for(packet in packetBuf) {
                        if(packet is CPacketPlayer) {
                            canAttack = true
                        }
                        if(!((packet is ICPacketUseEntity || packet is ICPacketAnimation) && !canAttack)) {
                            PacketUtils.sendPacketNoEvent(packet)
                        }
                    }
                    packetBuf.clear()
                }
            }
            if(!nextTemp) {
                lastBlockingStat = isBlocking
                if (!isBlocking) {
                    return
                }
                classProvider.createCPacketPlayerBlockPlacement(WBlockPos(-1, -1, -1), 255, mc.thePlayer!!.inventory.currentItem as IItemStack, 0f, 0f, 0f)
                nextTemp = true
                waitC03 = modeValue.equals("Vulcan")
                msTimer.reset()
            }
        }
    }

    @EventTarget
    fun onSlowDown(event: SlowDownEvent) {
        val heldItem = mc.thePlayer!!.heldItem?.item

        event.forward = getMultiplier(heldItem, true)
        event.strafe = getMultiplier(heldItem, false)
    }

    private fun getMultiplier(item: IItem?, isForward: Boolean): Float {
        return when {
            classProvider.isItemFood(item) || classProvider.isItemPotion(item) || classProvider.isItemBucketMilk(item) -> {
                if (isForward) this.consumeForwardMultiplier.get() else this.consumeStrafeMultiplier.get()
            }
            classProvider.isItemSword(item) -> {
                if (isForward) this.blockForwardMultiplier.get() else this.blockStrafeMultiplier.get()
            }
            classProvider.isItemBow(item) -> {
                if (isForward) this.bowForwardMultiplier.get() else this.bowStrafeMultiplier.get()
            }
            else -> 0.2F
        }
    }

    override val tag: String?
        get() = modeValue.get()

}
