package org.example;


import java.util.Arrays;

public class Main
{
    public static void reverse(String s){
        StringBuilder sb=new StringBuilder();
        for (int i = s.length()-1;i >=0; i--) {
            sb.append(s.charAt(i));
        }
        System.out.println(sb);
    }
    public static void main(String[] args) {
       String s="JAVA";
       reverse(s);
    }
}