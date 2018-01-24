package Utils;

import Model.Heap.HeapInterface;
import Model.LatchTable.LatchTableInterface;

public class FreeAddressGenerator {

    public static int generateFreeAddress(LatchTableInterface<Integer, Integer> latchTableInterface){
        int nextAddress = 1;

        while(latchTableInterface.contains(nextAddress)){
            nextAddress ++;
        }

        return nextAddress;
    }

    public static int generateFreeAddress(HeapInterface<Integer, Integer> heap){
        int nextAddress = 1;

        while(heap.contains(nextAddress)){
            nextAddress ++;
        }

        return nextAddress;
    }
}
