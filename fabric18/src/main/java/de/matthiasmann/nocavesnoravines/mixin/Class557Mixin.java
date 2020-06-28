package de.matthiasmann.nocavesnoravines.mixin;

import net.minecraft.class_557;
import net.minecraft.class_562;
import net.minecraft.util.math.ColumnPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.Map;

@SuppressWarnings("all")
@Mixin(class_557.class)
public abstract class Class557Mixin {
    @Inject(method = "method_1509", at = @At(value="INVOKE",ordinal = 0,target = "Lnet/minecraft/class_557;method_1691(II)Lnet/minecraft/class_562;"))
    public void disableCaves(final int i, final int j){
        if(true){
            class_562 lv = this.method_1691(0, 0);
            this.field_2099.put(ColumnPos.method_198(i, j), lv);
            this.method_1687(i, j, lv);
        }
    }

    @Shadow
    private void method_1687(int i, int j, class_562 arg){}

    @Shadow
    public abstract class_562 method_1691(int i,int j);

    @Shadow
    public Map<Long, class_562> field_2099;
}
