package org.dsa.Array;

import java.util.Arrays;
import java.util.HashMap;

public class TwoSumHash {
    public static int[] twoSum(int[] nums,int target){
        HashMap<Integer,Integer> seen =new HashMap<>();
        int need=0;

        for (int i=0; i<nums.length;i++){
            need=target-nums[i];
            if (seen.containsKey(need)){
                return new int[]{seen.get(need),i};
            }
            seen.put(nums[i],i);

        }
        return new int[]{};
    }
    public static void main(String[] args){
        int[] arr={2,7,11,15};
        System.out.println(Arrays.toString(twoSum(arr, 9)));
    }

}
