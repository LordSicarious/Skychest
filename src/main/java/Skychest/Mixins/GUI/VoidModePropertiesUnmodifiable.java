package Skychest.Mixins.GUI;

import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.UnmodifiableLevelProperties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import Skychest.VoidMode;
import Skychest.VoidModeInterface;

@Mixin(UnmodifiableLevelProperties.class)
public abstract class VoidModePropertiesUnmodifiable implements VoidModeInterface {

    @Shadow
    private SaveProperties saveProperties;

    @Override
    public VoidMode getVoidMode() {
        return ((VoidModeInterface)saveProperties).getVoidMode();
    }
}