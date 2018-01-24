package Model.LatchTable;

import Model.Exceptions.GenericException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LatchTable<K,V> implements LatchTableInterface<K,V> {
    private Lock latchTableLock;
    private Map<K,V> container;

    public LatchTable(){
        latchTableLock = new ReentrantLock();
        container = new HashMap<K,V>();
    }

    @Override
    public void lock() {
        latchTableLock.lock();
    }

    @Override
    public void unlock() {
        latchTableLock.unlock();
    }

    @Override
    public void add(K key, V value) {
        // synchronize
        latchTableLock.lock();

        container.put(key, value);

        latchTableLock.unlock();
    }

    @Override
    public void remove(K key) {
        // synchronize
        latchTableLock.lock();

        if(!container.containsKey(key))
            throw new GenericException("Invalid Key for LatchTable!");

        container.remove(key);

        latchTableLock.unlock();
    }

    @Override
    public V get(K key) {
        //synchronize
        latchTableLock.lock();

        if(!container.containsKey(key))
            throw new GenericException("Invalid Key for LatchTable!");

        latchTableLock.unlock();
        return container.get(key);

    }

    @Override
    public boolean contains(K key) {
        return container.containsKey(key);
    }

    @Override
    public Map<K, V> getUnderlyingMap() {
        return container;
    }

    @Override
    public Iterable<Map.Entry<K, V>> getAsIterable() {
        return container.entrySet();
    }

    @Override
    public void setUnderlyingMap(HashMap<K, V> map) {

    }

    @Override
    public String toString() {
        //synchronize
        latchTableLock.lock();

        StringBuilder stringBuilder = new StringBuilder();

        for(K key : container.keySet()){
            stringBuilder.append("\n\t");
            stringBuilder.append(key + "->" + get(key).toString());
        }

        latchTableLock.unlock();

        return stringBuilder.toString();
    }
}
