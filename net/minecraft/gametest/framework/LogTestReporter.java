package net.minecraft.gametest.framework;

import org.apache.logging.log4j.LogManager;
import net.minecraft.Util;
import org.apache.logging.log4j.Logger;

public class LogTestReporter implements TestReporter {
    private static final Logger LOGGER;
    
    public void onTestFailed(final GameTestInfo lf) {
        if (lf.isRequired()) {
            LogTestReporter.LOGGER.error(lf.getTestName() + " failed! " + Util.describeError(lf.getError()));
        }
        else {
            LogTestReporter.LOGGER.warn("(optional) " + lf.getTestName() + " failed. " + Util.describeError(lf.getError()));
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
