package de.matthiasmann.nocavesnoravines;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

public class Config implements ModMenuApi {

    @Override
    public String getModId() {
        return NoCavesNoRavines.MODID;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (ConfigScreenFactory<Screen>) screen -> AutoConfig.getConfigScreen(ModConfig.class, screen).get();
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
