package com.cultodeportivo.observer;

import java.util.HashMap;
import java.util.Map;

// Sujeto concreto: el sistema de notas de la universidad.
// Cuando un profesor publica o actualiza una nota, notifica a todos los observadores.
public class SistemaNotas extends Observable {

    private final String materia;
    private final Map<String, Double> notas = new HashMap<>();

    public SistemaNotas(String materia) {
        this.materia = materia;
    }

    public void publicarNota(String estudiante, double nota) {
        notas.put(estudiante, nota);
        System.out.println("\n[SistemaNotas] " + materia + " → " + estudiante + ": " + nota);
        notificarObservadores(new EventoNota(materia, estudiante, nota));
    }

    public String getMateria() { return materia; }

    // Dato que viaja a los observadores
    public record EventoNota(String materia, String estudiante, double nota) {
        public boolean aprobado() { return nota >= 7.0; }
    }
}
