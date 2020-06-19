package io.github.boogiemonster1o1.nocavesnoravines;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NoCavesNoRavines.MODID, name = NoCavesNoRavines.NAME, version = NoCavesNoRavines.VERSION)
public class NoCavesNoRavines{
    public static final String MODID = "nocavesnoravines";
    public static final String NAME = "NoCavesNoRavines";
    public static final String VERSION = "1.1.1";

    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    @Mod.EventBusSubscriber(modid=MODID)
    static class ModEventHandler{
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if(event.getModID().equals(MODID)){
                ConfigManager.sync(MODID,Config.Type.INSTANCE);
            }
        }

    }

    @Config(modid =MODID)
    @Config.LangKey("nocavesnoravines.config.title")
    static class CommonConfiguration{
        @Comment("Remove underwater caves")
        public static boolean removeUnderwaterCaves = true;

        @Comment("Remove underwater ravines")
        public static boolean removeUnderwaterRavines = true;

        @Comment("Remove caves")
        public static boolean removeCaves = true;

        @Comment("Remove ravines")
        public static boolean removeRavines = true;

        @Comment("Remove water lakes")
        public static boolean removeLakes = false;

        @Comment("Remove lava lakes")
        public static boolean removeLavaLakes = false;

        @Comment("Remove water springs")
        public static boolean removeSprings = false;

        @Comment("Remove lava springs")
        public static boolean removeLavaSprings = false;
    }
}
