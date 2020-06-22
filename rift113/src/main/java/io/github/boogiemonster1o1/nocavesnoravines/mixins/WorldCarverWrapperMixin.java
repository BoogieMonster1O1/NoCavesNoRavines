package io.github.boogiemonster1o1.nocavesnoravines.mixins;

import io.github.boogiemonster1o1.nocavesnoravines.FieldAccess;
import net.minecraft.world.gen.carver.IWorldCarver;
import net.minecraft.world.gen.carver.WorldCarverWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldCarverWrapper.class)
public class WorldCarverWrapperMixin implements FieldAccess {

    @Shadow
    @Final
    private IWorldCarver<?> carver;

    @Override
    public IWorldCarver<?> getField() {
        return this.carver;
    }
}
