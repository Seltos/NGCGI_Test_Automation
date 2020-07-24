package web_selenium.serialization.json;

import java.util.HashMap;
import java.util.Map;

/**
 * A HasMap whose values can be "trimmed" if they exceed a certain length when
 * serialized to JSON.
 */
public class TrimmableMap<K, V> extends HashMap<K, V> {

    public TrimmableMap() {
    }

    public TrimmableMap(Map<? extends K, ? extends V> map) {
        super(map);
    }
}
