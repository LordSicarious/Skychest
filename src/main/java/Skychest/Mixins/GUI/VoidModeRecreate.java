package Skychest.Mixins.GUI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Dynamic;

import Skychest.VoidMode;

@Environment(EnvType.CLIENT)
@Mixin(WorldListWidget.WorldEntry.class)
public abstract class VoidModeRecreate {

    // Outer Class instance field inside Inner Class cannot be found at compile time, only at run time.
    @SuppressWarnings("target")
    @Shadow
    WorldListWidget field_19135;

    @ModifyExpressionValue(method = "recreate", at = @At(value = "INVOKE",
    target = "net/minecraft/client/gui/screen/world/CreateWorldScreen.create(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/client/world/GeneratorOptionsHolder;Ljava/nio/file/Path;)Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;"))
    private CreateWorldScreen recreateWorld(CreateWorldScreen original, @Local LevelStorage.Session session) {
        try {
            //Loads the void mode of the world being recreated
            Dynamic<?> dynamic = session.readLevelProperties();
            original.getWorldCreator().setVoidMode(VoidMode.values()[dynamic.get("VoidMode").asInt(0)]);
        } catch (IOException e) {
            // If something went wrong, just assume default
            original.getWorldCreator().setVoidMode(VoidMode.DEFAULT);
        }
        return original;
    }

}