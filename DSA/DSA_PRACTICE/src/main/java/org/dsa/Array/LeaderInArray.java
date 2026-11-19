package org.dsa.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//Time Complexity =O(n^2);
//space complexity=o(n);

//public class LeaderInArray {
//    public static List<Integer> leader(int[] arr){
//        List<Integer> leader=new ArrayList<>();
//        for (int i=0;i<arr.length;i++){
//            boolean isLeader=true;
//            for (int j=i+1;j<arr.length;j++){
//                if (arr[i] < arr[j]) {
//                    isLeader = false;
//                    break;
//                }
//            }
//            if(isLeader){
//                leader.add(arr[i]);
//            }
//        }
//        return leader;
//    }
//
//    public static void main(String[] args){
//
//        int[] arr={16,17,4,3,5,2};
//
//        System.out.println(leader(arr));
//    }
//}


//Optimal Solution

public class LeaderInArray {
    public static List<Integer> leader(int[] arr){
        List<Integer>leader=new ArrayList<>();
        int max=Integer.MIN_VALUE;
        for (int i=arr.length-1;i>0;i--){
            if (arr[i]>=max){
                leader.add(arr[i]);
            }
            max=Math.max(max,arr[i]);
        }
        Collections.reverse(leader);
        return leader;
    }
    public static void main(String[] args){
        int[] arr={16,17,4,3,5,2};
        System.out.println(leader(arr));
    }
}
