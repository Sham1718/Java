package org.dsa.Array;

import java.util.Arrays;

//Q13
public class MergeSortedArray {
    public static int[] merge(int[] num1,int m,int[] num2,int n){
        int pos1=m-1;
        int pos2=n-1;
        int writePost=m+n-1;

        while(pos2>=0){
            if (pos1>=0 && num1[pos1]>num2[pos2]){
                num1[writePost]=num1[pos1];
                pos1--;
            }else {
                num1[writePost]=num2[pos2];
                pos2--;
            }
            writePost--;
        }
        return num1;
    }

    public static void main(String[] args){
        int[] num1={1,2,3,0,0,0};
        int[] num2={2,5,6};
        System.out.println(Arrays.toString(merge(num1, 3, num2, 3)));
    }

}
