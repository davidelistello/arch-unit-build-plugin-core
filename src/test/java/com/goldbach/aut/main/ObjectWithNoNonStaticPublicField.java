package com.goldbach.aut.main;

public class ObjectWithNoNonStaticPublicField {

    //this field is public, but since it's final, it's OK
    public static final int ABC = 10;
    protected static final String SD = "AAAAA";
    static final int NUMBER = 4;
    private static final int A = 10;
    static double Z = 10.2;
    protected String name = "name";
    double a = 5.3;
    private int x = 10;

}
