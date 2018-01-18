package Model.FileTable;

import java.util.Map;

public interface FileTableInterface<K,V> {
    public void add(K key, V value);
    public void remove(K key);
    public V get(K key);
    public boolean contains(V value);
    public Map<K,V> getUnderlayingContainer();
    public Iterable<Map.Entry<K, V>> getAsIterable();
    public int size();
}
