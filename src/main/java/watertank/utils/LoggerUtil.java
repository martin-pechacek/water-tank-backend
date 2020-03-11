package watertank.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    public static void info(final Class cls, final String message){
        Logger logger = LoggerFactory.getLogger(cls);
        logger.info(message);
    }

    public static void error(final Class cls, final String message){
        Logger logger = LoggerFactory.getLogger(cls);
        logger.error(message);
    }

}
