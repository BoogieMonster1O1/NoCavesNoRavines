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
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

import static de.matthiasmann.nocavesnoravines.NoCavesNoRavines.CommonConfiguration.*;

@Mod("nocavesnoravines")
public class NoCavesNoRavines {
    public static final String MODID = "nocavesnoravines";
    public static final Logger LOGGER = LogManager.getLogger(NoCavesNoRavines.class);

    public NoCavesNoRavines() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);

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
                        (REMOVE_RAVINES && carver.carver instanceof CanyonWorldCarver) ||
                                (REMOVE_CAVES && carver.carver instanceof CaveWorldCarver) ||
                                (REMOVE_UNDERWATER_CAVES && carver.carver instanceof UnderwaterCanyonWorldCarver) ||
                                (REMOVE_UNDERWATER_RAVINES && carver.carver instanceof UnderwaterCaveWorldCarver));
            }

            for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                b.getFeatures(stage).removeIf(maybe_decorated -> {
                    ConfiguredFeature<?,?> feature = maybe_decorated;
                    if (feature.config instanceof DecoratedFeatureConfig) {
                        DecoratedFeatureConfig decorated = (DecoratedFeatureConfig) feature.config;
                        feature = decorated.feature;
                    }
                    LOGGER.debug("Found feature {} with config {}", feature.feature.getClass(), feature.config.getClass());
                    if (feature.feature instanceof LakesFeature && feature.config instanceof BlockStateFeatureConfig) {
                        BlockStateFeatureConfig lakesConfig = (BlockStateFeatureConfig) feature.config;
                        LOGGER.debug("Found lake with block {}", lakesConfig.state.getBlock().getTranslationKey());
                        return (REMOVE_LAKES && lakesConfig.state == WATER) ||
                                (REMOVE_LAVA_LAKES && lakesConfig.state == LAVA);
                    }
                    if (feature.feature instanceof SpringFeature && feature.config instanceof LiquidsConfig) {
                        LiquidsConfig liquidsConfig = (LiquidsConfig) feature.config;
                        LOGGER.debug("Found spring with fluid {}", liquidsConfig.state.getFluid().getRegistryName());
                        return (REMOVE_SPRINGS && liquidsConfig.state.getFluid().isEquivalentTo(Fluids.WATER)) ||
                                (REMOVE_LAVA_SPRINGS && liquidsConfig.state.getFluid().isEquivalentTo(Fluids.LAVA));
                    }
                    return false;
                });
            }
        });
    }

    @Mod.EventBusSubscriber(modid = NoCavesNoRavines.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonConfiguration {
        public final ForgeConfigSpec.BooleanValue removeUnderwaterCaves;
        public final ForgeConfigSpec.BooleanValue removeUnderwaterRavines;
        public final ForgeConfigSpec.BooleanValue removeCaves;
        public final ForgeConfigSpec.BooleanValue removeRavines;
        public final ForgeConfigSpec.BooleanValue removeLakes;
        public final ForgeConfigSpec.BooleanValue removeLavaLakes;
        public final ForgeConfigSpec.BooleanValue removeSprings;
        public final ForgeConfigSpec.BooleanValue removeLavaSprings;

        public static final ForgeConfigSpec COMMON_SPEC;
        public static final CommonConfiguration COMMON;

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
            COMMON_SPEC = specPair.getRight();
            COMMON = specPair.getLeft();
        }

        public static boolean REMOVE_UNDERWATER_CAVES;
        public static boolean REMOVE_UNDERWATER_RAVINES;
        public static boolean REMOVE_CAVES;
        public static boolean REMOVE_RAVINES;
        public static boolean REMOVE_LAKES;
        public static boolean REMOVE_LAVA_LAKES;
        public static boolean REMOVE_SPRINGS;
        public static boolean REMOVE_LAVA_SPRINGS;

        public static void bakeConfig() {
            REMOVE_UNDERWATER_CAVES = COMMON.removeUnderwaterCaves.get();
            REMOVE_UNDERWATER_RAVINES = COMMON.removeUnderwaterRavines.get();
            REMOVE_CAVES = COMMON.removeCaves.get();
            REMOVE_RAVINES = COMMON.removeRavines.get();
            REMOVE_LAKES = COMMON.removeLakes.get();
            REMOVE_LAVA_LAKES = COMMON.removeLavaLakes.get();
            REMOVE_SPRINGS = COMMON.removeSprings.get();
            REMOVE_LAVA_SPRINGS = COMMON.removeLavaSprings.get();
        }

        @SubscribeEvent
        public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
            if (configEvent.getConfig().getSpec() == CommonConfiguration.COMMON_SPEC) {
                CommonConfiguration.bakeConfig();
            }
        }

    }
}

