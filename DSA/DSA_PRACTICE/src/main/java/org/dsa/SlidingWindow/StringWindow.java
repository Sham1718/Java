package org.dsa.SlidingWindow;

import java.util.HashSet;

public class StringWindow {

    public static int longestSubString(String s) {
        int left = 0;
        int maxLength = 0;

        HashSet<Character> window = new HashSet<>();

        for (int r = 0; r < s.length(); r++) {
            if (!window.add(s.charAt(r))) {
                while (window.contains(s.charAt(r))) {
                    window.remove(s.charAt(left));
                    left++;
                }
            }
            window.add(s.charAt(r));
            maxLength = Math.max(maxLength,r - left + 1);
        }

        return maxLength;
    }
    public static void main(String[] args){
        String s= "abba";
        System.out.println(longestSubString(s));
    }
}
