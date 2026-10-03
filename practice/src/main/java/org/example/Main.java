package org.example;

import java.util.*;

public class Main {

    public static void sortString(String []s) {
        for (int i = 0; i < s.length; i++) {
            int min=i;
            for (int j = i; j < s.length; j++) {
                if (s[j].charAt(0)<s[min].charAt(0)){
                    min=j;
                }
            }
            String temp =s[i];
            s[i]=s[min];
            s[min]=temp;
        }
        System.out.println(Arrays.toString(s));
    }
    public static void main(String[] args) {
       String[] s= {"shyam","abhay","yash","mahesh","tejaswini"};
       sortString(s);
    }
}