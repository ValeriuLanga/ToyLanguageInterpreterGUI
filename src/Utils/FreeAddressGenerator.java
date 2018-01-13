package Utils;

import Model.Heap.HeapInterface;

public class FreeAddressGenerator {

    public static int generateFreeAddress(HeapInterface<Integer, Integer> heap){
        int nextAddress = 1;

        while(heap.contains(nextAddress)){
            nextAddress ++;
        }

        return nextAddress;
    }
}
