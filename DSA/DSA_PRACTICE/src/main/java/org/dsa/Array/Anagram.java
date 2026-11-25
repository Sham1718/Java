package org.dsa.Array;

import java.util.HashMap;

public class Anagram {
    public static boolean isAnagram(String s ,String t){
        HashMap<Character,Integer> anagram = new HashMap<>();
        String a=s.toLowerCase();
        String b=t.toLowerCase();
        if (s.length()!=t.length()){
            return false;
        }
        for(int i=0;i<a.length();i++){
            anagram.put(a.charAt(i),anagram.getOrDefault(a.charAt(i),0)+1);
        }

        for (int i=0; i<b.length();i++){
            anagram.put(b.charAt(i),anagram.getOrDefault(b.charAt(i),0)-1);
        }

        for(int val: anagram.values()){
            if(val!=0){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        String a= "forxxxfor";
        String b="xxxroffor";
        System.out.println(isAnagram(a,b));
    }
}
