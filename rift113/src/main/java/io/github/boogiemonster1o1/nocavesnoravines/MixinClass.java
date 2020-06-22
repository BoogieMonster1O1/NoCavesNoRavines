package io.github.boogiemonster1o1.nocavesnoravines;

import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class MixinClass implements InitializationListener {
    @Override
    public void onInitialization() {
        MixinBootstrap.init();
        Mixins.addConfiguration("nocavesnoravines.mixins.json");
    }
}
