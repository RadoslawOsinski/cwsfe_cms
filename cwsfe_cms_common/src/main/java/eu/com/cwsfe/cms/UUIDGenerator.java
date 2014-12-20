package eu.com.cwsfe.cms;

import java.util.UUID;

/**
 * @author radek
 */
public class UUIDGenerator {

    public static String getRandomUniqueID() {
        return UUID.randomUUID().toString();
    }

}
