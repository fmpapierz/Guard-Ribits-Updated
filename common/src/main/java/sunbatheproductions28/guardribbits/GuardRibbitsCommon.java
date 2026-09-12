package sunbatheproductions28.guardribbits;

import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sunbatheproductions28.guardribbits.services.Services;

public class GuardRibbitsCommon {
    public static final String MOD_ID = "guardribbits";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // TODO - change this whenever updating to a new Minecraft version
    public static final String MC_VERSION_STRING = "26_2";

    public static boolean DEBUG_LOG = false;
    public static boolean DEBUG_RENDERING = false;

    /**
     * Called by each loader once its registries have been populated.
     */
    public static void init() {
        DEBUG_LOG = DEBUG_LOG && Services.PLATFORM.isDevelopmentEnvironment();
        DEBUG_RENDERING = DEBUG_RENDERING && Services.PLATFORM.isDevelopmentEnvironment();

        Services.MODULES.loadCommonModules();
        LOGGER.info("Initializing the Ribbit Guardians!");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
