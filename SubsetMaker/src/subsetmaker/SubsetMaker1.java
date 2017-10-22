/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsetmaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;

/**
 *
 * @author jimstewart
 */
public class SubsetMaker1 {
    static int operations = 0;
    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        int[] set = new int[7];
//        for(int i=0;i<set.length;i++){
//            set[i] = i;
//        }
//        
//        ArrayList<ArrayList<Integer>> subSets = new SubsetMaker1().makeSubSets2(set, 3);
////        ArrayList<ArrayList<Integer>> subSets = new SubsetMaker().makeSubSets(set, 7);
//        int num=0;
//        for(int i=0; i<subSets.size();i++){
//           System.out.print(i + ". {");
//           ArrayList<Integer> ss = subSets.get(i);
//           for(int j=0;j<ss.size();j++){
//                 System.out.print(ss.get(j) + ", ");
//            }
//            System.out.println("}");
//        }
//        //System.out.println(operations + " operations");
//    }

    
    private ArrayList<ArrayList<Integer>> makeSubSets(int[] set, int m){
        ArrayList<Integer> currentSet = new ArrayList<>();
        ArrayList<ArrayList<Integer>> superSet = new ArrayList<>();
        subSetMaker(m, 0, 0, set, currentSet, superSet);
        return superSet;
    }
    
    private void subSetMaker(int m, int pos, int depth, int[] set, ArrayList<Integer> currentSet, ArrayList<ArrayList<Integer>> superSet){
        operations++;
        int iterLim = set.length-m+pos+1;
        for(int i = pos; i<iterLim; i++){
            if(i+depth>=set.length)
                return;
            ArrayList<Integer> newSet = new ArrayList<Integer>(currentSet);
            int numberToAdd = set[i + depth];
            newSet.add(numberToAdd);
            if(newSet.size()==m)
                superSet.add(newSet);
                subSetMaker(m, i, depth+1, set, newSet, superSet);
        }
    }
    
    private ArrayList<ArrayList<Integer>> makeSubSets2( int[] set, int m){
        ArrayList<ArrayList<Integer>> returnArray = new ArrayList<>();
        int n = set.length;
        double p = 1;
        if(m==0)
            return returnArray;
        BitSet bSet = new BitSet(n);
        ArrayList<BitSet> bSetArray = new ArrayList<>();
        for(int i=0;i<m;i++){
            bSet.set(i, true);
            //printBitSet(bSet, n, "bset");
        }
        
        
        bSetArray.add((BitSet)bSet.clone());
        printBitSet(bSet, n, "added bitset");
        bSetArray = shiftBits(n, bSet, bSetArray, m);

        for(int i=0;i<bSetArray.size();i++){
           ArrayList<Integer> newSet = new ArrayList<>();
           bSet = bSetArray.get(i);
           for(int j=0;j<n;j++){
                System.out.print(bSet.get(j)?"1":"0");
                if(bSet.get(j))
                    try{
                    newSet.add(set[j]);
                    } catch(IndexOutOfBoundsException e) {
                        System.out.printf("System out of bounds at index %d ", j-1);
                    }
           }
        System.out.println();
        returnArray.add(newSet);

        }
        return returnArray;
        
    }
    
    private ArrayList<BitSet> shiftBits(int n, BitSet bSet, ArrayList<BitSet>bSetArray, int m){
        BitSet end = new BitSet(n);
        for(int i=n;i>n-m;i--){
            end.set(i-1,true);
        }
        //printBitSet(end, n, "end");
        //printBitSet(bSet, n, "bset");
        while(!bSet.equals(end)){
            BitSet bSetCopy = (BitSet)bSet.clone();
            boolean[] usedBits = new boolean[n];
            int bitsUsed = 0;
            boolean firstInLoop = true;
            while(bitsUsed < m){
                //printBitSet(bSet, n, "b start of loop");
               int pos = bSet.previousSetBit(n);
               while(!bSet.get(pos) || usedBits[pos]){
                    pos--;
                    if(pos<4)
                        System.out.println();
                }
                int currentBit = pos;
                if(firstInLoop){
                    firstInLoop = false;
                    pos=firstFreeSpace(bSet, pos, n);
                    //around here function needs to call
                    //itself recursively with n-1, m-1 and 
                    //1 position to the right for all sets of bits
                    //greater than 1. maybe shiftBitRight
                    //needs to work on more than 1 bit.
                    bSetArray.add((BitSet)bSet.clone());
                    printBitSet(bSet, n, "added bitset");
                    if(pos==-1)
                        break;
                }
                while((pos=shiftBitRight(bSet, pos, n))!=-1){
                    bSetArray.add((BitSet)bSet.clone());
                    printBitSet(bSet, n, "added bitset");
                }
                usedBits[currentBit] = true;
                bitsUsed++;
                //printBitSet(bSet, n, "b end of loop");
                bSet = (BitSet)bSetCopy.clone();
                firstInLoop = true;
            }
            Arrays.fill(usedBits, false);
            bitsUsed = 0;
            for(int i=n; i>0;i--){
                bSet.set(i, bSet.get(i-1));
            }
            bSet.clear(0);
        }
        return bSetArray;
    }
    
    private int shiftBitRight(BitSet bSet, int pos, int n){
        if(bSet.get(pos) && pos<n-1){
            bSet.clear(pos);
            bSet.set(pos+1, true);
            //printBitSet(bSet, n, "bSet in shiftBit:");
            return pos+1;
        }
        return -1;
    }
    
    private int firstFreeSpace(BitSet bSet, int pos, int n){
        bSet.clear(pos);
        int ffs = bSet.previousSetBit(n)+1;
        if(ffs!=-1){
            bSet.set(ffs);
            //printBitSet(bSet, n, "bSet in ffs:");
            return ffs;
        }
        return -1;
    }
    
    private void printBitSet(BitSet b, int n, String name){
        System.out.println("Bitset " + name + ":");
        for(int i=0;i<n;i++){
             System.out.print(b.get(i)?"1":"0");
        }
        System.out.println();
    }
    
    private double combinatorial(int n, int m){
        ArrayList<Integer> numerator = new ArrayList<>();
        ArrayList<Integer> denominator = new ArrayList<>();
        for(int i=1;i<=n;i++){
            numerator.add(i);
        }
        for(int i=0;i<=m;i++){
            denominator.add(m);
        }
        for(int i=0;i<n-m;i++){
            denominator.add(m);
        }
        for(int i=0;i<denominator.size();i++){
            int d = denominator.get(i);
            if(numerator.contains(d)){
                numerator.remove(d);
                denominator.remove(i);
            }
        }
        double c = 1;
        for(int i=0;i<numerator.size();i++){
            c*=(double)numerator.get(i);
        }
        for(int i=0;i<denominator.size();i++){
            c/=(double)denominator.get(i);
        }
        return c;
    }
}
