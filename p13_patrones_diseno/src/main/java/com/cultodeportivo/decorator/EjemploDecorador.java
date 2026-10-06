package com.cultodeportivo.decorator;

import com.cultodeportivo.decorator.decorador.*;

// DECORATOR: agrega comportamiento a un objeto envolviéndolo, sin modificar su clase.
// Alternativa a la herencia: composición en tiempo de ejecución.
// Cada decorador recibe un Formateador y devuelve un Formateador → se pueden anidar.
//
// En el JDK: InputStream → BufferedInputStream → DataInputStream (decoradores de streams).
public class EjemploDecorador {
    public static void main(String[] args) {

        String apunte = "Felipe estudia patrones de diseño en Ecuador";

        // ── Texto base ──────────────────────────────────────────────
        Formateador base = new TextoSimple(apunte);
        System.out.println("Base:        " + base.formatear());

        // ── Decoradores individuales ────────────────────────────────
        System.out.println("Mayúscula:   " + new MayusculaDecorador(base).formatear());
        System.out.println("Invertido:   " + new InvertirDecorador(base).formatear());
        System.out.println("Sin espacios:" + new ReemplazarEspaciosDecorador(base, "_").formatear());
        System.out.println("Corchete:    " + new CorcheteDecorador(base, ">>> ", " <<<").formatear());

        // ── Cadena de decoradores (orden importa) ───────────────────
        // base → mayúscula → invertir → reemplazar espacios
        Formateador cadena = new ReemplazarEspaciosDecorador(
                                 new InvertirDecorador(
                                     new MayusculaDecorador(base)), "-");
        System.out.println("\nCadena (mayús→invertir→espacios):");
        System.out.println("  " + cadena.formatear());

        // ── Cabecera de apunte ──────────────────────────────────────
        Formateador cabecera = new CorcheteDecorador(
                                   new MayusculaDecorador(
                                       new TextoSimple("Algoritmos y Estructuras de Datos")),
                                   "=== ", " ===");
        System.out.println("\nCabecera de apunte:");
        System.out.println("  " + cabecera.formatear());

        // ── Con lambda: Formateador es @FunctionalInterface ─────────
        // Se puede definir un decorador inline sin clase
        Formateador conFecha = () -> "[2025-03-13] " + base.formatear();
        System.out.println("\nCon fecha (lambda):  " + conFecha.formatear());
    }
}
