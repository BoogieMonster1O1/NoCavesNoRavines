package io.github.boogiemonster1o1.nocavesnoravines;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dimdev.rift.listener.BootstrapListener;
import org.dimdev.rift.listener.MinecraftStartListener;

public class NoCavesNoRavines implements MinecraftStartListener, BootstrapListener {

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "nocooldown";
    public static final String MOD_NAME = "No PVP Cooldown";


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
    }
}