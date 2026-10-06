package com.cultodeportivo.serializacion;

import com.cultodeportivo.modelo.Estudiante;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Serialización — convertir objeto Java → bytes (y viceversa).
 *
 *  ObjectOutputStream.writeObject(obj)  → serializa (escribe)
 *  ObjectInputStream.readObject()       → deserializa (lee)
 *
 *  La clase debe implementar Serializable (marker interface, sin métodos).
 *  Los campos transient NO se guardan.
 *
 * Casos de uso reales: caché a disco, estado de sesión, comunicación por red.
 * Alternativas modernas: JSON (Jackson/Gson), XML, Protocol Buffers.
 */
public class EjemploSerializacion {

    static final String TMP = System.getProperty("java.io.tmpdir");

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // ── 1. Serializar un objeto ────────────────────────────────────────
        Estudiante felipe = new Estudiante("Felipe", 21, "Ecuador", "Ciencias de la Computación")
                .agregarMateria("Algoritmos")
                .agregarMateria("Redes")
                .agregarMateria("IA");

        System.out.println("== Antes de serializar ==");
        System.out.println("  " + felipe);

        Path archivo = Path.of(TMP + "/estudiante.ser");

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(archivo.toFile())))) {
            oos.writeObject(felipe);
        }
        System.out.println("\nSerializado en: " + archivo);
        System.out.println("Tamaño: " + Files.size(archivo) + " bytes");

        // ── 2. Deserializar ────────────────────────────────────────────────
        Estudiante recuperado;
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(archivo.toFile())))) {
            recuperado = (Estudiante) ois.readObject();
        }

        System.out.println("\n== Después de deserializar ==");
        System.out.println("  " + recuperado);

        // ── 3. Verificar campos transient ─────────────────────────────────
        System.out.println("\n== Campos transient ==");
        System.out.println("  token original   : " + felipe.getToken());
        System.out.println("  token recuperado : " + recuperado.getToken()); // null → transient no persiste

        // ── 4. Serializar lista de objetos ────────────────────────────────
        List<Estudiante> estudiantes = List.of(
            new Estudiante("Felipe",   21, "Ecuador", "CS"),
            new Estudiante("Sofía",    22, "Ecuador", "Ingeniería"),
            new Estudiante("Mateo",    20, "Ecuador", "Matemáticas")
        );

        Path archivoLista = Path.of(TMP + "/estudiantes.ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(archivoLista.toFile())))) {
            oos.writeObject(estudiantes);
        }

        @SuppressWarnings("unchecked")
        List<Estudiante> listaRecuperada = (List<Estudiante>) new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(archivoLista.toFile()))
        ).readObject();

        System.out.println("\n== Lista deserializada ==");
        listaRecuperada.forEach(e ->
            System.out.printf("  %-10s | %-8s | %s%n", e.getNombre(), e.getPais(), e.getCarrera()));

        // ── Limpiar ───────────────────────────────────────────────────────
        Files.deleteIfExists(archivo);
        Files.deleteIfExists(archivoLista);
        System.out.println("\nArchivos .ser eliminados.");
    }
}
