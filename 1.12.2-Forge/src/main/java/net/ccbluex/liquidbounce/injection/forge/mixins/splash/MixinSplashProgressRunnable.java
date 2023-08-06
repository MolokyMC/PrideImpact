package net.ccbluex.liquidbounce.injection.forge.mixins.splash;


import net.ccbluex.liquidbounce.ui.cnfont.FontDrawer;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.RandomImgUtils;
import net.ccbluex.liquidbounce.utils.render.AnimatedValue;
import net.ccbluex.liquidbounce.utils.render.EaseUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.ProgressManager;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Objects;

import static net.ccbluex.liquidbounce.ui.cnfont.FontLoaders.getFont;

@Mixin(targets="net.minecraftforge.fml.client.SplashProgress$2", remap=false)
public abstract class MixinSplashProgressRunnable {

    @Shadow(remap = false)
    protected abstract void setGL();

    @Shadow(remap = false)
    protected abstract void clearGL();

    @Inject(method="run()V", at=@At(value="HEAD"), remap=false, cancellable=true)
    private void run(CallbackInfo callbackInfo) {
        callbackInfo.cancel();

        this.setGL();
        GL11.glClearColor(1f, 1f, 1f, 1f);

//        ClientUtils.INSTANCE.logInfo("[Splash] Loading Texture...");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int tex;
        try {
            tex = RenderUtils.loadGlTexture(ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/" + RandomImgUtils.getBackGroundPath()))));
        } catch (IOException e) {
            tex = 0;
        }
        GL11.glDisable(3553);
        AnimatedValue animatedValue = new AnimatedValue();

        animatedValue.setType(EaseUtils.EnumEasingType.CIRC);
        animatedValue.setDuration(600L);

        for (; !SplashProgress.done; Display.sync(60)) {
            GL11.glClear(16384);
            int width = Display.getWidth();
            int height = Display.getHeight();

            GL11.glViewport(0, 0, width, height);
            GL11.glMatrixMode(5889);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, width, height, 0.0D, -1.0D, 1.0D);
            GL11.glMatrixMode(5888);
            GL11.glLoadIdentity();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3553);
            GL11.glBindTexture(3553, tex);
            GL11.glBegin(7);
            GL11.glTexCoord2f(0.0F, 0.0F);
            GL11.glVertex2f(0.0F, 0.0F);
            GL11.glTexCoord2f(1.0F, 0.0F);
            GL11.glVertex2f((float) width, 0.0F);
            GL11.glTexCoord2f(1.0F, 1.0F);
            GL11.glVertex2f((float) width, (float) height);
            GL11.glTexCoord2f(0.0F, 1.0F);
            GL11.glVertex2f(0.0F, (float) height);
            GL11.glEnd();
            GL11.glDisable(3553);
            float rectX = (float) width * 0.2F;
            float rectX2 = (float) width * 0.8F;
            float progress = (float) animatedValue.sync(getProgress());

            if (progress != 1.0F) {
                GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.3F);
                RenderUtils.drawRoundedCornerRect(0, (float) height, width, (float) (height - 10), 0.0F, (new Color(49, 51, 53, 150)).getRGB());
            }

            if (progress != 0.0F) {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderUtils.drawRoundedCornerRect(0, (float) height, width * progress, (float) (height - 10), 0.0F, (new Color(255, 255, 255, 170)).getRGB());
            }

            DecimalFormat decimalFormat = new DecimalFormat("#");
            String progress2 = decimalFormat.format(progress * 100.0F);

            FontDrawer font = getFont("misans", 40, true);

            font.drawString(progress2 + "%", 2, height - font.FONT_HEIGHT, Color.WHITE.getRGB());

//            Fonts.font30.drawString(progress2 + "%", (float) (width / 2 - Fonts.font30.getStringWidth(progress2 + "%") / 2), (float) (height / 2) + 40.0F, -1);
//            RenderUtils.drawImage4("pride/logo.png",(width / 2) - 70,(height / 2) - 150,140,140);

            SplashProgress.mutex.acquireUninterruptibly();
            Display.update();
            SplashProgress.mutex.release();
            if (SplashProgress.pause) {
                this.clearGL();
                this.setGL();
            }
        }

        GL11.glDeleteTextures(tex);
        this.clearGL();
    }

    private static float getProgress() {
        float progress = 0;
        Iterator<ProgressManager.ProgressBar> it = ProgressManager.barIterator();
        if (it.hasNext()) {
            ProgressManager.ProgressBar bar = it.next();
            progress = bar.getStep() / (float) bar.getSteps();
        }

        return progress;
    }
}