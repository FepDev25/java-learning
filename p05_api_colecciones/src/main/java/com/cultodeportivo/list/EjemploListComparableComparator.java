package com.cultodeportivo.list;

import java.util.Collections;
import static java.util.Comparator.comparing;
import java.util.LinkedList;
import java.util.List;

import com.cultodeportivo.modelo.Alumno;

public class EjemploListComparableComparator {

    public static void main(String[] args) {

        List<Alumno> sa = new LinkedList<>();

        sa.add(new Alumno("Pato", 5));
        sa.add(new Alumno("Cata", 6));
        sa.add(new Alumno("Luci", 4));
        sa.add(new Alumno("Jano", 7));
        sa.add(new Alumno("Andres", 3));
        sa.add(new Alumno("Zeus2", 2));
        sa.add(new Alumno("Zeus", 2));
        sa.add(new Alumno("Lucas", 2));
        sa.add(new Alumno("Lucas", 3));

        System.out.println("=== Lista sin ordenar ===");
        System.out.println(sa);

        // Orden natural (Comparable)
        System.out.println("=== Ordenando por orden natural (nombre) ===");
        Collections.sort(sa);
        System.out.println(sa);

        System.out.println("=== Ordenando por nota (Comparator) ===");
        Collections.sort(sa, (a, b) -> a.getNota().compareTo(b.getNota()));
        System.out.println(sa);

        System.out.println("=== Ordenando por nota usando Comparator.comparing ===");
        Collections.sort(sa, comparing(Alumno::getNota));
        System.out.println(sa);

        System.out.println("=== Ordenando por nombre reversa usando comparing ===");
        sa.sort(comparing(Alumno::getNombre).reversed());
        System.out.println(sa);

        System.out.println("Iterando usando Stream forEach");
        sa.forEach(System.out::println);
    }
}
