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

public class SubsetMaker2 {
    static int operations = 0;
 
    private class BoolObj{
        boolean b;
        BoolObj(){
            b=false;
        }
        BoolObj(boolean b){
            this.b = b;
        }
        void set(){
            b=true;
        }
        void clear(){
            b=false;
        }

    }
    
       private class Grid{
       int s; //size
       int c; //max 1s per column
       int n;   //width of array
       int m;   //number of 1's
       BoolObj[][] arr;
       Integer[] columnCount;
       public Grid(int n, int m, int s, Grid mainGrid, Integer[] activeRows, Integer[] activeCols){
           this.n = n;
           this.m = m;
           this.s = s;
           this.c = combinatorial(n-1, m-1);
           this.arr = new BoolObj[s][n];
           this.columnCount = new Integer[this.n];
           for(int i=0;i<arr.length;i++){
               for(int j=0;j<arr[i].length;j++)
                   arr[i][j] = new BoolObj();
           }
           int rowNum = 0;
           for(int row:activeRows){
               int colNum = 0;
               for(int col:activeCols){
                   if(rowNum==0)
                       try{
                       this.columnCount[colNum] = mainGrid.columnCount[col];
                       } catch (ArrayIndexOutOfBoundsException e){
                           System.out.println();
                       }
                   try{
                   this.arr[rowNum][colNum] = mainGrid.arr[row][col];
                   } catch (IndexOutOfBoundsException e) {
                       System.out.println();
                   }
                   colNum++;
               }
               rowNum++;
           }

       }
       public Grid(int n, int m){
           this.n = n;
           this.m = m;
           this.s = combinatorial(n, m);
           this.c = combinatorial(n-1, m-1);
           this.arr = new BoolObj[s][n];
           this.columnCount = new Integer[n];
           Arrays.fill(this.columnCount, 0);
           for(int i=0;i<arr.length;i++){
               for(int j=0;j<arr[i].length;j++)
                   arr[i][j] = new BoolObj();
           }
       }
   }

//    public static void main(String[] args) {
//        int[] set = new int[30];
//        for(int i=0;i<set.length;i++){
//            set[i] = i;
//        }
//        
//        int[][] subSets = new SubsetMaker2().makeSubSets(set, 8);
//        //ArrayList<ArrayList<Integer>> subSets = new SubsetMaker().makeSubSets(set, 7);
//        int num=0;
//        for(int i=0; i<subSets.length;i++){
//           System.out.print(i + ". {");
//           int[] ss = subSets[i];
//           for(int j=0;j<ss.length;j++){
//                 System.out.print(ss[j] + ", ");
//            }
//            System.out.println("}");
//        }
//        //System.out.println(operations + " operations");
//    }
    
    

    
    private int[][] makeSubSets(int[] set, int m){
        Grid mainGrid = new Grid(set.length,m);
        mainGrid = fillColumns(mainGrid, set.length, m, mainGrid.s, 0, true,0 );
        BoolObj[][] arr = mainGrid.arr;
        int[][] superSet = new int[arr.length][m];
        for(int i=0;i<superSet.length;i++){
            int setPlace = 0;
            for(int j=0;j<arr[i].length;j++){
                    if(arr[i][j].b){
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
    private Grid fillColumns(Grid mainGrid, int n, int m, int s, int firstCol, boolean leftMostVal, int firstRow){ //[tk] add int firstRow
//        System.out.println("Operation #" + operations++);
        ArrayList<Integer> activeRows = new ArrayList<>();
        ArrayList<Integer> activeCols = new ArrayList<>();
        if(n<m||s==0){
            return mainGrid;
        }
        for(int i=firstRow;i<mainGrid.s; i++){
            if(firstCol>0){
               if(mainGrid.arr[i][firstCol-1].b==leftMostVal){
                    activeRows.add(i);
                }
            } else {
                activeRows.add(i);
            }
        }
        for(int i=firstCol;i<mainGrid.n;i++){
            if(mainGrid.columnCount[i]<mainGrid.c){
                activeCols.add(i);
            }
        }
        if(activeRows.isEmpty()||activeCols.isEmpty()){
//            printGrid(mainGrid);
            return mainGrid;
        }
        Grid activeGrid = new Grid(activeCols.size(), m, activeRows.size(), mainGrid, activeRows.toArray(new Integer[activeRows.size()]), activeCols.toArray(new Integer[activeCols.size()]));
//        System.out.println("Active grid at beginning of method:");
//        printGrid(activeGrid);
        if(m==1){
            for(int i=0;i<activeGrid.n;i++){
                try{
                activeGrid.arr[i][i].set();
                } catch(IndexOutOfBoundsException e){
                    System.out.println();
                }
            }
//            printGrid(mainGrid);
            return mainGrid;
        }
        for(int i=0;i<activeGrid.c;i++){
            try{
            activeGrid.arr[i][0].set();
            } catch(Exception e){
                System.out.println(e);
                System.out.println();
            }
        }
//        System.out.println("Active grid before methods method:");
//        printGrid(activeGrid);
//        System.out.println("Main grid before methods method:");
//        printGrid(mainGrid);
        mainGrid = fillColumns(mainGrid,n-1, m-1,activeGrid.c, firstCol+1, true, firstRow); //[tk] pass firstrow
//        System.out.println("Active grid at end of first method:");
//        printGrid(activeGrid);
//        System.out.println("Main grid at end of first method:");
//        printGrid(mainGrid);
        mainGrid = fillColumns(mainGrid, n-1, m, activeGrid.s-activeGrid.c, firstCol+1, false, firstRow + activeGrid.c); // [k] pass firstrow
//        System.out.println("Active grid at end of second method:");
//        printGrid(activeGrid);
//        System.out.println("Main grid at end of second method:");
//        printGrid(mainGrid);
//        System.out.println("Returning main grid:");
//        printGrid(mainGrid);
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

    private void printGrid(Grid grid){
        System.out.printf("c = %d, m =%d, n = %d, s = %d%n", grid.c, grid.m, grid.n, grid.s);
        System.out.println("Column count:");
        for(int i=0;i<grid.columnCount.length;i++){
            System.out.printf("%d ", grid.columnCount[i]);
        }
        System.out.println();
        System.out.println("arr:");
        if(grid.arr.length>0&&grid.arr[0].length>0){
        for(int i=0;i<grid.arr.length;i++){
            for(int j=0;j<grid.arr[i].length;j++){
                System.out.printf("%d ",grid.arr[i][j].b?1:0);
            }
        System.out.println();
        }
        }
        System.out.println();
    }
}
