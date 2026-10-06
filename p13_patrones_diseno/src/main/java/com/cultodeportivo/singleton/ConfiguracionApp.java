package com.cultodeportivo.singleton;

// SINGLETON: garantiza una sola instancia en toda la JVM.
// Casos de uso: conexiones a BD, configuración global, loggers, caches.
//
// Variante 1 — Lazy initialization (clásica del curso):
//   instancia se crea solo cuando se necesita (getInstancia()).
//   Problema: NO thread-safe sin synchronized.
//
// Variante 2 — Enum Singleton (Joshua Bloch, "Effective Java"):
//   La JVM garantiza una sola instancia del enum, thread-safe, serializable.
//   Forma más robusta en Java moderno.
public class ConfiguracionApp {

    // ── Variante 1: Lazy + synchronized (thread-safe) ──────────────
    private static volatile ConfiguracionApp instancia;

    private final String universidad;
    private final String estudiante;
    private int semestre;

    // Constructor PRIVADO: nadie puede hacer new ConfiguracionApp()
    private ConfiguracionApp() {
        this.universidad = "Universidad Central del Ecuador";
        this.estudiante  = "Felipe Pérez";
        this.semestre    = 5;
        System.out.println("[ConfiguracionApp] Instancia creada — solo ocurre UNA vez.");
    }

    // Double-checked locking: evita synchronized en cada llamada tras la primera
    public static ConfiguracionApp getInstancia() {
        if (instancia == null) {
            synchronized (ConfiguracionApp.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionApp();
                }
            }
        }
        return instancia;
    }

    public String getUniversidad() { return universidad; }
    public String getEstudiante()  { return estudiante; }
    public int    getSemestre()    { return semestre; }
    public void   setSemestre(int s) { this.semestre = s; }

    @Override
    public String toString() {
        return estudiante + " | " + universidad + " | semestre " + semestre;
    }
}
