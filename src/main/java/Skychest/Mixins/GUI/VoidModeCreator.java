package Skychest.Mixins.GUI;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.gen.WorldPreset;

import java.nio.file.Path;
import java.util.Optional;
import java.util.OptionalLong;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import Skychest.VoidMode;
import Skychest.VoidModeInterface;

@Environment(EnvType.CLIENT)
@Mixin(WorldCreator.class)
public abstract class VoidModeCreator implements VoidModeInterface {
    private VoidMode voidMode;

    @Shadow
    public void update() {}
    
    @Inject(at = @At("RETURN"), method = "<init>(Ljava/nio/file/Path;Lnet/minecraft/client/world/GeneratorOptionsHolder;Ljava/util/Optional;Ljava/util/OptionalLong;)V")
    private void init(Path savesDirectory, GeneratorOptionsHolder generatorOptionsHolder, Optional<RegistryKey<WorldPreset>> defaultWorldType, OptionalLong seed, CallbackInfo ci) {
        voidMode = VoidMode.DEFAULT;
    }

    @Override
    public VoidMode getVoidMode() {
        return (voidMode != null) ? voidMode : VoidModeInterface.super.getVoidMode();
    }
    @Override
    public void setVoidMode(VoidMode mode) { voidMode = mode; update(); }

}