package org.dsa.Array;

import java.util.HashSet;

public class Duplicates {
    public static boolean duplicate(int[] nums){
        HashSet<Integer> set =new HashSet<>();
         for(int val : nums){
             if(set.contains(val)){
                 return true;
             }
             set.add(val);
         }
         return false;
    }
    public static void main(String[] args){
        int[] arr = {1,2,3,1,4,5,7};
        System.out.println(duplicate(arr));

    }
}
