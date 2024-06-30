package Skychest.Mixins.GUI;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;

import Skychest.VoidMode;
import Skychest.VoidModeInterface;

@Mixin(LevelProperties.class)
public abstract class VoidModeProperties implements VoidModeInterface {
    private VoidMode voidMode;

    // Retrieve Void Mode from level Nbt
    @SuppressWarnings("deprecation")
    @Inject(at = @At("RETURN"), method = "readProperties")
    private static <T> void readProperties(Dynamic<T> dynamic, LevelInfo info, LevelProperties.SpecialProperty specialProperty, GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> cir) {
        cir.getReturnValue().setVoidMode(VoidMode.values()[dynamic.get("VoidMode").asInt(0)]);
    }

    // Store Void Mode in level Nbt
    @Inject(at = @At("TAIL"), method = "updateProperties")
    private void updateProperties(DynamicRegistryManager registryManager, NbtCompound levelNbt, @Nullable NbtCompound playerNbt, CallbackInfo ci) {
        if (!voidMode.isDefault()) {
            levelNbt.putInt("VoidMode",getVoidMode().ordinal());
        }
    }
    
    @Override
    public VoidMode getVoidMode() {
        return voidMode;
    }
    @Override
    public void setVoidMode(VoidMode mode) { voidMode = mode; }
}