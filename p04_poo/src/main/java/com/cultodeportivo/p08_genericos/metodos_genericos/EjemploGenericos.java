package com.cultodeportivo.p08_genericos.metodos_genericos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploGenericos {
    public static void main(String[] args) {

        System.out.println("=== EJEMPLO 1: Conversión de arreglo a lista (genérico simple) ===");
        Cliente[] clientesArreglo = {new Cliente("Luci", "Martínez"),
                new Cliente("Felipe", "Peralta")};
        List<Cliente> clientesLista = fromArrayToList(clientesArreglo);
        clientesLista.forEach(System.out::println);

        System.out.println("\n=== EJEMPLO 2: Conversión de arreglo de enteros a lista ===");
        Integer[] enterosArreglo = {1, 2, 3};
        List<Integer> enterosLista = fromArrayToList(enterosArreglo);
        enterosLista.forEach(System.out::println);

        System.out.println("\n=== EJEMPLO 3: Método genérico con dos parámetros de tipo ===");
        List<String> nombres = fromArrayToList(new String[]{"Andrés", "Pepe",
                "Luci", "Bea", "John"}, enterosArreglo);
        System.out.println("Lista de nombres:");
        nombres.forEach(System.out::println);

        System.out.println("\n=== EJEMPLO 4: Lista de clientes Premium ===");
        List<ClientePremium> clientesPremiumList = fromArrayToList(
                new ClientePremium[]{new ClientePremium("Paco", "Fernández")});
        clientesPremiumList.forEach(System.out::println);

        System.out.println("\n=== EJEMPLO 5: Imprimir clientes usando comodín (wildcard) ===");
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(new Cliente("Felipe", "Peralta"));
        
        System.out.println("Clientes regulares:");
        imprimirClientes(clientes);
        
        System.out.println("\nClientes de la lista:");
        imprimirClientes(clientesLista);
        
        System.out.println("\nClientes Premium:");
        imprimirClientes(clientesPremiumList);

        System.out.println("\n=== EJEMPLO 6: Método genérico para encontrar el máximo ===");
        System.out.println("Máximo de 1, 9 y 4 es: " + maximo(1, 9, 4));
        System.out.println("Máximo de 3.9, 11.6, 7.78 es: " + maximo(3.9, 11.6, 7.78));
        System.out.println("Máximo de 'zanahoria', 'arándano', 'manzana' es: "
                + maximo("zanahoria", "arándano", "manzana"));

        System.out.println("\n=== FIN DE EJEMPLOS ===");
    }

    // Metodo genérico que convierte un arreglo en una lista
    public static <T> List<T> fromArrayToList(T[] c){
        return Arrays.asList(c);
    }

    // Método genérico con restricción de tipo que convierte un arreglo en una lista
    // donde T debe ser una subclase de Number
    public static <T extends Number> List<T> fromArrayToList(T[] c){
        return Arrays.asList(c);
    }

    // Método genérico con múltiples restricciones de tipo, donde T debe ser una subclase de Cliente
    // y debe implementar la interfaz Comparable
    public static <T extends Cliente & Comparable<T>> List<T> fromArrayToList(T[] c){
        return Arrays.asList(c);
    }

    // Método genérico con dos parámetros de tipo, T y G
    public static <T, G> List<T> fromArrayToList(T[] c, G[] x){
        for(G elemento: x){
            System.out.println(elemento);
        }
        return Arrays.asList(c);
    }

    // Método que imprime una lista de clientes o de cualquier subclase de Cliente, usando comodín
    public static void imprimirClientes(List<? extends Cliente> clientes){
        clientes.forEach(System.out::println);
    }

    // Método genérico para encontrar el máximo entre tres elementos comparables, usando restricción de tipo
    public static <T extends Comparable<T>> T maximo(T a, T b, T c){
        T max = a;
        if(b.compareTo(max) > 0){
            max = b;
        }
        if(c.compareTo(max) > 0){
            max = c;
        }
        return max;
    }
}
