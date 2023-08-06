package net.ccbluex.liquidbounce.ui.client.hud.element.elements

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.ui.client.hud.element.Border
import net.ccbluex.liquidbounce.ui.client.hud.element.Element
import net.ccbluex.liquidbounce.ui.client.hud.element.ElementInfo
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.extensions.getDistanceToEntityBox
import net.ccbluex.liquidbounce.utils.misc.RandomUtils
import net.ccbluex.liquidbounce.utils.render.ColorUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.ccbluex.liquidbounce.value.IntegerValue
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.atan

/**
 * A target hud
 * only for Loserline & PaimonBounce
 * @author Paim0n
 */
@ElementInfo(name = "RiseTarget")
class Target2 : Element() {

    private val decimalFormat = DecimalFormat("##0.00", DecimalFormatSymbols(Locale.ENGLISH))

    private val riseCountValue = IntegerValue("Rise-Count", 5, 1, 20)
    private val riseSizeValue = FloatValue("Rise-Size", 1f, 0.5f, 3f)
    private val riseAlphaValue = FloatValue("Rise-Alpha", 0.7f, 0.1f, 1f)
    private val riseDistanceValue = FloatValue("Rise-Distance", 1f, 0.5f, 2f)
    private val riseMoveTimeValue = IntegerValue("Rise-MoveTime", 20, 5, 40)
    private val riseFadeTimeValue = IntegerValue("Rise-FadeTime", 20, 5, 40)
    private var easingHealth: Float = 0F
    private var lastTarget: IEntity? = null
    //你看你妈呢 我测你们码 //呃呃呃呃呃呃
    //你看你妈呢 我测你们码
    //你看你妈呢 我测你们码
    //你看你妈呢 我测你们码
    //你看你妈呢 我测你们码
    //你看你妈呢 我测你们码

    private val riseParticleList = mutableListOf<RiseParticle>()
    override fun drawElement(): Border {
        val target = (LiquidBounce.moduleManager[KillAura::class.java] as KillAura).target

        if (classProvider.isEntityPlayer(target) && target != null) {
            val font = Fonts.com35

            RenderUtils.drawRoundedCornerRect(0f, 0f, 150f, 50f, 5f, Color(0, 0, 0, 130).rgb)

            val hurtPercent = target.health/target.maxHealth
            val scale = if (hurtPercent == 0f) {
                1f
            } else if (hurtPercent < 0.5f) {
                1 - (0.2f * hurtPercent * 2)
            } else {
                0.8f + (0.2f * (hurtPercent - 0.5f) * 2)
            }
            val size = 30

            GL11.glPushMatrix()
            GL11.glTranslatef(5f, 5f, 0f)
            // 受伤的缩放效果
            GL11.glScalef(scale, scale, scale)
            GL11.glTranslatef(((size * 0.5f * (1 - scale)) / scale), ((size * 0.5f * (1 - scale)) / scale), 0f)
            // 受伤的红色效果
            GL11.glColor4f(1f, 1 - hurtPercent, 1 - hurtPercent, 1f)
            // 绘制头部图片
            drawHead( mc.netHandler.getPlayerInfo(target.uniqueID)!!.locationSkin, size, size)
            GL11.glPopMatrix()

            font.drawString("Name ${target.name}", 40, 11, Color.WHITE.rgb)
            font.drawString(
                "Distance ${decimalFormat.format(mc.thePlayer!!.getDistanceToEntityBox(target))} Hurt ${target.hurtTime}",
                40,
                11 + font.fontHeight,
                Color.WHITE.rgb
            )

            // 渐变血量条
            GL11.glEnable(3042)
            GL11.glDisable(3553)
            GL11.glBlendFunc(770, 771)
            GL11.glEnable(2848)
            GL11.glShadeModel(7425)
            val stopPos =
                (5 + ((135 - font.getStringWidth(decimalFormat.format(target.maxHealth))) * (target.health / target.maxHealth))).toInt()
            for (i in 5..stopPos step 5) {
                val x1 = (i + 5).coerceAtMost(stopPos).toDouble()
                RenderUtils.quickDrawGradientSidewaysH(
                    i.toDouble(),
                    39.0,
                    x1,
                    45.0,
                    ColorUtils.hslRainbow(i, indexOffset = 10).rgb,
                    ColorUtils.hslRainbow(x1.toInt(), indexOffset = 10).rgb
                )
            }
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glDisable(2848)
            GL11.glShadeModel(7424)
            GL11.glColor4f(1f, 1f, 1f, 1f)

            font.drawString(decimalFormat.format(target.health), stopPos + 5, 43 - font.fontHeight / 2, Color.WHITE.rgb)

            if (target.hurtTime >= 9) {
                for (i in 0 until riseCountValue.get()) {
                    riseParticleList.add(RiseParticle())
                }
            }

            val curTime = System.currentTimeMillis()
            riseParticleList.map { it }.forEach { rp ->
                if ((curTime - rp.time) > ((riseMoveTimeValue.get() + riseFadeTimeValue.get()) * 50)) {
                    riseParticleList.remove(rp)
                }
                val movePercent = if ((curTime - rp.time) < riseMoveTimeValue.get() * 50) {
                    (curTime - rp.time) / (riseMoveTimeValue.get() * 50f)
                } else {
                    1f
                }
                val x = (movePercent * rp.x * 0.5f * riseDistanceValue.get()) + 20
                val y = (movePercent * rp.y * 0.5f * riseDistanceValue.get()) + 20
                val alpha = if ((curTime - rp.time) > riseMoveTimeValue.get() * 50) {
                    1f - ((curTime - rp.time - riseMoveTimeValue.get() * 50) / (riseFadeTimeValue.get() * 50f)).coerceAtMost(
                        1f
                    )
                } else {
                    1f
                } * riseAlphaValue.get()
                RenderUtils.drawCircle2(
                    x,
                    y,
                    riseSizeValue.get() * 2,
                    Color(rp.color.red, rp.color.green, rp.color.blue, (alpha * 255).toInt()).rgb
                )
            }
        }
        lastTarget = target
        return  Border(0F, 0F, 150F, 50F)
    }
        class RiseParticle {
            val color = ColorUtils.rainbow(RandomUtils.nextInt(0, 30))
            val alpha = RandomUtils.nextInt(150, 255)
            val time = System.currentTimeMillis()
            val x = RandomUtils.nextInt(-50, 50)
            val y = RandomUtils.nextInt(-50, 50)
        }
    public fun drawEntityOnScreen(posX : Int,posY : Int, yaw: Float, pitch: Float, entityLivingBase: IEntityLivingBase) {
        classProvider.getGlStateManager().resetColor()
        classProvider.getGlStateManager().enableColorMaterial()
        GL11.glPushMatrix()
        GL11.glTranslatef(posX.toFloat(), posY.toFloat(), 50F)
        GL11.glScalef(15F, 15F, 15F)
        GL11.glRotatef(180F, 0F, 0F, 1F)

        val renderYawOffset = entityLivingBase.renderYawOffset
        val rotationYaw = entityLivingBase.rotationYaw
        val rotationPitch = entityLivingBase.rotationPitch
        val prevRotationYawHead = entityLivingBase.prevRotationYawHead
        val rotationYawHead = entityLivingBase.rotationYawHead

        GL11.glRotatef(135F, 0F, 1F, 0F)
        functions.enableStandardItemLighting()
        GL11.glRotatef(-135F, 0F, 1F, 0F)
        GL11.glRotatef(-atan(pitch / 40F) * 20.0F, 1F, 0F, 0F)

        entityLivingBase.renderYawOffset = atan(yaw / 40F) * 20F
        entityLivingBase.rotationYaw = atan(yaw / 40F) * 40F
        entityLivingBase.rotationPitch = -atan(pitch / 40F) * 20F
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw

        GL11.glTranslatef(0F, 0F, 0F)

        val renderManager = mc.renderManager
        renderManager.playerViewY = 180F
        renderManager.isRenderShadow = false
        renderManager.renderEntityWithPosYaw(entityLivingBase, 0.0, 0.0, 0.0, 0F, 1F)
        renderManager.isRenderShadow = true

        entityLivingBase.renderYawOffset = renderYawOffset
        entityLivingBase.rotationYaw = rotationYaw
        entityLivingBase.rotationPitch = rotationPitch
        entityLivingBase.prevRotationYawHead = prevRotationYawHead
        entityLivingBase.rotationYawHead = rotationYawHead

        GL11.glPopMatrix()
        functions.disableStandardItemLighting()
        classProvider.getGlStateManager().disableRescaleNormal()
        functions.setActiveTextureLightMapTexUnit()
        classProvider.getGlStateManager().disableTexture2D()
        functions.setActiveTextureDefaultTexUnit()
        classProvider.getGlStateManager().resetColor()
    }
    private fun drawHead(skin: IResourceLocation, width: Int, height: Int) {
        GL11.glColor4f(1F, 1F, 1F, 1F)
        mc.textureManager.bindTexture(skin)
        RenderUtils.drawScaledCustomSizeModalCircle(0, 0, 8F, 8F, 8, 8, width, height,
            64F, 64F)
    }
}