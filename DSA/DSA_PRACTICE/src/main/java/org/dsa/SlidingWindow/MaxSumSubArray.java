package org.dsa.SlidingWindow;

public class MaxSumSubArray {
    public static int subArray(int[] arr,int k) {
        int sum=0;

        for (int i=0;i<arr.length-2;i++){
           int a=arr[i]+arr[i+1]+arr[i+2];
           sum=Math.max(a,sum);
        }
        return sum;
    }

    public static int subArrayOptimal(int[] arr, int k) {

        if (k <= 0 || k > arr.length) {
            throw new IllegalArgumentException("Invalid window size");
        }

        int windowSum = 0;

        // Calculate first window
        for (int i = 0; i < k; i++) {
            windowSum += arr[i];
        }

        int maxSum = windowSum;

        // Slide the window
        for (int i = k; i < arr.length; i++) {

            windowSum += arr[i] - arr[i - k];

            maxSum = Math.max(maxSum, windowSum);
        }

        return maxSum;
    }
    public static void main(String[] args){
        int[] arr={2,1,5,2,1,3};
        int k=3;
        System.out.println(subArray(arr,k));
    }
}
