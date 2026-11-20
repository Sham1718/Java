package org.dsa.SlidingWindow;

import java.lang.reflect.Array;
import java.util.Arrays;

public class OccOfAnagrams {
    public static int occAnagrams(String txt,String pat){
        int k=txt.length();
        int n=pat.length();
        int[] need=new int[26];
        int[] win=new int[26];
        for (char c: pat.toCharArray()) need[c-'a']++;
        int count=0;

        for (int r=0;r<k;r++){
            win[txt.charAt(r)-'a']++;
            if (r>=n) win[txt.charAt(r-n)-'a']--;
            if (r>=n-1 && Arrays.equals(win,need)) count++;
        }
        return count;
    }

    public static void main(String[] args){

        String p="forxxxxfrof";
        String s="for";
        System.out.println(occAnagrams(p,s));

    }
}
