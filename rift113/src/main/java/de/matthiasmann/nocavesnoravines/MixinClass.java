package de.matthiasmann.nocavesnoravines;

import me.shedaniel.api.ConfigRegistry;
import org.apache.logging.log4j.Level;
import org.dimdev.riftloader.listener.InitializationListener;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

public class MixinClass implements InitializationListener {
    @Override
    public void onInitialization() {
        MixinBootstrap.init();
        Mixins.addConfiguration("nocavesnoravines.mixins.json");
        try {
            Class.forName("me.shedaniel.api.ConfigRegistry");
            ConfigRegistry.registerConfig(NoCavesNoRavines.MOD_ID, () -> {
                NoCavesNoRavines.log(Level.INFO, "Opening Config");
            });
        } catch (ClassNotFoundException e) {
            NoCavesNoRavines.log(Level.INFO, "Modlist is not present, not registering config");
            NoCavesNoRavines.log(Level.INFO, "You should really consider getting Modlist :)");
        }
    }
}
