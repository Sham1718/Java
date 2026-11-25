package org.dsa.Array;

import java.util.HashMap;

public class FirstUniqueCharacter {
    public static char firstUnique(String s) {

        for (int i = 0; i < s.length(); i++) {

            int count = 0;

            for (int j = 0; j < s.length(); j++) {

                if (s.charAt(i) == s.charAt(j)) {
                    count++;
                }
            }

            if (count == 1) {
                return s.charAt(i);
            }
        }

        return '\0';
    }

    public static char firstUniqueHash(String s) {

        HashMap<Character, Integer> map = new HashMap<>();

        // First pass: count frequencies
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            map.put(ch, map.getOrDefault(ch, 0) + 1);
        }

        // Second pass: find first unique character
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (map.get(ch) == 1) {
                return ch;
            }
        }

        return '\0';
    }

    public static void main(String[] args){
        String s= "leetcode";

        System.out.println(firstUnique(s));
    }
}
