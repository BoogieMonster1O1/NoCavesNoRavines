package io.github.boogiemonster1o1.nocavesnoravines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.*;
import net.minecraft.world.gen.feature.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.MinecraftStartListener;

public class NoCavesNoRavines implements MinecraftStartListener, BootstrapListener {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_NAME = "NoCavesNoRavines";


    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

    @Override
    public void afterVanillaBootstrap() {
        log(Level.WARN,"This version of NoCavesNoRavines is made for Rift 1.13.2, so errors may exist. Please report them to the github issue tracker.");
    }

    @Override
    public void onMinecraftStart() {
        log(Level.INFO, "Initializing");
        this.modifyBiomes();
    }

    public void modifyBiomes(){

        IRegistry.BIOME.forEach(b -> {
            if (b.getCategory() == Biome.Category.NETHER || b.getCategory() == Biome.Category.THEEND || b.getCategory() == Biome.Category.NONE)
                return;

            final IBlockState LAVA = Blocks.LAVA.getDefaultState();
            final IBlockState WATER = Blocks.WATER.getDefaultState();

            for(GenerationStage.Carving stage : GenerationStage.Carving.values()) {
                b.getCarvers(stage).removeIf(carvers ->
                        (((FieldAccess)carvers).getField() instanceof CanyonWorldCarver) ||
                                (((FieldAccess)carvers).getField() instanceof CaveWorldCarver) ||
                                (((FieldAccess)carvers).getField() instanceof UnderwaterCanyonWorldCarver) ||
                                (((FieldAccess)carvers).getField() instanceof UnderwaterCaveWorldCarver));
            }

            /*
            for(GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                b.getFeatures(stage).removeIf(maybe_decorated -> {
                    CompositeFeature<?,?> feature = maybe_decorated;
                    if(feature.config instanceof DecoratedFeatureConfig) {
                        DecoratedFeatureConfig decorated = (DecoratedFeatureConfig)feature.config;
                        //LOGGER.debug("Found decorated feature {} with decorator {}", decorated.feature.getClass(), decorated.decorator.getClass());
                        feature = decorated.feature;
                    }
                    LOGGER.debug("Found feature {} with config {}", feature.feature.getClass(), feature.config.getClass());
                    if(feature.feature instanceof LakesFeature && feature.config instanceof BlockStateFeatureConfig) {
                        BlockStateFeatureConfig config = (BlockStateFeatureConfig)feature.config;
                        LOGGER.debug("Found lake with block {}", config.state.getBlock().getTranslationKey());
                        return (config.state == WATER) ||
                                (config.state == LAVA);
                    }
                    if(((FieldAccess)feature).getField() instanceof SpringFeature && feature.config instanceof LiquidsConfig) {
                        LiquidsConfig config = (LiquidsConfig)feature.config;
                        LOGGER.debug("Found spring with fluid {}", config.state.getFluid().getRegistryName());
                        return (config.state.getFluid().isEquivalentTo(Fluids.WATER)) ||
                                (config.state.getFluid().isEquivalentTo(Fluids.LAVA));
                    }
                    return false;
                });
            }*/
        });
    }
}