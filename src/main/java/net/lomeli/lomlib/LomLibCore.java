package net.lomeli.lomlib;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.common.MinecraftForge;

import net.lomeli.lomlib.client.CommandLomLib;
import net.lomeli.lomlib.client.ProxyClient.IconRegisterEvent;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.LogHelper;
import net.lomeli.lomlib.util.XMLConfiguration;
import net.lomeli.lomlib.util.XMLConfiguration.ConfigEnum;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class LomLibCore extends DummyModContainer {

    public LomLibCore() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = Strings.MOD_ID + "Core";
        meta.name = Strings.MOD_NAME + ":Core";
        meta.version = Strings.VERSION;
        meta.authorList.add("Lomeli12");
        meta.url = "http://www.anthony-lomeli.net/";
        meta.description = "Shared library mod required by several of Lomeli's Mods. LomLib:Core handles ASM and server commands";
    }

    public static LogHelper logger;

    public static boolean debug, capes, optiFailSafe;

    @Override
    public List<ArtifactVersion> getDependants() {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("Equivalency"));
        deps.add(VersionParser.parseVersionReference("MagicThings"));
        deps.add(VersionParser.parseVersionReference("ElementalCreepers"));
        return deps;
    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(Strings.MOD_NAME);

        configureMod(event.getSuggestedConfigurationFile());

        if (event.getSide().isClient()) {
            IconRegisterEvent iconR = new IconRegisterEvent();
            MinecraftForge.EVENT_BUS.register(iconR);
            FMLCommonHandler.instance().bus().register(iconR);
        }
    }

    public void configureMod(File configFile) {
        XMLConfiguration config = new XMLConfiguration(configFile);

        config.loadXml();

        debug = config.getBoolean("debugMode", false, Strings.DEBUG_MODE, ConfigEnum.GENERAL_CONFIG);
        capes = config.getBoolean("capes", true, Strings.CAPES, ConfigEnum.GENERAL_CONFIG);

        config.saveXML();
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return VersionParser.parseRange(Strings.MC_VERSION);
    }

}