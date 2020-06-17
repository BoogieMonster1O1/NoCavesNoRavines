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

    public Screen getScreen(Screen a){
        main.addEntry(entryBuilder.startBooleanToggle("config.nocavesnoravines.caves",disableCaves)
                .setDefaultValue(true)
                .setSaveConsumer((newVal)->disableCaves=newVal)
                .build());
        return null;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::getScreen;
    }
}
