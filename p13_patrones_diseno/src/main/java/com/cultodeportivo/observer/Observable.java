package com.cultodeportivo.observer;

import java.util.ArrayList;
import java.util.List;

// Sujeto abstracto: mantiene la lista de observadores y los notifica.
// "Publicador" en terminología moderna (pub/sub).
public abstract class Observable {

    private final List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o)  { observadores.add(o); }
    public void removerObservador(Observador o)  { observadores.remove(o); }

    // Notifica a TODOS los observadores registrados
    protected void notificarObservadores(Object dato) {
        for (Observador o : observadores) {
            o.actualizar(this, dato);
        }
    }
}
