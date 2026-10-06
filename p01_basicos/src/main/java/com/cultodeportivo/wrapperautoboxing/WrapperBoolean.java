package com.cultodeportivo.wrapperautoboxing;

@SuppressWarnings("UnnecessaryBoxing")
public class WrapperBoolean {

    
    public static void main(String[] args) {

        boolean primBoolean = 8 > 2;
        Boolean objBoolean = Boolean.valueOf(primBoolean);
        Boolean objBoolean2 = Boolean.valueOf("false");
        Boolean objBoolean3 = false;

        System.out.println(primBoolean);
        System.out.println(objBoolean);
        System.out.println(objBoolean2);
        System.out.println(objBoolean3);

        System.out.println(primBoolean == objBoolean);
    }
}
