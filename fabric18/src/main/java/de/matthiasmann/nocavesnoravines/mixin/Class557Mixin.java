package de.matthiasmann.nocavesnoravines.mixin;

import net.minecraft.class_557;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(class_557.class)
public class Class557Mixin {
    @Inject(method = "method_1509", at = @At(value="INVOKE",ordinal = 0,target = "Lnet/minecraft/class_557;method_1691(II)Lnet/minecraft/class_562;"))
    public void disableCaves(){

    }
}
