package Model.SymbolTable;

import java.util.HashMap;

public class SymbolTable<K,V> implements SymbolTableInterface<K,V>
{
    private HashMap<K,V> container;

    public SymbolTable(){

        container = new HashMap<K,V>();
    }

    public V get(K key){
        return container.get(key);
    }

    public boolean contains(K key){
        return container.containsKey(key);
    }

    public void add(K key, V value){
        container.put(key, value);
    }

    public void update(K key, V value){
        container.put(key, value);
    }

    public void put(K key, V value){
        container.put(key, value);
    }

    public void replace(K key, V value){
        container.replace(key, value);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();

        for(K key : container.keySet()){
            stringBuilder.append("\n\t");
            stringBuilder.append(key + "->" + get(key).toString());
        }

        return stringBuilder.toString();
    }

    @Override
    public HashMap<K, V> getUnderlyingMap() {
        return container;
    }

    @Override
    public Iterable<HashMap.Entry<K, V>> getAsIterableMap() {
        return container.entrySet();
    }

    @Override
    public SymbolTableInterface<K,V> clone(){
        SymbolTableInterface<K,V> symbolTableInterfaceClone = new SymbolTable<>();

        for( K key : container.keySet()){
            V value = container.get(key);
            symbolTableInterfaceClone.put(key, value);
        }

        return  symbolTableInterfaceClone;
    }
}
