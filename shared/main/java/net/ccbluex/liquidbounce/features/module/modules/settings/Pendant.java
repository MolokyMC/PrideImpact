package net.ccbluex.liquidbounce.features.module.modules.settings;


import me.utils.render.VisualUtils;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render2DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.AnimationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;


@ModuleInfo(name = "Pendant", description = "flux", category = ModuleCategory.SETTINGS)
public class Pendant extends Module {


    public final ListValue Fubukistyle = new ListValue("Fubukistyle", new String[]{"GIF","Static"}, "GIF");
    public static BoolValue Taco = new BoolValue("Taco",false);
    public static BoolValue Fubuki = new BoolValue("Fubuki",false);
    public final FloatValue positionY = new FloatValue("PositionY", 5F, 130F, 1000F);
    public final FloatValue positionX = new FloatValue("PositionX", 5F, 130F, 1000F);
    public final FloatValue size = new FloatValue("size", 50F, 10F, 1000F);

    float posX = 0;

    @EventTarget
    public void onRender2D(Render2DEvent event) {

        if (Taco.getValue()){
            Taco();
        }

        if (Fubuki.getValue()){
            Fubuki();
        }

    }

    public void Fubuki(){

        if (Fubukistyle.get().contains("GIF")){
            int state = (mc.getThePlayer().getTicksExisted() % 16) + 1;
            RenderUtils.drawImage(classProvider.createResourceLocation("pride/fubuki/" + state + ".png"),positionX.getValue().intValue() , VisualUtils.height() - positionY.getValue().intValue(), size.getValue().intValue(), size.getValue().intValue());
        }

        if (Fubukistyle.get().contains("Static")){
            RenderUtils.drawImage(classProvider.createResourceLocation("pride/fubuki/Static.png"),positionX.getValue().intValue() , VisualUtils.height() - positionY.getValue().intValue(), 77 ,250);
        }
    }

    public void Taco(){
        if (posX < VisualUtils.width()) {
            posX += AnimationUtils.delta * 0.1;
        } else {
            posX = 0;
        }
        int state = (mc.getThePlayer().getTicksExisted() % 12) + 1;
        RenderUtils.drawImage(classProvider.createResourceLocation("pride/taco/" + state + ".png"), (int) posX, VisualUtils.height() - 80, 42, 27);
    }


}
