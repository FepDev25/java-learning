package com.cultodeportivo.set;

import java.util.HashSet;
import java.util.Set;

import com.cultodeportivo.modelo.Alumno;

public class EjemploHashSetUnicidad {

    public static void main(String[] args) {

        Set<Alumno> sa = new HashSet<>();

        sa.add(new Alumno("Pato", 5));
        sa.add(new Alumno("Cata", 6));
        sa.add(new Alumno("Luci", 4));
        sa.add(new Alumno("Jano", 7));
        sa.add(new Alumno("Andres", 3));
        sa.add(new Alumno("Zeus2", 2));
        sa.add(new Alumno("Zeus", 2));
        sa.add(new Alumno("Lucas", 2));
        sa.add(new Alumno("Lucas", 3));
        System.out.println(sa);

        System.out.println("Iterando usando un foreach");
        for (Alumno a : sa) {
            System.out.println(a.getNombre());
        }

        System.out.println("Iterando usando Stream forEach");
        sa.forEach(System.out::println);
    }
}
