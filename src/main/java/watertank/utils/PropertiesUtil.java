package watertank.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtil {

    @Value("${deviceIds.valid}")
    private static String allowedDeviceIds;

    public static String getAllowedDeviceIds(){
        return allowedDeviceIds;
    }
}
