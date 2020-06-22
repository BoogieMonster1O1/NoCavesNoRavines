package io.github.boogiemonster1o1.nocavesnoravines;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.CaveCarver;
import net.minecraft.world.gen.carver.RavineCarver;
import net.minecraft.world.gen.carver.UnderwaterCaveCarver;
import net.minecraft.world.gen.carver.UnderwaterRavineCarver;
import net.minecraft.world.gen.feature.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoCavesNoRavines implements ModInitializer, ClientModInitializer {

    public static final String MODID = "nocavesnoravines";
    public static final Logger LOGGER = LogManager.getLogger(NoCavesNoRavines.class);

    @Override
    public void onInitialize() {
        AutoConfig.register(Config.ModConfig.class, GsonConfigSerializer::new);

        LOGGER.info("Starting NoCavesNoRavines...");
        Registry.BIOME.forEach(this::modifyBiomes);
        RegistryEntryAddedCallback.event(Registry.BIOME).register(this::modifyBiomes);
    }

    public void modifyBiomes(Biome biome) {
        Config.ModConfig modConfig = AutoConfig.getConfigHolder(Config.ModConfig.class).getConfig();
        if (biome.getCategory() == Biome.Category.NETHER || biome.getCategory() == Biome.Category.THEEND || biome.getCategory() == Biome.Category.NONE)
            return;

        final BlockState LAVA = Blocks.LAVA.getDefaultState();
        final BlockState WATER = Blocks.WATER.getDefaultState();

        for (GenerationStep.Carver stage : GenerationStep.Carver.values()) {
            biome.getCarversForStep(stage).removeIf(carver ->
                    (modConfig.disableRavines && carver.carver instanceof RavineCarver) || (modConfig.disableCaves && carver.carver instanceof CaveCarver) || (modConfig.disableUnderwaterRavines && carver.carver instanceof UnderwaterRavineCarver) || (modConfig.disableUnderwaterCaves && carver.carver instanceof UnderwaterCaveCarver));
        }

        for (GenerationStep.Feature stage : GenerationStep.Feature.values()) {
            biome.getFeaturesForStep(stage).removeIf(maybe_decorated -> {
                ConfiguredFeature<?, ?> feature = maybe_decorated;
                if (feature.config instanceof DecoratedFeatureConfig) {
                    DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.config;
                    //LOGGER.debug("Found decorated feature {} with decorator {}", decorated.feature.getClass(), decorated.decorator.getClass());
                    feature = decorated.feature;
                }
                LOGGER.debug("Found feature {} with config {}", feature.feature.getClass(), feature.config.getClass());
                if (feature.feature instanceof LakeFeature && feature.config instanceof SingleStateFeatureConfig) {
                    SingleStateFeatureConfig config = (SingleStateFeatureConfig) feature.config;
                    LOGGER.debug("Found lake with block {}", config.state.getBlock().getTranslationKey());
                    return (modConfig.disableWaterLakes && config.state == WATER) ||
                            (modConfig.disableLavaLakes && config.state == LAVA);
                }
                if (feature.feature instanceof SpringFeature && feature.config instanceof SpringFeatureConfig) {
                    SpringFeatureConfig config = (SpringFeatureConfig) feature.config;
                    LOGGER.debug("Found spring with fluid {}", config.state.getFluid().toString());
                    return (modConfig.disableWaterSprings && config.state.getFluid().matchesType(Fluids.WATER)) ||
                            (modConfig.disableLavaSprings && config.state.getFluid().matchesType(Fluids.LAVA));
                }
                return false;
            });
        }
    }

    public void modifyBiomes(int i, Identifier identifier, Biome biome) {
        this.modifyBiomes(biome);
    }

    @Override
    public void onInitializeClient() {
    }
}
