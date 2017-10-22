/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subsetmaker;

import java.util.ArrayList;
import java.util.Arrays;


/**
 *
 * @author jimstewart
 */

public class SubsetMaker {
    static int operations = 0;
 
    
       private class Grid{
       int s; //size
       int c; //max 1s per column
       int n;   //width of array
       int m;   //number of 1's
       boolean[][] arr;
       int[] columnCount;
       public Grid(int n, int m){
           this.n = n;
           this.m = m;
           this.s = combinatorial(n, m);
           this.c = combinatorial(n-1, m-1);
           this.arr = new boolean[s][n];
           this.columnCount = new int[n];
           Arrays.fill(this.columnCount, 0);
       }
   }

    public static void main(String[] args) {
        int[] set = new int[50];
        for(int i=0;i<set.length;i++){
            set[i] = i;
        }
        
        int[][] subSets = new SubsetMaker().makeSubSets(set, 7);
        //ArrayList<ArrayList<Integer>> subSets = new SubsetMaker().makeSubSets(set, 7);
        int num=0;
        for(int i=0; i<subSets.length;i++){
           System.out.print(i + ". {");
           int[] ss = subSets[i];
           for(int j=0;j<ss.length;j++){
                 System.out.print(ss[j] + ", ");
            }
            System.out.println("}");
        }
    }
    
    

    
    private int[][] makeSubSets(int[] set, int m){
        Grid mainGrid = new Grid(set.length,m);
        mainGrid = fillColumns(mainGrid, set.length, m, mainGrid.s, 0, 0 );
        boolean[][] arr = mainGrid.arr;
        int[][] superSet = new int[arr.length][m];
        for(int i=0;i<superSet.length;i++){
            int setPlace = 0;
            for(int j=0;j<arr[i].length;j++){
                    if(arr[i][j]){
                        try{
                        superSet[i][setPlace] = set[j];
                        } catch (ArrayIndexOutOfBoundsException e){
                            System.out.println();
                        }
                        setPlace++;
                    }
             }
        }
        return superSet;
    }
    /**
     * 
     * @param mainGrid the main grid of the problem
     * @param n the width of the sub-grid
     * @param m the number of 1s in a row
     * @param p the position to the left of a active array in the main array
     * @return the main grid
     */
    private Grid fillColumns(Grid mainGrid, int n, int m, int s, int firstCol, int firstRow){ //[tk] add int firstRow
        int[] activeRows = new int[s];
        for(int i=0;i<s;i++){
            activeRows[i] = i + firstRow;
        }
        int[] activeCols = new int[n];
        for(int i=0; i< activeCols.length; i++){
            activeCols[i] = i+firstCol;
        }
        int c = combinatorial(n-1, m-1);
//        System.out.println("Operation #" + operations++);
        if(n<m||s==0){
            return mainGrid;
        }
        if(m==1){
            for(int i=0;i<n;i++){
                try{
                mainGrid.arr[activeRows[i]][activeCols[i]] = true;
                } catch(IndexOutOfBoundsException e){
                    System.out.println();
                }
            }
//            printGrid(mainGrid);
            return mainGrid;
        }
        for(int i=0;i<c;i++){
            try{
            mainGrid.arr[activeRows[i]][activeCols[0]] = true;
            } catch(Exception e){
                System.out.println(e);
                System.out.println();
            }
        }
        mainGrid = fillColumns(mainGrid,n-1, m-1, c, firstCol+1, firstRow); //[tk] pass firstrow
        mainGrid = fillColumns(mainGrid, n-1, m, s-c, firstCol+1, firstRow + c); // [k] pass firstrow
        return mainGrid;
    }
    
    private int combinatorial(int n, int m){
        ArrayList<Integer> numerator = new ArrayList<>();
        ArrayList<Integer> denominator = new ArrayList<>();
        for(int i=2;i<=n;i++){
            numerator.add(i);
        }
        for(int i=2;i<=m;i++){
            if(numerator.contains(i))
                numerator.remove(numerator.indexOf(i));
            else
                denominator.add(i);
        }
        for(int i=2;i<=n-m;i++){
            if(numerator.contains(i))
                numerator.remove(numerator.indexOf(i));
            else
                denominator.add(i);
        }
        for(int i=0;i<denominator.size();i++)
            try{
            for(int j=0;j<numerator.size();j++){
                if(denominator.isEmpty())
                    break;
                if(numerator.get(j)%denominator.get(i)==0){
                    numerator.set(j, numerator.get(j)/denominator.get(i));
                    denominator.remove(i);
                }
            }
            } catch (IndexOutOfBoundsException e){
                System.out.println();
            }
        int c = 1;
        for(int i=0;i<numerator.size();i++){
            c*=numerator.get(i);
        }
        for(int i=0;i<denominator.size();i++){
            c/=denominator.get(i);
        }
        return c;
    }

}
