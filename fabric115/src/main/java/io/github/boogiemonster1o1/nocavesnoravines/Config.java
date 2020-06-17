package io.github.boogiemonster1o1.nocavesnoravines;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Config implements ModMenuApi {

    @Override
    public String getModId() {
        return NoCavesNoRavines.MODID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return null;
    }

    @me.sargunvohra.mcmods.autoconfig1u.annotation.Config(name = "nocavesnoravines_config")
    public static class ModConfig{
        boolean disableCaves;
        boolean disableRavines;
        boolean disableUnderwaterCaves;
        boolean disableUnderwaterRavines;
        boolean disableWaterLakes;
        boolean disableLavaLakes;
        boolean disableWaterSprings;
        boolean disableLavaSprings;
    }
}
