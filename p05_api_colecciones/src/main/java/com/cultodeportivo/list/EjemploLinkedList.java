package com.cultodeportivo.list;

import java.util.LinkedList;
import java.util.ListIterator;

import com.cultodeportivo.modelo.Alumno;

public class EjemploLinkedList {
    public static void main(String[] args) {

        LinkedList<Alumno> enlazada = new LinkedList<>();

        // Estado inicial
        System.out.println("=== ESTADO INICIAL ===");
        System.out.println("Lista: " + enlazada + ", size = " + enlazada.size());
        System.out.println("¿Está vacía? = " + enlazada.isEmpty());

        // Agregar elementos
        System.out.println("\n=== AGREGANDO ELEMENTOS CON add() ===");
        enlazada.add(new Alumno("Pato", 5));
        enlazada.add(new Alumno("Cata", 6));
        enlazada.add(new Alumno("Luci", 4));
        enlazada.add(new Alumno("Jano", 7));
        enlazada.add(new Alumno("Andres", 3));
        
        // Estado con elementos
        System.out.println("Lista después de agregar: " + enlazada + ", size = " + enlazada.size());

        System.out.println("\n=== AGREGANDO AL INICIO Y AL FINAL ===");
        enlazada.addFirst(new Alumno("Zeus", 5));
        System.out.println("Después de addFirst(Zeus): " + enlazada);
        enlazada.addLast(new Alumno("Atenea", 6));
        System.out.println("Después de addLast(Atenea): " + enlazada + ", size = " + enlazada.size());

        System.out.println("\n=== CONSULTANDO ELEMENTOS ===");
        System.out.println("Primer elemento (getFirst): " + enlazada.getFirst());
        System.out.println("Último elemento (getLast): " + enlazada.getLast());
        System.out.println("Elemento en índice 2 (get(2)): " + enlazada.get(2));

        System.out.println("\n=== ELIMINANDO PRIMER Y ÚLTIMO ELEMENTO ===");
        enlazada.removeFirst();
        System.out.println("Después de removeFirst(): " + enlazada);
        enlazada.removeLast();
        System.out.println("Después de removeLast(): " + enlazada + ", size = " + enlazada.size());

        System.out.println("\n=== ELIMINANDO POR OBJETO ===");
        enlazada.remove(new Alumno("Jano", 7));
        System.out.println("Después de remove(Jano): " + enlazada + ", size = " + enlazada.size());

        System.out.println("\n=== AGREGANDO Y BUSCANDO ÍNDICE ===");
        Alumno a = new Alumno("Lucas", 5);
        enlazada.addLast(a);
        System.out.println("Después de addLast(Lucas): " + enlazada);
        System.out.println("Índice de Lucas = " + enlazada.indexOf(a));

        System.out.println("\n=== ELIMINANDO POR ÍNDICE ===");
        enlazada.remove(2);
        System.out.println("Después de remove(índice 2): " + enlazada + ", size = " + enlazada.size());
        System.out.println("Nuevo índice de Lucas = " + enlazada.indexOf(a));

        System.out.println("\n=== REEMPLAZANDO ELEMENTO ===");
        enlazada.set(3, new Alumno("Lalo", 7));
        System.out.println("Después de set(3, Lalo): " + enlazada + ", size = " + enlazada.size());

        System.out.println("\n=== ITERANDO HACIA ADELANTE ===");
        ListIterator<Alumno> li = enlazada.listIterator();
        while(li.hasNext()){
            Alumno alumno = li.next();
            System.out.println("  -> " + alumno);
        }
        
        System.out.println("\n=== ITERANDO HACIA ATRÁS ===");
        while(li.hasPrevious()){
            Alumno alumno = li.previous();
            System.out.println("  <- " + alumno);
        }
        
    }
}
