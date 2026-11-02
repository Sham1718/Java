package org.dsa.Array;

import java.util.Arrays;

public class TwoSum {
    // Own solution
//    public static int[] twoSum(int [] arr ,int answer){
//        if (arr.length<2) throw new IllegalArgumentException("Array length must be at least 2");
//        int total;
//        for(int i=0;i<arr.length;i++){
//            for (int j=i+1;j<arr.length;j++){
//                total=arr[i]+arr[j];
//               if(total==answer){
//                   return new int[]{i,j};
//               }
//            }
//        }
//        return new int []{-1,-1};
//    }

    //Optimized Hashing way solution
    public static int[] twoSum(int[] arr,int answer){
        return new int[]{-1,-1};
    }
    public static void main(String[] args) {
    int[] arr={3,3,9};
    int ans=18;
    int[] answer=twoSum(arr,ans);
        System.out.println(Arrays.toString(answer));
    }
}
