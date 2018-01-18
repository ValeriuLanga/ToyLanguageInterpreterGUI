package Model.Heap;

import Model.Exceptions.HeapException;

import java.util.HashMap;
import java.util.Map;

public class Heap<K,V> implements HeapInterface<K,V>{
    private HashMap<K,V> heapContainer;

    public Heap(){
        heapContainer = new HashMap<>();
    }

    @Override
    public void add(K key, V value) {
        heapContainer.put(key, value);
    }

    @Override
    public void remove(K key) {
        if(!heapContainer.containsKey(key))
            throw new HeapException("Invalid Address!");

        heapContainer.remove(key);
    }

    @Override
    public V get(K key) {
        if(!heapContainer.containsKey(key))
            throw new HeapException("Invalid Address!");

        return heapContainer.get(key);
    }

    @Override
    public boolean contains(K key) {
        return heapContainer.containsKey(key);
    }

    @Override
    public Map<K, V> getUnderlyingMap() {
        return heapContainer;
    }

    @Override
    public Iterable<Map.Entry<K, V>> getAsIterable() {
        return heapContainer.entrySet();
    }

    @Override
    public void setUnderlyingMap(HashMap<K, V> map) {
        heapContainer = map;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for(K key : heapContainer.keySet()){
            stringBuilder.append("\n\t");
            stringBuilder.append(key + "->" + get(key).toString());
        }

        return stringBuilder.toString();
    }
}
