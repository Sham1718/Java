package org.dsa.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    // Own solution
    public static int[] twoSumOwn(int [] arr ,int answer){
        if (arr.length<2) throw new IllegalArgumentException("Array length must be at least 2");
        int total;
        for(int i=0;i<arr.length;i++){
            for (int j=i+1;j<arr.length;j++){
                total=arr[i]+arr[j];
               if(total==answer){
                   return new int[]{i,j};
               }
            }
        }
        return new int []{-1,-1};
    }

    //Optimized Hashing way solution
    public static int[] twoSum(int[] arr,int answer){
        Map<Integer,Integer> seen =new HashMap<>();
        for(int i=0;i<arr.length;i++){
            int need=answer-arr[i];
            if (seen.containsKey(need)){
                return new int[]{seen.get(need),i};
            }
            seen.put(arr[i],i);
        }
        return new int[]{-1,-1};
    }
    public static void main(String[] args) {
    int[] arr={3,3,9,9,7,8,5,2,4,1};
    int ans=10;
    int[] answer=twoSum(arr,ans);
        System.out.println(Arrays.toString(answer));
    }
    //Space complexity: O(n)
    //Time Complexity :O(n)
}
