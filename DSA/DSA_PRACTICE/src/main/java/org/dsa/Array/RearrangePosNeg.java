package org.dsa.Array;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RearrangePosNeg {
    public static int[] rearrange(int[] arr){
        int n=arr.length;
        int pos=0;
        for (int j:arr){
            if (j>=0)pos++;
        }
        int l=0;
        int r=arr.length-1;
        while (l<r){
            while (l<r && arr[l]>=0)l++;
            while(l<r && arr[r]<0)r--;
            if (l<r)swap(arr,l++,r--);
        }

        int pair=Math.min(pos,n-pos);
        for (int i=0;i<pair;i++){
            swap(arr,2*i+1,pos+i);
        }

        return arr;
    }
    public static void swap(int[] arr,int i,int j){
        int a=arr[i]; arr[i]=arr[j];arr[j]=a;
    };

    public static void main(String[] args){
        int[] arr={1,5,-6,-2,8,-4};
        System.out.println(Arrays.toString(rearrange(arr)));
    }
}
