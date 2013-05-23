package cz.cuni.xrg.intlib.commons.app.rdf;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates unique file name for each pipeline run.
 *
 * @author Jiri Tomes
 *
 *
 */
public class UniqueNameGenerator {

    private static Map<String, Integer> map = new HashMap<>();

    public static String getNextName(String name) {
        int value = 1;

        if (!map.containsKey(name)) {
            map.put(name, value);
        } else {
            value = map.remove(name);
            value++;
            map.put(name, value);

        }

        int lastBot = name.lastIndexOf(".");

        if (lastBot == -1) {
            return name + "-" + String.valueOf(value);
        } else {

            String first = name.substring(0, lastBot);
            String second = name.substring(lastBot + 1, name.length());


            return first + "-" + String.valueOf(value) + "." + second;
        }
    }
}
