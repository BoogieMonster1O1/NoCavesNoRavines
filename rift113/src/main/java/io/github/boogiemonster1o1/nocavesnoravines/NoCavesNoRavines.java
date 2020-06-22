package io.github.boogiemonster1o1.nocavesnoravines;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.CanyonWorldCarver;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.carver.UnderwaterCanyonWorldCarver;
import net.minecraft.world.gen.carver.UnderwaterCaveWorldCarver;
import net.minecraft.world.gen.feature.LakesFeature;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.MinecraftStartListener;

public class NoCavesNoRavines implements MinecraftStartListener, BootstrapListener {

    public static final String MOD_NAME = "NoCavesNoRavines";
    public static final String MOD_ID = "nocavesnoravines";
    public static Logger LOGGER = LogManager.getLogger();

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    @Override
    public void afterVanillaBootstrap() {
        log(Level.WARN, "This version of NoCavesNoRavines is made for Rift 1.13.2, so errors may exist. Please report them to the github issue tracker.");

        RegistryNamespaced.BIOME.forEach(b -> {
            if (b.getCategory() == Biome.Category.NETHER || b.getCategory() == Biome.Category.THEEND || b.getCategory() == Biome.Category.NONE)
                return;

            final IBlockState LAVA = Blocks.LAVA.getDefaultState();
            final IBlockState WATER = Blocks.WATER.getDefaultState();

            for (GenerationStage.Carving stage : GenerationStage.Carving.values()) {
                b.getCarvers(stage).removeIf(carvers -> (((FieldAccess) carvers).getField() instanceof CanyonWorldCarver) ||
                        (((FieldAccess) carvers).getField() instanceof CaveWorldCarver) ||
                        (((FieldAccess) carvers).getField() instanceof UnderwaterCanyonWorldCarver) ||
                        (((FieldAccess) carvers).getField() instanceof UnderwaterCaveWorldCarver));
            }

            for (GenerationStage.Decoration stage : GenerationStage.Decoration.values()) {
                b.getFeatures(stage).removeIf(maybe_decorated -> {
                    LOGGER.debug("Found feature {} with config", ((FieldAccess) maybe_decorated).getField().getClass());
                    return ((FieldAccess) maybe_decorated).getField() instanceof LakesFeature;
                });
            }
        });
    }

    @Override
    public void onMinecraftStart() {
        log(Level.INFO, "Initializing");
        this.modifyBiomes();
    }

    public void modifyBiomes() {

    }
}