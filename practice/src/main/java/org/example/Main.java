package org.example;



import java.util.*;

public class Main
{
    public static void firstOcc(int []arr) {
        Set<Integer> occ=new HashSet<>();
        for (int j:arr){
            if(!occ.add(j)){
                System.out.println(j);
                break;
            }
        }
    }
    public static void main(String[] args) {
        int[] arr = {1,2,6,4,7,6,9,8,9,7,1};
       firstOcc(arr);
    }
}