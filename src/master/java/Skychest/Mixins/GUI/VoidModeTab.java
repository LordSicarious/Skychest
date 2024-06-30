package Skychest.Mixins.GUI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.GridWidget.Adder;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

import Skychest.VoidMode;
import Skychest.Mixins.Access.ScreenData;


@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.WorldTab.class)
public abstract class VoidModeTab {

    private static final Text VOID_MODE_TEXT = Text.literal("Void Mode");

    // Outer Class instance field inside Inner Class cannot be found at compile time, only at run time.
    @SuppressWarnings("target")
    @Shadow
    private CreateWorldScreen field_42182;
    
    // Inner Class constructor method cannot be found at compile time, only at run time.
    @SuppressWarnings("target")
    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;)V")
    private void init(CreateWorldScreen createWorldScreen, CallbackInfo ci,
                        @Local Adder adder) {
        // Need the creator field from the outer class, since that's where the mode is actually stored
        WorldCreator creator = field_42182.getWorldCreator();
        adder.add(new TextWidget(VOID_MODE_TEXT, ((ScreenData)field_42182).getTextRenderer()).alignLeft());
        CyclingButtonWidget<VoidMode> cyclingButtonWidget = adder.add(
				CyclingButtonWidget.<VoidMode>builder(mode -> mode.toText()).omitKeyText().values(VoidMode.values())
				.build(0, 0, 150, 20, VOID_MODE_TEXT, (button, mode) -> creator.setVoidMode(mode)),
                // The margin better centres the buttom relative to the text
                adder.copyPositioner().alignRight().marginY(-5)
		);
        cyclingButtonWidget.setValue(creator.getVoidMode());
        creator.addListener(c -> { VoidMode mode = c.getVoidMode(); 
            cyclingButtonWidget.setValue(mode); });
    }

}