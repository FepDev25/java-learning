package com.cultodeportivo.list;

import java.util.ArrayList;
import java.util.List;

import com.cultodeportivo.modelo.Alumno;

public class EjemploArrayList {

    public static void main(String[] args) {

        List<Alumno> al = new ArrayList<>();

        // Estado inicial
        System.out.println("=== ESTADO INICIAL ===");
        System.out.println("Lista: " + al + ", tamaño = " + al.size());
        System.out.println("¿La lista está vacía? " + al.isEmpty());

        // Agregar elementos
        System.out.println("\n=== AGREGANDO ELEMENTOS ===");
        al.add(new Alumno("Pato", 5));
        System.out.println("Agregado: Pato con nota 5");
        al.add(new Alumno("Cata", 6));
        System.out.println("Agregado: Cata con nota 6");
        al.add(new Alumno("Luci", 4));
        System.out.println("Agregado: Luci con nota 4");
        al.add(2, new Alumno("Jano", 7));
        System.out.println("Agregado en posición 2: Jano con nota 7");
        al.set(3, new Alumno("Andres", 3));
        System.out.println("Reemplazado en posición 3: Andres con nota 3");

        // Estado con elementos
        System.out.println("\n=== ESTADO ACTUAL DE LA LISTA ===");
        System.out.println("Lista: " + al + ", tamaño = " + al.size());

        // Eliminar elementos
        System.out.println("\n=== ELIMINANDO ELEMENTOS ===");
        al.remove(new Alumno("Jano", 7));
        System.out.println("Eliminado: Jano con nota 7");
        
        // Estado después de eliminar
        System.out.println("\n=== ESTADO DESPUÉS DE ELIMINAR ===");
        System.out.println("Lista: " + al + ", tamaño = " + al.size());

        // Obtener elemento
        System.out.println("\n=== BÚSQUEDA EN LA LISTA ===");
        boolean b = al.contains(new Alumno("Cata", 6));
        System.out.println("¿La lista contiene a Cata con nota 6? " + b);

        // Tranformar a arreglo
        System.out.println("\n=== CONVERSIÓN A ARREGLO ===");
        Object a[] = al.toArray();
        for (Object a1 : a) {
            System.out.println("Elemento del arreglo: " + a1);
        }

    }
}
