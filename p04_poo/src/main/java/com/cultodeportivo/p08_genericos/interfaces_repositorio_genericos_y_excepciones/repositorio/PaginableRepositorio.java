package com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.repositorio;

import java.util.List;

public interface PaginableRepositorio<T> {
    List<T> listar(int desde, int hasta);
}
