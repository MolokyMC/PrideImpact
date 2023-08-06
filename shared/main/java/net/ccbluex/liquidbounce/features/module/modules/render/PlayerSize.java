/*    */ package net.ccbluex.liquidbounce.features.module.modules.render;
/*    */
/*    */ import kotlin.Metadata;
/*    */ import net.ccbluex.liquidbounce.features.module.Module;
/*    */ import net.ccbluex.liquidbounce.features.module.ModuleCategory;
/*    */ import net.ccbluex.liquidbounce.features.module.ModuleInfo;
/*    */ import net.ccbluex.liquidbounce.value.FloatValue;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */
/*    */ @ModuleInfo(name = "PlayerSize", description = "Edit the player's size", category = ModuleCategory.RENDER)
/*    */ @Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\000\024\n\002\030\002\n\002\030\002\n\002\b\002\n\002\030\002\n\002\b\003\b\007\030\0002\0020\001B\005¢\006\002\020\002R\021\020\003\032\0020\004¢\006\b\n\000\032\004\b\005\020\006¨\006\007"}, d2 = {"Lnet/ccbluex/liquidbounce/features/module/modules/Render/PlayerSize;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "playerSizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "getPlayerSizeValue", "()Lnet/ccbluex/liquidbounce/value/FloatValue;", "LiquidSense"})
/*    */ public final class PlayerSize extends Module {
    /*    */   @NotNull
    /* 14 */   private final FloatValue playerSizeValue = new FloatValue("PlayerSize", 0.5F, 0.01F, 5.0F); @NotNull public final FloatValue getPlayerSizeValue() { return this.playerSizeValue; }
    /*    */
    /*    */ }


/* Location:              C:\Users\Sock\Documents\Tencent Files\1647054792\FileRecv\Vic.jar!\net\ccbluex\liquidbounce\features\module\modules\tomk\PlayerSize.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */