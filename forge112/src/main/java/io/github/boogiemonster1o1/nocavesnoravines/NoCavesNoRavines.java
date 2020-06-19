package io.github.boogiemonster1o1.nocavesnoravines;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NoCavesNoRavines.MODID, name = NoCavesNoRavines.NAME, version = NoCavesNoRavines.VERSION)
public class NoCavesNoRavines{
    public static final String MODID = "nocavesnoravines";
    public static final String NAME = "NoCavesNoRavines";
    public static final String VERSION = "1.1.1";

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}
