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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Environment(EnvType.CLIENT)
public class Config implements ModMenuApi {

    @Override
    public String getModId() {
        return NoCavesNoRavines.MODID;
    }

    public static boolean getBool(int line){
        if(configFile.exists()) return false;
        else {
            try {
                boolean val = Boolean.parseBoolean(Files.readAllLines(Paths.get(configFile.getPath())).get(line));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private static final File configFile = new File(MinecraftClient.getInstance().runDirectory.getPath() + "." + File.separator + "config" + File.separator + "nocavesnoravines.txt");

    public static boolean disableCaves = getBool(1);
    public static boolean disableRavines = getBool(2);
    public static boolean disableUnderwaterCaves = getBool(3);
    public static boolean disableUnderwaterRavines = getBool(4);
    public static boolean disableWaterLakes = getBool(5);
    public static boolean disableLavaLakes = getBool(6);

    public Screen getScreen(Screen a){
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(MinecraftClient.getInstance().currentScreen).setTitle("config.nocavesnoravines.title").setShouldListSmoothScroll(false);
        ConfigCategory main = builder.getOrCreateCategory("config.nocavesnoravines.category");
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

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
        builder.setSavingRunnable(() -> {
        });
        return builder.build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::getScreen;
    }
}
