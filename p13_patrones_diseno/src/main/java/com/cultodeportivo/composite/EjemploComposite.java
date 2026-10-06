package com.cultodeportivo.composite;

// COMPOSITE: trata objetos individuales (hojas) y composiciones (ramas)
// de forma uniforme mediante una interfaz común (Componente).
// Resultado: el cliente llama mostrar() sin saber si es Archivo o Directorio.
public class EjemploComposite {
    public static void main(String[] args) {

        // Árbol de carpetas del repositorio de Felipe (CS Student, Ecuador)
        Directorio repo = new Directorio("Udemy_Master_Java");

        // p07_java8_lambda
        Directorio lambda = new Directorio("p07_java8_lambda");
        Directorio srcLambda = new Directorio("src/main/java/com/cultodeportivo");
        srcLambda.add(new Directorio("interfacefuncional")
                          .add(new Archivo("Operacion.java"))
                          .add(new Archivo("Calculadora.java"))
                          .add(new Archivo("EjemploInterfaceFuncional.java")))
                 .add(new Directorio("consumer")
                          .add(new Archivo("EjemploConsumer.java")))
                 .add(new Directorio("predicate")
                          .add(new Archivo("EjemploPredicado.java")))
                 .add(new Archivo("orden.txt"));
        lambda.add(srcLambda).add(new Archivo("pom.xml"));

        // p08_api_stream
        Directorio stream = new Directorio("p08_api_stream");
        Directorio srcStream = new Directorio("src/main/java/com/cultodeportivo");
        srcStream.add(new Directorio("modelo")
                          .add(new Archivo("Curso.java"))
                          .add(new Archivo("Estudiante.java")))
                 .add(new Directorio("stream")
                          .add(new Archivo("EjemploCrearStream.java"))
                          .add(new Archivo("EjemploStreamMap.java"))
                          .add(new Archivo("EjemploStreamFilter.java"))
                          .add(new Archivo("EjemploStreamReduce.java")));
        stream.add(srcStream);

        repo.add(lambda).add(stream).add(new Archivo("README.md"));

        // ── mostrar(): recorre el árbol completo recursivamente ──────
        System.out.println("=== ÁRBOL DEL REPOSITORIO ===");
        System.out.print(repo.mostrar(0));

        // ── buscar(): cortocircuito con anyMatch ─────────────────────
        System.out.println("=== BÚSQUEDA ===");
        String[] buscar = {"EjemploStreamFilter.java", "Calculadora.java",
                           "Examen.java", "p08_api_stream"};
        for (String nombre : buscar) {
            System.out.printf("  %-40s → %s%n", nombre,
                    repo.buscar(nombre) ? "encontrado" : "no encontrado");
        }

        // ── remove: eliminar un nodo ─────────────────────────────────
        System.out.println("\n=== DESPUÉS DE ELIMINAR README.md ===");
        repo.remove(new Archivo("README.md"));
        System.out.print(repo.mostrar(0));
    }
}
