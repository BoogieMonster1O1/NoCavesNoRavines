package io.github.boogiemonster1o1.nocavesnoravines;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
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
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static io.github.boogiemonster1o1.nocavesnoravines.NoCavesNoRavines.CommonConfiguration.COMMON;

@Mod("nocavesnoravines")
public class NoCavesNoRavines {
    public static final String MODID = "nocavesnoravines";
    public static final Logger LOGGER = LogManager.getLogger(NoCavesNoRavines.class);

    public NoCavesNoRavines() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON,COMMON,"nocavesnoravines-config.toml");

        CommonConfiguration.registerConfig(COMMON, FMLPaths.CONFIGDIR.get().resolve("nocavesnoravines-config.toml").toString());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // final Pair<CommonConfiguration, ForgeConfigSpec> com = new ForgeConfigSpec.Builder().configure(builder -> new CommonConfiguration());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(this::modifyBiomes);
//        this.modifyBiomes();
    }

    private void modifyBiomes() {
        ForgeRegistries.BIOMES.forEach(b -> {
            if (b.getCategory() == Biome.Category.NETHER || b.getCategory() == Biome.Category.THEEND || b.getCategory() == Biome.Category.NONE)
                return;
            final BlockState LAVA = Blocks.LAVA.getDefaultState();
            final BlockState WATER = Blocks.WATER.getDefaultState();

            for (GenerationStage.Carving stage : GenerationStage.Carving.values()) {
                b.getCarvers(stage).removeIf(carver ->
                        (CommonConfiguration.REMOVE_RAVINES.get() && carver.carver instanceof CanyonWorldCarver) ||
                                (CommonConfiguration.REMOVE_CAVES.get() && carver.carver instanceof CaveWorldCarver) ||
                                (CommonConfiguration.REMOVE_UNDERWATER_CAVES.get() && carver.carver instanceof UnderwaterCanyonWorldCarver) ||
                                (CommonConfiguration.REMOVE_UNDERWATER_RAVINES.get() && carver.carver instanceof UnderwaterCaveWorldCarver));
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
                        BlockStateFeatureConfig config = (BlockStateFeatureConfig) feature.config;
                        LOGGER.debug("Found lake with block {}", config.state.getBlock().getTranslationKey());
                        return (CommonConfiguration.REMOVE_LAKES.get() && config.state == WATER) ||
                                (CommonConfiguration.REMOVE_LAVA_LAKES.get() && config.state == LAVA);
                    }
                    if (feature.feature instanceof SpringFeature && feature.config instanceof LiquidsConfig) {
                        LiquidsConfig config = (LiquidsConfig) feature.config;
                        LOGGER.debug("Found spring with fluid {}", config.state.getFluid().getRegistryName());
                        return (CommonConfiguration.REMOVE_SPRINGS.get() && config.state.getFluid().isEquivalentTo(Fluids.WATER)) ||
                                (CommonConfiguration.REMOVE_LAVA_SPRINGS.get() && config.state.getFluid().isEquivalentTo(Fluids.LAVA));
                    }
                    return false;
                });
            }
        });
    }

    public static class CommonConfiguration {
        public static ForgeConfigSpec.BooleanValue REMOVE_UNDERWATER_CAVES;
        public static ForgeConfigSpec.BooleanValue REMOVE_UNDERWATER_RAVINES;
        public static ForgeConfigSpec.BooleanValue REMOVE_CAVES;
        public static ForgeConfigSpec.BooleanValue REMOVE_RAVINES;
        public static ForgeConfigSpec.BooleanValue REMOVE_LAKES;
        public static ForgeConfigSpec.BooleanValue REMOVE_LAVA_LAKES;
        public static ForgeConfigSpec.BooleanValue REMOVE_SPRINGS;
        public static ForgeConfigSpec.BooleanValue REMOVE_LAVA_SPRINGS;
        public static ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static ForgeConfigSpec COMMON = BUILDER.build();

        static {
            BUILDER.push("carvers");
            REMOVE_UNDERWATER_CAVES = BUILDER.define("removeUnderwaterCaves", true);
            REMOVE_UNDERWATER_RAVINES = BUILDER.define("removeUnderwaterRavines", true);
            REMOVE_CAVES = BUILDER.define("removeCaves", true);
            REMOVE_RAVINES = BUILDER.define("removeRavines", true);
            BUILDER.pop();

            BUILDER.push("features");
            REMOVE_LAKES = BUILDER.define("removeLakes", false);
            REMOVE_LAVA_LAKES = BUILDER.define("removeLavaLakes", false);
            REMOVE_SPRINGS = BUILDER.define("removeSprings", false);
            REMOVE_LAVA_SPRINGS = BUILDER.define("removeLavaSprings", false);
            BUILDER.pop();
        }

        public static void registerConfig(ForgeConfigSpec config, String path)
        {
            final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
            file.load();
            config.setConfig(file);
        }
    }

    /*@Mod.EventBusSubscriber(modid=MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    static class NoCavesNoRavinesEventHandler{
        @SubscribeEvent
        public static void onModConfigEvent(final ModConfig.ModConfigEvent event) {
            event.getConfig().ge
        }
    }*/
}

