package ba.steleks.storage.store;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ensar on 30/05/17.
 */

@Component
public class HashMapKeyValueStore<K, V> implements KeyValueStore<K, V> {

    private final Map<K, V> kvMap;

    public HashMapKeyValueStore() {
        kvMap = new HashMap<>();
    }

    public HashMapKeyValueStore(Map<K, V> kvMap) {
        this.kvMap = kvMap;
    }

    @Override
    public void save(K key, V value) {
        kvMap.put(key, value);
    }

    @Override
    public V get(K key) {
        return kvMap.get(key);
    }

    @Override
    public boolean contains(K key) {
        return kvMap.containsKey(key);
    }

    @Override
    public void remove(K key) {
        kvMap.remove(key);
    }
}
