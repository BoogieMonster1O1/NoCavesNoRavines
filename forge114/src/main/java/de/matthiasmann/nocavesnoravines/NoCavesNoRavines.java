package de.matthiasmann.nocavesnoravines;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.CanyonWorldCarver;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.carver.UnderwaterCanyonWorldCarver;
import net.minecraft.world.gen.carver.UnderwaterCaveWorldCarver;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static de.matthiasmann.nocavesnoravines.NoCavesNoRavines.CommonConfiguration.COMMON;

@Mod("nocavesnoravines")
public class NoCavesNoRavines {
    public static final String MODID = "nocavesnoravines";
    public static final Logger LOGGER = LogManager.getLogger(NoCavesNoRavines.class);

    public NoCavesNoRavines() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(this::modifyBiomes);
    }

    private void modifyBiomes() {
        CommonConfiguration config = new CommonConfiguration(new ForgeConfigSpec.Builder());

        ForgeRegistries.BIOMES.forEach(b -> {
            if (b.getCategory() == Biome.Category.NETHER || b.getCategory() == Biome.Category.THEEND || b.getCategory() == Biome.Category.NONE)
                return;
            final BlockState LAVA = Blocks.LAVA.getDefaultState();
            final BlockState WATER = Blocks.WATER.getDefaultState();

            for (GenerationStage.Carving stage : GenerationStage.Carving.values()) {
                b.getCarvers(stage).removeIf(carver ->
                        (config.removeRavines.get() && carver.carver instanceof CanyonWorldCarver) ||
                                (config.removeCaves.get() && carver.carver instanceof CaveWorldCarver) ||
                                (config.removeUnderwaterCaves.get() && carver.carver instanceof UnderwaterCanyonWorldCarver) ||
                                (config.removeUnderwaterRavines.get() && carver.carver instanceof UnderwaterCaveWorldCarver));
            }

            for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                b.getFeatures(stage).removeIf(maybe_decorated -> {
                    ConfiguredFeature<?> feature = maybe_decorated;
                    if (feature.config instanceof DecoratedFeatureConfig) {
                        DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.config;
                        feature = decorated.feature;
                    }
                    LOGGER.debug("Found feature {} with config {}", feature.feature.getClass(), feature.config.getClass());
                    if (feature.feature instanceof LakesFeature && feature.config instanceof LakesConfig) {
                        LakesConfig lakesConfig = (LakesConfig) feature.config;
                        LOGGER.debug("Found lake with block {}", lakesConfig.state.getBlock().getTranslationKey());
                        return (config.removeLakes.get() && lakesConfig.state == WATER) ||
                                (config.removeLavaLakes.get() && lakesConfig.state == LAVA);
                    }
                    if (feature.feature instanceof SpringFeature && feature.config instanceof LiquidsConfig) {
                        LiquidsConfig liquidsConfig = (LiquidsConfig) feature.config;
                        LOGGER.debug("Found spring with fluid {}", liquidsConfig.state.getFluid().getRegistryName());
                        return (config.removeSprings.get() && liquidsConfig.state.getFluid().isEquivalentTo(Fluids.WATER)) ||
                                (config.removeLavaSprings.get() && liquidsConfig.state.getFluid().isEquivalentTo(Fluids.LAVA));
                    }
                    return false;
                });
            }
        });
    }

    public static class CommonConfiguration {
        public final ForgeConfigSpec.BooleanValue removeUnderwaterCaves;
        public final ForgeConfigSpec.BooleanValue removeUnderwaterRavines;
        public final ForgeConfigSpec.BooleanValue removeCaves;
        public final ForgeConfigSpec.BooleanValue removeRavines;
        public final ForgeConfigSpec.BooleanValue removeLakes;
        public final ForgeConfigSpec.BooleanValue removeLavaLakes;
        public final ForgeConfigSpec.BooleanValue removeSprings;
        public final ForgeConfigSpec.BooleanValue removeLavaSprings;

        public static final ForgeConfigSpec COMMON;

        CommonConfiguration(final ForgeConfigSpec.Builder builder) {
            builder.push("carvers");
            removeUnderwaterCaves = builder.define("removeUnderwaterCaves", true);
            removeUnderwaterRavines = builder.define("removeUnderwaterRavines", true);
            removeCaves = builder.define("removeCaves", true);
            removeRavines = builder.define("removeRavines", true);
            builder.pop();

            builder.push("features");
            removeLakes = builder.define("removeLakes", false);
            removeLavaLakes = builder.define("removeLavaLakes", false);
            removeSprings = builder.define("removeSprings", false);
            removeLavaSprings = builder.define("removeLavaSprings", false);
            builder.pop();
        }

        static{
            final Pair<CommonConfiguration, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfiguration::new);
            COMMON = specPair.getRight();
        }
    }
}

