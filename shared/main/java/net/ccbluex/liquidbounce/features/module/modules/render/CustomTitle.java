package net.ccbluex.liquidbounce.features.module.modules.render;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.TextValue;
import org.lwjgl.opengl.Display;

/**
 * 我他妈是自写的，你别几把Skid搞事情行不行
 * Date:2023/1/17
 */


@ModuleInfo(name="CustomTitle",description = "自定义窗口标题",category= ModuleCategory.RENDER)
public class CustomTitle extends Module {
    private final TextValue Title = new TextValue("Title","PrideImpact 1.12.2");
    public CustomTitle() {


    }
    private int S;
    private int HM;
    private int M;
    private int H;

    public final int getS() {
        return this.S;
    }


    public final int getM() {
        return this.M;
    }

    public final int getH() {
        return this.H;
    }
    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        Display.setTitle((String)this.Title.get());
    }

}
