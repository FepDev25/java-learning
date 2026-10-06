package com.cultodeportivo.p08_genericos.clases_genericas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Camion<T> implements Iterable<T> {

    private List<T> objetos;
    private int max;

    public Camion(int max) {
        this.max = max;
        this.objetos = new ArrayList<>();
    }

    public void add(T objeto){
        if(this.objetos.size() <= max) {
            this.objetos.add(objeto);
        } else {
            throw new RuntimeException("no hay más espacio.");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return this.objetos.iterator();
    }

    public List<T> getObjetos() {
        return objetos;
    }

    public void setObjetos(List<T> objetos) {
        this.objetos = objetos;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    
}
