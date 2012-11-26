package vn.ducquoc.jutil;

import java.util.logging.Logger;

/**
 * Helper class for Logging operations.
 * 
 * @author ducquoc
 * @see com.liferay.portal.kernel.log.LogUtil
 * @see net.osmand.LogUtil
 */
public class LogUtil {

    private static final int TEST_INIT = 60 * 1000;
    private static int testInit;
    private static int testFieldInit = 1000;

    static {
        System.out.println(" staticFinalInit " + TEST_INIT + " staticInit " + testInit + " fieldInit " + testFieldInit);
        testInit = TEST_INIT;
    }

    public static Logger getInstance() {
        String callingClassName = Thread.currentThread().getStackTrace()[2].getClass().getCanonicalName();
        return Logger.getLogger(callingClassName);
    }

    public static void main(String[] args) {
        System.out.println("MAIN staticFinalInit " + TEST_INIT + " staticInit " + testInit + " fieldInit " + testFieldInit);

        Logger logger = getInstance();
        logger.fine("LOG staticFinalInit " + TEST_INIT + " staticInit " + testInit + " fieldInit " + testFieldInit);
    }

}
