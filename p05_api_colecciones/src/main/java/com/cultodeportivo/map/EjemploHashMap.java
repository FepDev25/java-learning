package com.cultodeportivo.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class EjemploHashMap {

    public static void main(String[] args) {

        // Crear un HashMap, String es el tipo de la llave y Object el tipo
        // del valor, o sea cualquier tipo
        Map<String, Object> persona = new HashMap<>();
        System.out.println("¿El mapa persona contiene elementos? " + !persona.isEmpty());
        persona.put(null, "1234");
        persona.put(null, "12345");
        persona.put("nombre", "John");
        persona.put("apellido", "Doe");
        persona.put("apellidoPaterno", "Doe");
        persona.put("email", "john.doe@email.com");
        persona.put("edad", 30);

        // Crear un mapa para la dirección
        Map<String, String> direccion = new HashMap<>();
        direccion.put("pais", "USA");
        direccion.put("estado", "California");
        direccion.put("ciudad", "Santa Barbara");
        direccion.put("calle", "One Street");
        direccion.put("numero", "120");

        // Agregar la dirección al mapa persona
        persona.put("direccion", direccion);

        System.out.println("Contenido completo del mapa persona: " + persona);

        // Obtener valores del mapa
        String nombre = (String) persona.get("nombre");
        System.out.println("Valor obtenido para 'nombre': " + nombre);
        String apellido = (String) persona.get("apellido");
        System.out.println("Valor obtenido para 'apellido': " + apellido);

        // Obtener valores del mapa anidado de dirección
        Map<String, String> direccionPersona = (Map<String, String>) persona.get("direccion");
        String pais = direccionPersona.get("pais");
        String ciudad = direccionPersona.get("ciudad");
        String barrio = direccionPersona.getOrDefault("barrio", "La playa");
        System.out.println("El país de " + nombre + " es: " + pais);
        System.out.println("La ciudad de " + nombre + " es: " + ciudad);
        System.out.println("El barrio de " + nombre + " es (valor por defecto): " + barrio);

        // Eliminar un valor del mapa
        //String apellidoPaterno = persona.remove("apellidoPaterno");
        boolean b = persona.remove("apellidoPaterno", "Doe");
        System.out.println("¿Se eliminó 'apellidoPaterno' con valor 'Doe'? " + b);
        System.out.println("Mapa persona después de eliminar 'apellidoPaterno': " + persona);

        boolean b2 = persona.containsKey("apellidoPaterno");
        System.out.println("¿El mapa contiene la clave 'apellidoPaterno'? " + b2);

        // Verificar si un valor existe en el mapa
        b2 = persona.containsValue("john.doe@email.co");
        System.out.println("¿El mapa contiene el valor 'john.doe@email.co'? " + b2);

        System.out.println("========================== Iterando sobre valores con values()");
        Collection<Object> valores = persona.values();
        for (Object v : valores) {
            System.out.println("Valor encontrado: " + v);
        }

        System.out.println("========================= Iterando sobre llaves con keySet()");
        Set<String> llaves = persona.keySet();
        for (String k : llaves) {
            System.out.println("Llave encontrada: " + k);
        }

        System.out.println("========================== Iterando sobre pares clave-valor con entrySet()");
        for (Map.Entry<String, Object> par : persona.entrySet()) {
            Object valor = par.getValue();
            if (valor instanceof Map) {
                System.out.println("Desglosando dirección:");
                Map<String, String> direccionMap = (Map<String, String>) valor;
                for (Map.Entry<String, String> parDir : direccionMap.entrySet()) {
                    System.out.println("  " + parDir.getKey() + " => " + parDir.getValue());
                }
            } else {
                System.out.println(par.getKey() + " => " + valor);
            }
        }

        System.out.println("========================== Iterando con keySet() y procesando dirección");
        for (String llave : persona.keySet()) {
            Object valor = persona.get(llave);
            if (valor instanceof Map) {
                String nom = (String) persona.get("nombre");
                Map<String, String> direccionMap = (Map<String, String>) valor;
                System.out.println("Información de dirección de " + nom + ":");
                System.out.println("  País: " + direccionMap.get("pais"));
                System.out.println("  Estado: " + direccionMap.get("estado"));
                System.out.println("  Ciudad: " + direccionMap.get("ciudad"));
            } else {
                System.out.println(llave + " => " + valor);
            }
        }

        System.out.println("========================== Iterando con forEach de Java 8");
        persona.forEach((llave, valor) -> {
            System.out.println(llave + " => " + valor);
        });

        System.out.println("Número total de elementos en el mapa: " + persona.size());
        System.out.println("¿El mapa persona contiene elementos? " + !persona.isEmpty());
        boolean b3 = persona.replace("nombre", "John", "Andrés");
        System.out.println("¿Se reemplazó 'nombre' de 'John' a 'Andrés'? " + b3);
        System.out.println("Mapa persona después del reemplazo: " + persona);

    }
}
