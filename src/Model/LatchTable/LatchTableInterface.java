package Model.LatchTable;

import java.util.HashMap;
import java.util.Map;

public interface LatchTableInterface<K,V> {
    // lock and unlock access to the table
    public void lock();
    public void unlock();

    // generic functions
    public void add(K key, V value);
    public void remove(K key);
    public V get(K key);
    public boolean contains(K key);
    public Map<K,V> getUnderlyingMap();
    public Iterable<Map.Entry<K, V>> getAsIterable();
    public void setUnderlyingMap(HashMap<K, V> map);
}
