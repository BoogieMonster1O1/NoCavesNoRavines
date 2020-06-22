package io.github.boogiemonster1o1.nocavesnoravines.mixins;

import io.github.boogiemonster1o1.nocavesnoravines.WorldCarverWrapperAccess;
import net.minecraft.world.gen.carver.IWorldCarver;
import net.minecraft.world.gen.carver.WorldCarverWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldCarverWrapper.class)
public class WorldCarverWrapperMixin implements WorldCarverWrapperAccess {

    @Shadow @Final private IWorldCarver<?> carver;

    @Override
    public IWorldCarver<?> getCarver() {
        return this.carver;
    }
}
