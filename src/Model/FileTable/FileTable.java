package Model.FileTable;

import Model.Exceptions.NotExistingException;

import java.util.HashMap;
import java.util.Map;

public class FileTable<K,V> implements FileTableInterface<K,V> {
    private HashMap<K,V> fileDescriptorTable;

    public FileTable(){
        fileDescriptorTable = new HashMap<K,V>();
    }

    @Override
    public void add(K key, V value) {
        fileDescriptorTable.put(key, value);
    }

    @Override
    public void remove(K key) {
        if(!fileDescriptorTable.containsKey(key))
            throw new NotExistingException("File "+ key + " does not exist!");

        fileDescriptorTable.remove(key);
    }

    @Override
    public V get(K key) {
        if(!fileDescriptorTable.containsKey(key))
            throw new NotExistingException("File "+ key + " does not exist!");

        return fileDescriptorTable.get(key);
    }

    @Override
    public Map<K,V> getUnderlayingContainer(){
        return fileDescriptorTable;
    }

    @Override
    public Iterable<Map.Entry<K, V>> getAsIterable() {
        return fileDescriptorTable.entrySet();
    }

    @Override
    public boolean contains(V value) {
        return fileDescriptorTable.containsValue(value);
    }

    @Override
    public int size() {
        return fileDescriptorTable.size();
    }

    @Override
    public String toString(){

        StringBuffer stringBuffer = new StringBuffer();
        //stringBuffer.append("File table\n");

        for(K key : fileDescriptorTable.keySet()){
            stringBuffer.append(key + "-->" + fileDescriptorTable.get(key) + "\n");
        }

        return stringBuffer.toString();
    }
}
