package net.ccbluex.liquidbounce.features.module.modules.world

import net.ccbluex.liquidbounce.LiquidBounce.moduleManager
import net.ccbluex.liquidbounce.api.enums.BlockType
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import kotlin.jvm.internal.Intrinsics

@ModuleInfo(name = "ScaffoldTelly", description = "Helper ur Telly by Scaffold", category = ModuleCategory.WORLD)
class ScaffoldTelly : Module() {
    private val scaffoldModule =
        ListValue("ScaffoldModule", arrayOf("Scaffold", "SkyrimScaffold"), "Scaffold")
    private val autoJumpValue = BoolValue("AutoJump", false)
    private val autoJumpHelper =
        ListValue("JumpHelper", arrayOf("Parkour", "Eagle", "OnMove"), "Parkour").displayable { autoJumpValue.get() }


    private val autoTimerValue = BoolValue("AutoTimer", false)
    private val autoPitchValue = BoolValue("setBestPitch", false)
    private val alwaysPitchValue = BoolValue("setPitch-onUpdate", false)
    private val autoYawValue = ListValue("setYawMode", arrayOf("None", "onEnable", "onUpdate"), "None")

    override fun onEnable() {
        if (autoPitchValue.get()) {
            mc.thePlayer!!.rotationPitch = 26.5F
        }
        if (autoYawValue.get() == "onEnable") setYaw()
    }

    @EventTarget
    fun onUpdate(event: UpdateEvent?) {
        val thePlayer = mc.thePlayer!!

        if (autoPitchValue.get() && alwaysPitchValue.get()) {
            mc.thePlayer!!.rotationPitch = 26.5F
        }
        if (autoYawValue.get() == "onUpdate") setYaw()
        if (!thePlayer.sneaking) {
            val thePlayer2 = mc.thePlayer
            if (thePlayer2 == null) {
                Intrinsics.throwNpe()
            }
            if (thePlayer2!!.onGround) {
                scaffoldChange(false)
                if (autoTimerValue.get()) if (moduleManager.getModule(Timer::class.java).state) moduleManager.getModule(
                    Timer::class.java
                ).state = false
            } else {
                scaffoldChange(true)
                if (autoTimerValue.get()) if (!moduleManager.getModule(Timer::class.java).state) moduleManager.getModule(
                    Timer::class.java
                ).state = true
            }
        }
        if (autoJumpValue.get()) tryJump()
    }

    private fun jump() = mc.thePlayer!!.jump()


    @EventTarget
    override fun onDisable() {
        scaffoldChange(false)
        if (autoTimerValue.get()) if (moduleManager.getModule(Timer::class.java).state) moduleManager.getModule(Timer::class.java).state =
            false
        disable()
    }

    private fun scaffoldChange(state: Boolean) {
        when (scaffoldModule.get().toLowerCase()) {
            "scaffold" -> moduleManager.getModule(Scaffold::class.java).state = state
            "skyrimscaffold" -> moduleManager.getModule(SkyrimScaffold::class.java).state = state
        }
    }

    private fun tryJump() {
        val thePlayer = mc.thePlayer!!
        when (autoJumpHelper.get().toLowerCase()) {
            "parkour" -> if (MovementUtils.isMoving && thePlayer.onGround && !thePlayer.sneaking && !mc.gameSettings.keyBindSneak.isKeyDown && !mc.gameSettings.keyBindJump.isKeyDown &&
                mc.theWorld!!.getCollidingBoundingBoxes(
                    thePlayer, thePlayer.entityBoundingBox
                        .offset(0.0, -0.5, 0.0).expand(-0.001, 0.0, -0.001)
                ).isEmpty()
            ) {
                jump()
            }

            "eagle" -> {
                if (mc.theWorld!!.getBlockState(
                        WBlockPos(
                            thePlayer.posX,
                            thePlayer.posY - 1.0,
                            thePlayer.posZ
                        )
                    ).block == classProvider.getBlockEnum(
                        BlockType.AIR
                    ) && thePlayer.onGround
                ) jump()
            }

            "onmove" -> {
                if (thePlayer.onGround && MovementUtils.isMoving && thePlayer.sprinting) {
                    jump()
                }
            }
        }
    }

    private fun disable() {
        when (scaffoldModule.get().toLowerCase()) {
            "scaffold" -> moduleManager.getModule(Scaffold::class.java).state = false
            "skyrimscaffold" -> moduleManager.getModule(SkyrimScaffold::class.java).state = false
        }
    }

    private fun setYaw() {
        val thePlayer = mc.thePlayer!!
        if (autoYawValue.get().toLowerCase() == "none") return
        val x = java.lang.Double.valueOf(thePlayer.motionX)
        val y = java.lang.Double.valueOf(thePlayer.motionZ)
        if (mc.gameSettings.keyBindForward.isKeyDown) {
            if (y != null && y.toDouble() > 0.1) thePlayer.rotationYaw = 0.0f

            if (y != null && y.toDouble() < -0.1) thePlayer.rotationYaw = 180.0f

            if (x != null && x.toDouble() > 0.1) thePlayer.rotationYaw = -90.0f

            if (x != null && x.toDouble() < -0.1) thePlayer.rotationYaw = 90.0f
        }
    }
}
