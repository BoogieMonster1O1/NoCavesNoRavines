package io.github.boogiemonster1o1.nocavesnoravines;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class Config implements ModMenuApi {
    ConfigBuilder builder = ConfigBuilder.create().setParentScreen(MinecraftClient.getInstance().currentScreen).setTitle("config.nocavesnoravines.title");
    ConfigCategory main = builder.getOrCreateCategory("config.nocavesnoravines.category");
    ConfigEntryBuilder entryBuilder = builder.entryBuilder();

    @Override
    public String getModId() {
        return NoCavesNoRavines.MODID;
    }

    public static boolean disableCaves = true;
    public static boolean disableRavines = true;
    public static boolean disableUnderwaterCaves = true;
    public static boolean disableUnderwaterRavines = true;
    public static boolean disableWaterLakes = true;
    public static boolean disableLavaLakes = true;

    public Screen getScreen(Screen a){
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.caves",disableCaves)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableCaves=newVal)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.ravines",disableRavines)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableRavines=newVal)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.underwater_caves",disableUnderwaterCaves)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableUnderwaterCaves=newVal)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.underwater_ravines",disableUnderwaterRavines)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableUnderwaterRavines=newVal)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.water_lakes",disableWaterLakes)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableWaterLakes=newVal)
                .build());
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.lava_lakes",disableLavaLakes)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableLavaLakes=newVal)
                .build());
        return builder.build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::getScreen;
    }
}
