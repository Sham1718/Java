package org.dsa.SlidingWindow;

import java.net.Inet4Address;

public class VariableWindow {
    public static int subArray(int[] arr,int target){
        int l=0;
        int sum=0;
        int minLength=Integer.MAX_VALUE;
        for (int r=0;r<arr.length;r++){
            sum+=arr[r];
            while(sum>=target){
                minLength=Math.min(minLength,r-l+1);
                sum-=arr[l];
                l++;
            }
        }
        return minLength== Integer.MAX_VALUE ? 0:minLength;
    }

    public static void main(String[] args){
        int[] arr = {1,2,3,4,5};
        System.out.println(subArray(arr,11));
    }
}
