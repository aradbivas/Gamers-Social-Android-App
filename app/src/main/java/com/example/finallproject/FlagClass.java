package com.example.finallproject;

public abstract class FlagClass {


    private static boolean flag ;
    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag2) {
       flag = flag2;
    }
}
