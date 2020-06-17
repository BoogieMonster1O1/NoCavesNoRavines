package io.github.boogiemonster1o1.nocavesnoravines;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class Config implements ModMenuApi {

    @Override
    public String getModId() {
        return NoCavesNoRavines.MODID;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen(ModConfig.class,screen));
    }


    @me.sargunvohra.mcmods.autoconfig1u.annotation.Config(name = "nocavesnoravines_config")
    public static class ModConfig implements ConfigData {

        public boolean disableCaves = true;
        public boolean disableRavines = true;
        public boolean disableUnderwaterCaves = true;
        public boolean disableUnderwaterRavines = true;
        public boolean disableWaterLakes = false;
        public boolean disableLavaLakes = false;
        public boolean disableWaterSprings = false;
        public boolean disableLavaSprings = false;
    }
}
