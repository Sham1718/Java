package org.dsa.Array;

import java.util.*;

public class MajorityElement {
    //OWN SOLUTION n=3
    public static List<Integer> majorityEle(int[] arr){
        Map<Integer,Integer> freq=new HashMap<>();
        for (int j : arr) {
            freq.put(j, freq.getOrDefault(j, 0) + 1);
        }
        int majority=arr.length/3;
        List<Integer> needs=new ArrayList<>();
        for (Map.Entry<Integer,Integer> e: freq.entrySet()){
            if (e.getValue()>majority){
                 needs.add(e.getKey());
            }
        }
        return needs;
    }
    // FOR N=2
    public static int majorityEle2(int[] arr){
        Map<Integer,Integer> freq=new HashMap<>();
        for (int j : arr) {
            freq.put(j, freq.getOrDefault(j, 0) + 1);
        }
        int m=arr.length/3;
        for (Map.Entry<Integer,Integer> e: freq.entrySet()){
            if (e.getValue()>m){
                return  e.getKey();

            }
        }
        return -1;
    }

    //BOYER-MOORE
    class Solution {
        public List<Integer> majorityElement(int[] nums) {
            int c1 = 0, c2 = 0, k1 = 0, k2 = 0;
            for (int x : nums) {
                if (x == c1) k1++;
                else if (x == c2) k2++;
                else if (k1 == 0) { c1 = x; k1 = 1; }
                else if (k2 == 0) { c2 = x; k2 = 1; }
                else { k1--; k2--; }
            }
            k1 = 0; k2 = 0;
            for (int x : nums) {
                if (x == c1) k1++;
                else if (x == c2) k2++;
            }
            List<Integer> res = new ArrayList<>();
            if (k1 > nums.length / 3) res.add(c1);
            if (c2 != c1 && k2 > nums.length / 3) res.add(c2);
            return res;
        }
    }

    //OPTIMAL / README SOLUTION
    public static int majorityElement(int[] nums) {
        int candidate = 0, count = 0;
        for (int x : nums) {
            if (count == 0) {
                candidate = x;
                count = 1;
            } else if (x == candidate) {
                count++;
            } else {
                count--;
            }
        }
        return candidate;
    }
    public static void main(String []args){
     int[] arr={1,1,2,2,0};
//     System.out.println(Arrays.toString(majorityEle(arr)));
//     System.out.println(majorityElement(arr));
    }
}
