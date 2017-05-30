package ba.steleks.storage.store;

/**
 * Created by ensar on 30/05/17.
 */
public interface KeyValueStore<K, V> {
    void save(K key, V value);
    V get(K key);
    boolean contains(K key);
    void remove(K key);
}
