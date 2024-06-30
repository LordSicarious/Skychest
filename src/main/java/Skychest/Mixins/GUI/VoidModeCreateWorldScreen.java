package Skychest.Mixins.GUI;

import com.mojang.serialization.Lifecycle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.registry.CombinedDynamicRegistries;
import net.minecraft.registry.ServerDynamicRegistryType;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.LevelProperties;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;

@Environment(EnvType.CLIENT)
@Mixin(CreateWorldScreen.class)
public abstract class VoidModeCreateWorldScreen {

    @Shadow
    private WorldCreator worldCreator;

    @SuppressWarnings("deprecation")
    @Inject(method = "startServer", at = @At(value = "INVOKE", shift = Shift.BY, by = 2, 
            target = "net/minecraft/world/level/LevelProperties.<init>(Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/world/gen/GeneratorOptions;Lnet/minecraft/world/level/LevelProperties$SpecialProperty;Lcom/mojang/serialization/Lifecycle;)V"))
    private void startServer(LevelProperties.SpecialProperty specialProperty,
                                CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries,
                                Lifecycle lifecycle,
                                CallbackInfo ci,
                                @Local SaveProperties saveProperties) {
        ((LevelProperties)saveProperties).setVoidMode(worldCreator.getVoidMode());
    }  
}