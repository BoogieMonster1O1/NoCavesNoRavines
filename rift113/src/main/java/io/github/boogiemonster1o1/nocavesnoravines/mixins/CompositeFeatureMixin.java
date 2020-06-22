package io.github.boogiemonster1o1.nocavesnoravines.mixins;

import io.github.boogiemonster1o1.nocavesnoravines.FieldAccess;
import net.minecraft.world.gen.feature.CompositeFeature;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CompositeFeature.class)
public class CompositeFeatureMixin implements FieldAccess {
    @Shadow @Final protected Feature<?> feature;

    @Override
    public Feature<?> getField() {
        return this.feature;
    }
}
