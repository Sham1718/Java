package org.dsa.Array;

public class TwoSum {
    public static int[] twoSum(int [] arr ,int answer){
        if (arr.length<2) throw new IllegalArgumentException("Array Length Must be More than two ");

        int total;
        for(int i=0;i<arr.length;i++){
            for (int j=i;j<arr.length;j++){
                total=arr[i]+arr[j];
               if(total==answer){
                   return new int[]{i,j};
               }
            }
        }
        return new int []{0,0};
    }
    public static void main(String[] args) {
        
    }
}
