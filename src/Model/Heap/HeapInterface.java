package Model.Heap;

import java.util.HashMap;
import java.util.Map;

public interface HeapInterface<K,V> {
    public void add(K key, V value);
    public void remove(K key);
    public V get(K key);
    public boolean contains(K key);
    public Map<K,V> getUnderlyingMap();
    public Iterable<Map.Entry<K, V>> getAsIterable();
    public void setUnderlyingMap(HashMap<K, V> map);
}
