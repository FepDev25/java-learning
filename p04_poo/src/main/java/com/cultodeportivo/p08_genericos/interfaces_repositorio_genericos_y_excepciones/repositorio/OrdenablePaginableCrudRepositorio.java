package com.cultodeportivo.p08_genericos.interfaces_repositorio_genericos_y_excepciones.repositorio;

public interface OrdenablePaginableCrudRepositorio<T> extends OrdenableRepositorio<T>,
                        PaginableRepositorio<T>, CrudRepositorio<T>, ContableRepositorio {
}
