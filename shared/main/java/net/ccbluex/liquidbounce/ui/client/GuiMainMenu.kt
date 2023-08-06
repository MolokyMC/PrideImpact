/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.ui.client


import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen
import net.ccbluex.liquidbounce.ui.client.altmanager.GuiAltManager
import net.ccbluex.liquidbounce.ui.cnfont.FontLoaders
import net.ccbluex.liquidbounce.ui.font.Fonts
import net.ccbluex.liquidbounce.utils.AnimationUtils
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.RandomImgUtils
import net.ccbluex.liquidbounce.utils.misc.MiscUtils
import net.ccbluex.liquidbounce.utils.render.RenderUtils
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.concurrent.thread

class GuiMainMenu : WrappedGuiScreen() {

    val bigLogo = ResourceLocation("pride/logo.png")
    val darkIcon = ResourceLocation("pride/menu/dark.png")
    val lightIcon = ResourceLocation("pride/menu/light.png")
    val background = RandomImgUtils.getBackGround()

    var slideX : Float = 0F
    var fade : Float = 0F

    var sliderX : Float = 0F
    var sliderDarkX : Float = 0F

    var lastAnimTick: Long = 0L
    var alrUpdate = false

    var lastXPos = 0F

    var extendedModMode = false
    var extendedBackgroundMode = false

    //WaWa
    private var currentX = 0f
    private var currentY = 0f

    companion object {
        var useParallax = false
    }

    override fun initGui() {
        slideX = 0F
        fade = 0F
        sliderX = 0F
        sliderDarkX = 0F
        super.initGui()
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val height = representedScreen.height
        val width = representedScreen.width
        if (!alrUpdate) {
            lastAnimTick = System.currentTimeMillis()
            alrUpdate = true
        }
        val creditInfo = "Copyright Mojang AB. Do not distribute!"
        //drawBackground(0)

        val res = ScaledResolution(mc2);
        val xDiff: Float = ((mouseX - height / 2).toFloat() - this.currentX) / res.scaleFactor.toFloat()
        val yDiff: Float = ((mouseY - width / 2).toFloat() - this.currentY) / res.scaleFactor.toFloat()
        this.currentX += xDiff * 0.3f
        this.currentY += yDiff * 0.3f
        GlStateManager.translate(this.currentX / 30.0f, this.currentY / 15.0f, 0.0f)
        RenderUtils.drawImage4(background, -30, -30, res.scaledWidth + 60, res.scaledHeight + 60)
        GlStateManager.translate(-this.currentX / 30.0f, -this.currentY / 15.0f, 0.0f)

        val list = LiquidBounce.UPDATE_LIST
        var length = 2

        //for ((i, _: String) in list.withIndex()) {
        //    length += if (i <= 0) Fonts.font40.fontHeight + 2 else Fonts.font35.fontHeight + 2
        //}

        GL11.glPushMatrix()
        renderSwitchButton()
        renderDarkModeButton()
        FontLoaders.F16.drawStringWithShadow("${LiquidBounce.CLIENT_NAME} b${LiquidBounce.CLIENT_VERSION} | miHoYo.com",
            2.0, height - 12.0, -1)
        FontLoaders.F16.drawStringWithShadow(creditInfo, width - 3.0 - FontLoaders.F16.getStringWidth(creditInfo), height - 12.0, -1)

        //Text
        for ((i, _: String) in list.withIndex()) {
            length += if (i <= 0) {
                FontLoaders.F16.drawString(list[i], 2F, length.toFloat(), Color(255, 255, 255, 255).rgb, true)
                Fonts.ComfortaaRegular35.fontHeight + 2
           } else {
                FontLoaders.F16.drawString(list[i], 2F, length.toFloat(), Color(255, 255, 255, 255).rgb, true)
                Fonts.ComfortaaRegular35.fontHeight + 2
           }
        }

        if (useParallax) moveMouseEffect(mouseX, mouseY, 10F)
        GlStateManager.disableAlpha()
        RenderUtils.drawImage2(bigLogo, width / 2 - 50, height / 2 - 90, 100, 100)
        GlStateManager.enableAlpha()
        renderBar(mouseX, mouseY, partialTicks)
        GL11.glPopMatrix()
        super.drawScreen(mouseX, mouseY, partialTicks)

        if (!LiquidBounce.mainMenuPrep) {
            val animProgress = ((System.currentTimeMillis() - lastAnimTick).toFloat() / 1500F).coerceIn(0F, 1F)
            RenderUtils.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(),10F, Color(0F, 0F, 0F, 1F - animProgress).rgb)
            if (animProgress >= 1F)
                LiquidBounce.mainMenuPrep = true
        }
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        val height = representedScreen.height
        val width = representedScreen.width
        if (!LiquidBounce.mainMenuPrep || mouseButton != 0) return

        if (isMouseHover(2F, height - 26F, 28F, height - 16F, mouseX, mouseY))
            useParallax = !useParallax

        if (isMouseHover(2F, height - 38F, 28F, height - 28F, mouseX, mouseY))
            LiquidBounce.darkMode = !LiquidBounce.darkMode

        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F
        var index: Int = 0
        for (icon in if (extendedModMode) ExtendedImageButton.values() else ImageButton.values()) {
            if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY))
                when (index) {
                    0 -> if (extendedBackgroundMode) extendedBackgroundMode = false else if (extendedModMode) extendedModMode = false else mc.displayGuiScreen(classProvider.createGuiSelectWorld(this.representedScreen))
                    1 -> if (extendedBackgroundMode) GuiBackground.enabled = !GuiBackground.enabled else if (extendedModMode) mc.displayGuiScreen(
                        classProvider.wrapGuiScreen(GuiModsMenu(this.representedScreen))
                    ) else mc.displayGuiScreen(classProvider.createGuiMultiplayer(this.representedScreen))
                    2 -> if (extendedBackgroundMode) GuiBackground.particles = !GuiBackground.particles else if (extendedModMode) mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiScripts(this.representedScreen))) else mc.displayGuiScreen(classProvider.wrapGuiScreen(GuiAltManager(this.representedScreen)))
                    3 -> if (extendedBackgroundMode) {
                        val file = MiscUtils.openFileChooser() ?: return
                        if (file.isDirectory) return

                        try {
                            Files.copy(file.toPath(), FileOutputStream(LiquidBounce.fileManager.backgroundFile))

                            val image = ImageIO.read(FileInputStream(LiquidBounce.fileManager.backgroundFile))
                            LiquidBounce.background = classProvider.createResourceLocation("pride/background.png")
                            mc.textureManager.loadTexture(LiquidBounce.background!!, classProvider.createDynamicTexture(image))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            MiscUtils.showErrorPopup("Error", "Exception class: " + e.javaClass.name + "\nMessage: " + e.message)
                            LiquidBounce.fileManager.backgroundFile.delete()
                        }
                    } else if (extendedModMode) {
                        val rpc = LiquidBounce.clientRichPresence
                        rpc.showRichPresenceValue = when (val state = !rpc.showRichPresenceValue) {
                            false -> {
                                rpc.shutdown()
                                false
                            }
                            true -> {
                                var value = true
                                thread {
                                    value = try {
                                        rpc.setup()
                                        true
                                    } catch (throwable: Throwable) {
                                        ClientUtils.getLogger().error("Failed to setup Discord RPC.", throwable)
                                        false
                                    }
                                }
                                value
                            }
                        }
                    } else mc.displayGuiScreen(classProvider.createGuiOptions(this.representedScreen, mc.gameSettings))
                    4 -> if (extendedBackgroundMode) {
                        LiquidBounce.background = null
                        LiquidBounce.fileManager.backgroundFile.delete()
                    } else if (extendedModMode) extendedBackgroundMode = true else extendedModMode = true
                    5 -> mc.shutdown()
                }

            index++
        }

        super.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun moveMouseEffect(mouseX: Int, mouseY: Int, strength: Float) {
        val height = representedScreen.height
        val width = representedScreen.width
        val mX = mouseX - width / 2
        val mY = mouseY - height / 2
        val xDelta = mX.toFloat() / (width / 2).toFloat()
        val yDelta = mY.toFloat() / (height / 2).toFloat()

        GL11.glTranslatef(xDelta * strength, yDelta * strength, 0F)
    }

    fun renderSwitchButton() {
        val height = representedScreen.height
        sliderX = (sliderX + (if (useParallax) 2F else -2F)).coerceIn(0F, 12F)
        FontLoaders.F16.drawStringWithShadow("Parallax", 28.0, height - 25.0, -1)
        RenderUtils.drawRoundRect(4F, height - 24F, 22F, height - 18F, 3F, if (useParallax) Color(0, 111, 255, 255).rgb else (if (LiquidBounce.darkMode) Color(70, 70, 70, 255) else Color(140, 140, 140, 255)).rgb)
        RenderUtils.drawRoundRect(2F + sliderX, height - 26F, 12F + sliderX, height - 16F, 5F, Color.white.rgb)
    }

    fun renderDarkModeButton() {
        val height = representedScreen.height
        sliderDarkX = (sliderDarkX + (if (LiquidBounce.darkMode) 2F else -2F)).coerceIn(0F, 12F)
        GlStateManager.disableAlpha()
        RenderUtils.drawImage3(darkIcon, 28F, height - 40F, 14, 14, 1F, 1F, 1F, sliderDarkX / 12F)
        RenderUtils.drawImage3(lightIcon, 28F, height - 40F, 14, 14, 1F, 1F, 1F, 1F - (sliderDarkX / 12F))
        GlStateManager.enableAlpha()
        RenderUtils.drawRoundRect(4F, height - 36F, 22F, height - 30F, 3F, (if (LiquidBounce.darkMode) Color(70, 70, 70, 255) else Color(140, 140, 140, 255)).rgb)
        RenderUtils.drawRoundRect(2F + sliderDarkX, height - 38F, 12F + sliderDarkX, height - 28F, 5F, Color.white.rgb)
    }

    fun renderBar(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val height = representedScreen.height
        val width = representedScreen.width
        val staticX = width / 2F - 120F
        val staticY = height / 2F + 20F

        RenderUtils.drawRoundRect(staticX, staticY, staticX + 240F, staticY + 20F,10F, (if (LiquidBounce.darkMode) Color(0, 0, 0, 100) else Color(255, 255, 255, 100)).rgb)

        var index: Int = 0
        var shouldAnimate = false
        var displayString: String? = null
        var moveX = 0F
        if (extendedModMode) {
            if (extendedBackgroundMode)
                for (icon in ExtendedBackgroundButton.values()) {
                    if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY)) {
                        shouldAnimate = true
                        displayString = if (icon == ExtendedBackgroundButton.Enabled)
                            "Custom background: ${if (GuiBackground.enabled) "§aON" else "§cOFF"}"
                        else if (icon == ExtendedBackgroundButton.Particles)
                            "${icon.buttonName}: ${if (GuiBackground.particles) "§aON" else "§cOFF"}"
                        else
                            icon.buttonName
                        moveX = staticX + 40F * index
                    }
                    index++
                }
            else
                for (icon in ExtendedImageButton.values()) {
                    if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY)) {
                        shouldAnimate = true
                        displayString = if (icon == ExtendedImageButton.DiscordRPC) "${icon.buttonName}: ${if (LiquidBounce.clientRichPresence.showRichPresenceValue) "§aON" else "§cOFF"}" else icon.buttonName
                        moveX = staticX + 40F * index
                    }
                    index++
                }
        } else
            for (icon in ImageButton.values()) {
                if (isMouseHover(staticX + 40F * index, staticY, staticX + 40F * (index + 1), staticY + 20F, mouseX, mouseY)) {
                    shouldAnimate = true
                    displayString = icon.buttonName
                    moveX = staticX + 40F * index
                }
                index++
            }

        if (displayString != null)
            FontLoaders.F16.drawCenteredString(displayString!!, width / 2.0, staticY + 30.0, -1,true)
        else
            FontLoaders.F16.drawCenteredString("Genshin Impact", width / 2.0, staticY + 30.0, -1,true)

        if (shouldAnimate) {
            if (fade == 0F)
                slideX = moveX
            else
                slideX = AnimationUtils.animate(moveX, slideX, 0.5F * (1F - partialTicks))

            lastXPos = moveX

            fade += 10F
            if (fade >= 100F) fade = 100F
        } else {
            fade -= 10F
            if (fade <= 0F) fade = 0F

            slideX = AnimationUtils.animate(lastXPos, slideX, 0.5F * (1F - partialTicks))
        }

        if (fade != 0F)
            RenderUtils.drawRoundRect(slideX, staticY, slideX + 40F, staticY + 20F,10F, (if (LiquidBounce.darkMode) Color(0F, 0F, 0F, fade / 100F * 0.6F) else Color(1F, 1F, 1F, fade / 100F * 0.6F)).rgb)

        index = 0
        GlStateManager.disableAlpha()
        if (extendedModMode) {
            if (extendedBackgroundMode)
                for (i in ExtendedBackgroundButton.values()) {
                    if (LiquidBounce.darkMode)
                        RenderUtils.drawImage2(i.texture, (staticX + 40 * index + 11).toInt(), (staticY + 1).toInt(), 18, 18)
                    else
                        RenderUtils.drawImage3(i.texture, staticX + 40F * index + 11F, staticY + 1F, 18, 18, 0F, 0F, 0F, 1F)
                    index++
                }
            else
                for (i in ExtendedImageButton.values()) {
                    if (LiquidBounce.darkMode)
                        RenderUtils.drawImage2(i.texture, (staticX + 40 * index + 11).toInt(), (staticY + 1).toInt(), 18, 18)
                    else
                        RenderUtils.drawImage3(i.texture, staticX + 40F * index + 11F, staticY + 1F, 18, 18, 0F, 0F, 0F, 1F)
                    index++
                }
        } else
            for (i in ImageButton.values()) {
                if (LiquidBounce.darkMode)
                    RenderUtils.drawImage2(i.texture, (staticX + 40 * index + 11).toInt(), (staticY + 1).toInt(), 18, 18)
                else
                    RenderUtils.drawImage3(i.texture, staticX + 40F * index + 11F, staticY + 1F, 18, 18, 0F, 0F, 0F, 1F)
                index++
            }
        GlStateManager.enableAlpha()
    }

    fun isMouseHover(x: Float, y: Float, x2: Float, y2: Float, mouseX: Int, mouseY: Int): Boolean = mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2

    enum class ImageButton(val buttonName: String, val texture: ResourceLocation) {
        Single("Singleplayer", ResourceLocation("pride/menu/singleplayer.png")),
        Multi("Multiplayer", ResourceLocation("pride/menu/multiplayer.png")),
        Alts("Alts", ResourceLocation("pride/menu/alt.png")),
        Settings("Settings", ResourceLocation("pride/menu/settings.png")),
        Mods("Mods/Customize", ResourceLocation("pride/menu/mods.png")),
        Exit("Exit", ResourceLocation("pride/menu/exit.png"))
    }

    enum class ExtendedImageButton(val buttonName: String, val texture: ResourceLocation) {
        Back("Back", ResourceLocation("pride/clickgui/back.png")),
        Mods("Mods", ResourceLocation("pride/menu/mods.png")),
        Scripts("Scripts", ResourceLocation("pride/clickgui/docs.png")),
        DiscordRPC("Discord RPC", ResourceLocation("pride/menu/discord.png")),
        Background("Background", ResourceLocation("pride/menu/wallpaper.png")),
        Exit("Exit", ResourceLocation("pride/menu/exit.png"))
    }

    enum class ExtendedBackgroundButton(val buttonName: String, val texture: ResourceLocation) {
        Back("Back", ResourceLocation("pride/clickgui/back.png")),
        Enabled("Enabled", ResourceLocation("pride/notification/new/checkmark.png")),
        Particles("Particles", ResourceLocation("pride/clickgui/brush.png")),
        Change("Change wallpaper", ResourceLocation("pride/clickgui/import.png")),
        Reset("Reset wallpaper", ResourceLocation("pride/clickgui/reload.png")),
        Exit("Exit", ResourceLocation("pride/menu/exit.png"))
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {}
}