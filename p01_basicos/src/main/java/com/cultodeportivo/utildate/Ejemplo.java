package com.cultodeportivo.utildate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Ejemplo {
    public static void main(String[] args) {
        Calendar nacimiento = Calendar.getInstance();
        nacimiento.set(Calendar.YEAR, 2005);
        nacimiento.set(Calendar.MONTH, Calendar.JANUARY);
        nacimiento.set(Calendar.DAY_OF_MONTH, 25);

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS a");
        String fechaConFormato = formato.format(nacimiento.getTime());
        System.out.println("fecha Con Formato = " + fechaConFormato);


    }
}
