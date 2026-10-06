package com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.repositorio;

import java.util.List;

public interface OrdenableRepositorio<T> {
    List<T> listar(String campo, Direccion dir);
}
