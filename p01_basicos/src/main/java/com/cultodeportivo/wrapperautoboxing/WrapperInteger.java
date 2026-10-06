package com.cultodeportivo.wrapperautoboxing;

@SuppressWarnings("UnnecessaryBoxing")
public class WrapperInteger {

    public static void main(String[] args) {
        int intPrimitivo = 19;
        Integer intObjeto = Integer.valueOf(intPrimitivo);
        System.out.println("Int primitivo: " + intPrimitivo);
        System.out.println("Int objeto: " + intObjeto);

        Integer intObjeto2 = 12;
        int intPrimitivo2 = intObjeto2; 
        System.out.println("Int primitivo: " + intPrimitivo2);
        System.out.println("Int objeto: " + intObjeto2);
    }
}
