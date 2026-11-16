package org.dsa.Array;
// optimised and solution
public class StockPrice {
    public static int profit(int[] arr){
       int minPrice=arr[0];
       int maxProfit=0;
        for (int j : arr) {
            int currentProfit = j - minPrice;
            maxProfit = Math.max(maxProfit, currentProfit);
            minPrice = Math.min(j, minPrice);
        }
    return maxProfit;
    }
    public static void main(String[] args){
        int[] arr={2,5,1,8,29,0,43,8};
//        int p=profit(arr);
        System.out.println(profit(arr));
    }
}


//my own solution but it is false
//package org.dsa.Array;
//
//public class StockPrice {
//    public static int profit(int[] arr){
//        int i=0;
//        int j=arr.length-1;
//        int max=arr[j], min=arr[i];
//        while(i<=j){
//            if (arr[i]>arr[i+1]){
//                min=arr[i+1];
//            }
//            if (arr[j]<arr[j-1]){
//                max=arr[j-1];
//            }
//            i++;
//            j--;
//        }
////        System.out.println(max-min);
////        System.out.println("aaa");
//        return max-min;
//    }
//    public static void main(String[] args){
//        int[] arr={2,5,1,8,29,0,43,8};
//        int p=profit(arr);
//        System.out.println(profit(arr));
//    }
//}