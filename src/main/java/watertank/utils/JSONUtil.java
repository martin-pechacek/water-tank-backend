package watertank.utils;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;

import java.util.List;

/**
 * Helper methods providing support for working with JSONs
 */
public class JSONUtil {

    /**
     * @param stringContent JSON as string which is supposed to be parsed
     * @return JSONParser object
     */
    public static JSONParser getJSONParser(final String stringContent) {
        return new JSONParser(stringContent);
    }

    /**
     * @param stringContent JSON array as string which is supposed to be parsed
     * @return ArrayList from provided string
     */
    public static List<Object> parseStringToArray(final String stringContent) throws ParseException {
        JSONParser parser = getJSONParser(stringContent);
        return parser.list();
    }
}
