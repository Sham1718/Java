package org.dsa.Array;

import java.util.Arrays;

public class RotateArray {
    public static int[] rotate(int[] arr,int k){
        int n=arr.length;
        k%=n;
        reverse(arr,0,n-1);
        reverse(arr,0,k);
        reverse(arr,k+1,n-1);
        return arr;
    }
    public static void reverse(int[] arr,int low,int high){
        while(low<high){
            int t=arr[low];
            arr[low]=arr[high];
            arr[high]=t;
            low++;
            high--;
        }
    }
    public static void main(String[] args){
        int [] arr={1,2,3,4,5,6,7};
        System.out.println(Arrays.toString(rotate(arr, 3)));

    }
}
